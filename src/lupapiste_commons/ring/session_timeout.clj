(ns lupapiste-commons.ring.session-timeout
  (:import [java.util Date]))

(defn session-expired? [request]
  (let [now     (.getTime (Date.))
        expires (get-in request [:session :expires] now)]
    (< expires now)))

(defn wrap-session-timeout
  ([handler]
   (wrap-session-timeout handler {:status 401}))
  ([handler response-on-expired]
   (fn [request]
     (if (session-expired? request)
       response-on-expired
       (handler request)))))
