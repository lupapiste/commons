(ns lupapiste-commons.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl-server]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))

(defrecord NreplServer [port bind]
  component/Lifecycle
  (start [this]
    (if (:server this)
      this
      (let [component (assoc this :server (nrepl-server/start-server :port port :bind bind))]
        (timbre/info (str "Started nrepl server in port: " port))
        component)))
  (stop [this]
    (when (:server this)
      (nrepl-server/stop-server (:server this))
      (timbre/info "Stopped nrepl server"))
    (assoc this :server nil)))

(defn new-nrepl-server [port]
  (map->NreplServer {:port port :bind "localhost"}))
