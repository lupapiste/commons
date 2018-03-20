(ns lupapiste-commons.ring.autologin
  (:require [clojure.core.memoize :as memo]
            [clj-http.client :as http]
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

(defn wrap-sso-autologin [handler autologin-check-url]
  (fn [{:keys [headers remote-addr] :as request}]
    (let [{:strs [authorization x-real-ip]} headers]
      (try
        (if-let [user (fetch-login-user autologin-check-url authorization (or x-real-ip remote-addr))]
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
