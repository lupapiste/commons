(ns lupapiste-commons.ring.autologin
  (:require [clj-http.client :as http]
            [clojure.core.memoize :as memo]
            [clojure.string :as s]
            [taoensso.timbre :as timbre]))

(defn- keyword-authz [org-authz]
  (reduce (fn [acc [k vs]]
            (assoc acc k (set (map keyword vs))))
          {}
          org-authz))

(defn- do-fetch-login-user [url basic-auth request-ip]
  (when (and basic-auth request-ip)
    (-> (http/get url {:query-params {:basic-auth basic-auth
                                      :ip request-ip}
                       :as :json})
        :body
        (update :orgAuthz keyword-authz))))

(def ^{:arglists '([url basic-auth request-ip])} fetch-login-user
  (memo/ttl do-fetch-login-user :ttl/threshold 5000))

(defn- blank-as-nil [s]
  (when-not (some-> s s/blank?)
    s))

(defn client-ip-map
  "Resolves remote client ip header values."
  [{:keys                     [remote-addr]
    {:strs [x-real-ip
            x-forwarded-for]} :headers}]
  (->> {:remote-addr     remote-addr
        :x-real-ip       x-real-ip
        :x-forwarded-for x-forwarded-for}
       (filter (comp blank-as-nil second))
       (into {})))

(defn client-ip
  "Parses HTTP `request` and returns the originating ip address for the remote client. The
  address is then later resolved against the organization autologin configuration. If
  `gcp?` is true (default) the `x-forwarded-for` is taken into account and prioritized,
  since it is used by GCP."
  ([request gcp?]
   (let [{:keys [remote-addr x-forwarded-for
                 x-real-ip]} (client-ip-map request)]
     (or
       (when gcp?
         ;; Note: this x-forwarded-for parsing is GCP specific!
         ;; GCP appends client IP + load balancer IP after whatever existing contents
         ;; the header might already have
         (when-let [[_lb-ip ip & _] (some-> x-forwarded-for
                                            ;; Should contain at least two addresses
                                            (s/split #",")
                                            reverse)]
           (some-> ip s/trim blank-as-nil)))
       x-real-ip
       remote-addr)))
  ([request] (client-ip request true)))

(defn wrap-sso-autologin [handler autologin-check-url]
  (fn [{:keys [headers] :as request}]
    (let [{:strs [authorization]} headers
          ip                      (client-ip request)]
      (try
        (if-let [user (fetch-login-user autologin-check-url authorization ip)]
          (-> (assoc request :autologin-user user)
              (handler))
          (handler request))
        (catch Exception e
          (let [{:keys [status body]} (ex-data e)]
            (if status
              (do
                (timbre/warn e)
                {:status status
                 :body   (or body "Unknown authentication error")})
              ; Probably an exception from the handler chain
              (throw e))))))))
