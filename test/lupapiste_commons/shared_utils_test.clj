(ns lupapiste-commons.shared-utils-test
  (:require [lupapiste-commons.shared-utils :as u]
            [clojure.test :refer :all]))

(deftest dissoc-in-map
  (is (= (u/dissoc-in {:a {:b [1 2 3]}} [:a :b 0])
         {:a {:b [2 3]}})))

(deftest dissoc-in-vector
  (is (= (u/dissoc-in [0 1 2] [0])
       [1 2])))
