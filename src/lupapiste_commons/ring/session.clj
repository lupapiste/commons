(ns lupapiste-commons.ring.session
  (:require [ring.middleware.session.cookie :refer [cookie-store]]
            [ring.middleware.session.store :as st]))

(defn read-key [session-key-path]
  (with-open [is (java.io.FileInputStream. session-key-path)]
    (let [bytes (byte-array 16)]
      (.read is bytes)
      bytes)))

(defprotocol Rekeyable
  (rekey [this]))

(defn rekeyable
  "Returns a SessionStore with additional method 'rekey', which will
  reload the secret key given by the 'session-key-path' argument.
  
  Can be used with Compojure for example like this:

  (defn rekey-route [store]
    (POST \"/rekey\" request
      (if (#{\"127.0.0.1\" \"0:0:0:0:0:0:0:1\"} (:remote-addr request))
        (do
          (rs/rekey store)
          (response \"rekeyd\"))
        (status (response \"Unauthorized\") 401))))"
  [session-key-path]
  (let [store (atom (cookie-store {:key (read-key session-key-path)}))]
    (reify
      st/SessionStore
      (read-session [_ key]
        (st/read-session @store key))
      (write-session [_ key data]
        (st/write-session @store key data))
      (delete-session [_ key]
        (st/delete-session @store key))
      Rekeyable
      (rekey [_]
        (reset! store (cookie-store {:key (read-key session-key-path)}))))))
