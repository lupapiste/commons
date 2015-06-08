(ns lupapiste-commons.ring.session-timeout
  (:import [java.util.concurrent TimeUnit]))

(defn get-session-timeout [request]
  (get-in request [:session :user :session-timeout] (.toMillis TimeUnit/HOURS 4)))

(defn increment-timeout [request now]
  (+ now (get-session-timeout request)))

(defn wrap-session-timeout [handler]
  (fn [request]
    (let [now (.getTime (java.util.Date.))
          expires (get-in request [:session :expires] now)
          expired? (< expires now)
          response (handler request)]
      (if expired?
        (assoc response :session (assoc-in (or (:session response) (:session request))
                                           [:expires]
                                           (increment-timeout request now)))
        response))))
