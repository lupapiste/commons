(ns lupapiste-commons.ring.mongo-session
  "MongoDB-backed Ring SessionStore
  - Session keys are generated with 256-bit CSPRNG randomness (java.security.SecureRandom)
    and encoded as URL-safe Base64 (43 characters, no padding).
  - The database stores a SHA-256 hash of the session key as the document `_id`"
  (:require [ring.middleware.session.store :as store]
            [schema.core :as sc]
            [schema-tools.core :as st])
  (:import [java.security MessageDigest SecureRandom]
           [java.util Base64 Date HexFormat]))

(set! *warn-on-reflection* true)

(def ^:private ^SecureRandom csprng (SecureRandom.))

(defn- new-session-key
  "Generates a 256-bit cryptographically random session key, returned as URL-safe Base64 (no padding)."
  ^String []
  (let [buf (byte-array 32)]
    (.nextBytes csprng buf)
    (.encodeToString (.withoutPadding (Base64/getUrlEncoder)) buf)))

(def ^:private ^HexFormat hex-format (HexFormat/of))

(defn session-id->db-id
  "Derives the database document `_id` from the session key by computing its SHA-256 hash"
  ^String [^String session-key]
  (let [digest     (MessageDigest/getInstance "SHA-256")
        hash-bytes (.digest digest (.getBytes session-key "UTF-8"))]
    (.formatHex hex-format hash-bytes)))

(defn- now-date ^Date [] (Date.))

(defn coerce-org-authz
  [org-authz]
  (update-vals org-authz #(set (map keyword %))))

(defn with-org-auth [user]
  (if (coll? (get-in user [:orgAuthz]))
    (update user :orgAuthz coerce-org-authz)
    user))

(defprotocol MongoAdapter
  "MongoDB operations adapter"
  (-get-session [this id fields]
    "Return a single document map matching the given `id`, projecting `fields`. Returns
    nil if no document is found. Implementations must dissoc `:_id` from  the MongoDB
    document so that the return value matches the `SessionResult` schema.")
  (-update-session! [this id session]
    "Execute a MongoDB update with `query` and $set the `session` to the document.
     Does NOT insert if document does not already exist.")
  (-insert-session! [this id session]
    "Execute a MongoDB insert with `id` as the document :_id and `session` as the other
    document data")
  (-remove-session! [this id]
    "Remove the document with the given `id`."))

(sc/defschema Session
  "Data $set on every session write."
  {:data                         sc/Any
   :user-id                      (sc/maybe sc/Str)
   :updated-at                   Date
   (sc/optional-key :created-at) Date})

(sc/defschema SessionResult
  "Schema for a session document read from the database."
  (st/optional-keys Session))

(sc/defn ^:private get-session :- (sc/maybe SessionResult)
  [adapter :- (sc/protocol MongoAdapter)
   id :- sc/Str
   fields :- [sc/Keyword]]
  (-get-session adapter id fields))

(sc/defn ^:private update-session!
  [adapter :- (sc/protocol MongoAdapter)
   id :- sc/Str
   session :- Session]
  (-update-session! adapter id session))

(sc/defn ^:private insert-session!
  [adapter :- (sc/protocol MongoAdapter)
   id :- sc/Str
   session :- Session]
  (-insert-session! adapter id session))

(sc/defn ^:private remove-session!
  [adapter :- (sc/protocol MongoAdapter)
   id :- sc/Str]
  (-remove-session! adapter id))

(defrecord MongoSessionStore [adapter read-only? store-session-data?] store/SessionStore
  (read-session [_ key]
    (when key
      (some-> (get-session adapter (session-id->db-id key) [:data])
              :data
              (update :user with-org-auth))))

  (write-session [_ key data]
    (if (and (not read-only?)
             (store-session-data? data))
      (let [now     (now-date)
            new-key (or key (new-session-key))]
        (if (nil? key)
          (insert-session! adapter
                           (session-id->db-id new-key)
                           {:data       data
                            :user-id    (get-in data [:user :id])
                            :updated-at now
                            :created-at now})
          (update-session! adapter
                           (session-id->db-id new-key)
                           {:data       data
                            :user-id    (get-in data [:user :id])
                            :updated-at now}))
        new-key)
      key))

  (delete-session [_ key]
    (when (and (not read-only?) key)
      (remove-session! adapter (session-id->db-id key)))
    nil))

(defn make-store
  "Create Ring SessionStore backed by MongoDB.
  `adapter`    – an implementation of MongoAdapter.
  `read-only?` – when true, write-session and delete-session are no-ops. Defaults to false
  `store-session-data?` - a function from session data to bool that can, based on the session
                          data, be used to determine whether the session document should be
                          written to database at all. For example, if the data contains no
                          meaningful data, storing the mongo session document can be
                          prevented by returning false."
  ([adapter & {:keys [read-only? store-session-data?]}]
   (->MongoSessionStore adapter (boolean read-only?) (or store-session-data? (constantly true)))))
