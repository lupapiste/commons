(ns lupapiste-commons.ring.utils
  (:require [clojure.string :as s]
            [taoensso.timbre :as timbre]))

(defn wrap-exception-logging [handler]
  (fn [request]
    (try
      (handler request)
      (catch Throwable t
        (timbre/warn t (str "Error while prosessing request: " request))
        {:status 500
         :body "Error while prosessing request"}))))

(defn wrap-request-logging [handler]
  (fn [{:keys [remote-addr request-method uri headers] :as request}]
    (let [start (System/currentTimeMillis)
          {:keys [status] :as response} (handler request)
          end (System/currentTimeMillis)]
      (timbre/info (s/join " " [remote-addr (s/upper-case (name request-method)) uri status (- end start) (get headers "user-agent" "")]))
      response)))

(defn optional-middleware [current-middleware middleware pred]
  (if pred
    (middleware current-middleware)
    current-middleware))

(defn wrap-no-ajax-cache [handler]
  (fn [request]
    (let [response (handler request)]
      (if (-> request :headers ^String (get "x-requested-with" "") (.contains "XMLHttpRequest"))
        (assoc-in response [:headers "Expires"] "0")
        response))))

(defn wrap-csp-header [handler policies]
  (let [csp-header (->> policies
                        (map (fn [[k v]]
                               (str (name k) " " v)))
                        (s/join "; ")
                        (array-map "Content-Security-Policy"))]
    (fn [request]
      (-> (handler request)
          (update :headers merge csp-header)))))
