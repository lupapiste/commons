(ns lupapiste-commons.attachment-types-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.attachment-types :as target]))

(deftest merge-attachment-listings
  (testing "Merge correctly two attachment listings"
    (let [merged (target/merge-attachment-listings
                   [:group1 [:doc-id-1]
                    :group2 [:doc-id-2]]
                   [:group1 [:doc-id-1 :doc-id-3]
                    :group3 [:doc-id-4]])
          expected-as-map {:group1 [:doc-id-1 :doc-id-3]
                           :group2 [:doc-id-2]
                           :group3 [:doc-id-4]}]
      (println merged)
      (is (vector? merged))
      (is (= (apply hash-map merged) expected-as-map))))
  (testing "merge correctly tree listings"
    (let [merged (target/merge-attachment-listings
                   [:group1 [:doc-id-1]
                    :group2 [:doc-id-2]]
                   [:group1 [:doc-id-1 :doc-id-3]
                    :group3 [:doc-id-5]]
                   [:group1 [:doc-id-7]
                    :group3 [:doc-id-6]])
          expected-as-map {:group1 [:doc-id-1 :doc-id-3 :doc-id-7]
                           :group2 [:doc-id-2]
                           :group3 [:doc-id-3 :doc-id-6]}]
      (is (= (apply hash-map merged) expected-as-map)))))
