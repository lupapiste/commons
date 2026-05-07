(ns lupapiste-commons.ring.mongo-session-test
  (:require [clojure.test :refer :all]
            [ring.middleware.session.store :as store]
            [schema.core :as sc]
            [lupapiste-commons.ring.mongo-session :as mongo-session])
  (:import [java.util Date]))

(use-fixtures :once (fn [tests] (sc/with-fn-validation (tests))))

(defn- track-calls
  "Returns an atom and a function. The function records each call's args into the atom
  and returns `return-val`."
  [return-val]
  (let [calls (atom [])]
    [calls (fn [& args] (swap! calls conj (vec args)) return-val)]))

(defn- test-adapter
  "Builds a MongoAdapter from individual functions."
  [{:keys [get-session update-session! insert-session! remove-session!]
    :or   {get-session     (constantly nil)
           update-session! (constantly nil)
           insert-session! (constantly nil)
           remove-session! (constantly nil)}}]
  (reify mongo-session/MongoAdapter
    (-get-session [_ id fields] (get-session id fields))
    (-update-session! [_ id session] (update-session! id {"$set" session}))
    (-insert-session! [_ id session] (insert-session! id session))
    (-remove-session! [_ id] (remove-session! id))))

(deftest coerce-org-authz-test
  (testing "converts string values to keyword sets"
    (is (= {:org-1 #{:authority :reader}
            :org-2 #{:admin}}
           (mongo-session/coerce-org-authz {:org-1 ["authority" "reader"]
                                            :org-2 ["admin"]}))))
  (testing "handles empty map"
    (is (= {} (mongo-session/coerce-org-authz {}))))
  (testing "handles already-keyword values"
    (is (= {:org-1 #{:authority}}
           (mongo-session/coerce-org-authz {:org-1 [:authority]})))))

(deftest with-org-auth-test
  (testing "coerces orgAuthz when it is a collection"
    (is (= {:name "u" :orgAuthz {:org-1 #{:authority}}}
           (mongo-session/with-org-auth {:name "u" :orgAuthz {:org-1 ["authority"]}}))))
  (testing "returns user unchanged when orgAuthz is nil"
    (is (= {:name "u"} (mongo-session/with-org-auth {:name "u"}))))
  (testing "returns user unchanged when orgAuthz is not a collection"
    (is (= {:name "u" :orgAuthz nil}
           (mongo-session/with-org-auth {:name "u" :orgAuthz nil}))))
  (testing "returns nil when user is nil"
    (is (= nil
           (mongo-session/with-org-auth nil)))))

(deftest read-session-test
  (testing "returns nil when key is nil"
    (let [session-store (mongo-session/make-store (test-adapter {}))]
      (is (nil? (store/read-session session-store nil)))))

  (testing "returns nil when session not found"
    (let [session-store (mongo-session/make-store (test-adapter {:get-session (constantly nil)}))]
      (is (nil? (store/read-session session-store "some-key")))))

  (testing "returns data with orgAuthz coerced when found"
    (let [stored-data   {:data {:user {:name     "Test User"
                                       :orgAuthz {:org-1 ["authority" "reader"]}}
                                :foo  "bar"}}
          [get-calls mock-get] (track-calls stored-data)
          session-store (mongo-session/make-store (test-adapter {:get-session mock-get}))]
      (is (= {:user {:name     "Test User"
                     :orgAuthz {:org-1 #{:authority :reader}}}
              :foo  "bar"}
             (store/read-session session-store "some-key")))
      (is (= [[(mongo-session/session-id->db-id "some-key") [:data]]]
             @get-calls))))

  (testing "returns data when user has no orgAuthz"
    (let [stored-data   {:data {:user {:name "Test User"} :foo "bar"}}
          session-store (mongo-session/make-store (test-adapter {:get-session (constantly stored-data)}))]
      (is (= {:user {:name "Test User"} :foo "bar"}
             (store/read-session session-store "some-key"))))))

(deftest write-session-test
  (testing "creates new session (nil key) — inserts"
    (let [[insert-calls mock-insert] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:insert-session! mock-insert}))]
      (let [new-key (store/write-session session-store nil {:user {:id "user-1"}})]
        (is (string? new-key))
        (is (= 43 (count new-key)))
        (is (re-matches #"^[A-Za-z0-9_-]{43}$" new-key))
        (is (= 1 (count @insert-calls)))
        (let [[id update-doc] (first @insert-calls)]
          (is (= (mongo-session/session-id->db-id new-key) id))
          (is (= {:user {:id "user-1"}} (get update-doc :data)))
          (is (= "user-1" (get update-doc :user-id)))
          (is (instance? Date (get update-doc :updated-at)))
          (is (instance? Date (get update-doc :created-at)))))))

  (testing "updates existing session (non-nil key, no rotation) — plain update, returns same key"
    (let [[update-calls mock-update] (track-calls nil)
          [insert-calls mock-insert] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:update-session! mock-update
                                                                 :insert-session! mock-insert}))]
      (let [returned-key (store/write-session session-store "existing-key" {:user {:id "user-1"}})]
        (is (= "existing-key" returned-key))
        (is (= 0 (count @insert-calls)) "should NOT insert for existing key")
        (is (= 1 (count @update-calls)))
        (let [[id update-doc] (first @update-calls)]
          (is (= (mongo-session/session-id->db-id "existing-key") id))
          (is (= {:user {:id "user-1"}} (get-in update-doc ["$set" :data])))
          (is (= "user-1" (get-in update-doc ["$set" :user-id])))
          (is (instance? Date (get-in update-doc ["$set" :updated-at])))
          (is (nil? (get update-doc "$setOnInsert")) "no $setOnInsert on plain update")))))

  (testing "read-only mode returns key unchanged, no DB calls"
    (let [[insert-calls mock-insert] (track-calls nil)
          [update-calls mock-update] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:insert-session! mock-insert
                                                                 :update-session! mock-update})
                                                  {:read-only? true})]
      (is (= "my-key" (store/write-session session-store "my-key" {:user {:id "u"}})))
      (is (= 0 (count @insert-calls)))
      (is (= 0 (count @update-calls)))))

  (testing "read-only mode with nil key returns nil"
    (let [session-store (mongo-session/make-store (test-adapter {}) {:read-only? true})]
      (is (nil? (store/write-session session-store nil {:user {:id "u"}}))))))

(deftest delete-session-test
  (testing "removes session from database"
    (let [[remove-calls mock-remove] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:remove-session! mock-remove}))]
      (is (nil? (store/delete-session session-store "some-key")))
      (is (= 1 (count @remove-calls)))
      (is (= [(mongo-session/session-id->db-id "some-key")] (first @remove-calls)))))

  (testing "does nothing when key is nil"
    (let [[remove-calls mock-remove] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:remove-session! mock-remove}))]
      (is (nil? (store/delete-session session-store nil)))
      (is (= 0 (count @remove-calls)))))

  (testing "does nothing when read-only"
    (let [[remove-calls mock-remove] (track-calls nil)
          session-store (mongo-session/make-store (test-adapter {:remove-session! mock-remove}) {:read-only? true})]
      (is (nil? (store/delete-session session-store "some-key")))
      (is (= 0 (count @remove-calls))))))
