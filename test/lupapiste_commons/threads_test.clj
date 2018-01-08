(ns lupapiste-commons.threads-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.threads :refer :all]))

(deftest one-thread
  (let [result (atom [])
        pool   (threadpool 1 "lonely worker")
        thread (submit pool (Thread/sleep 100) (swap! result conj "done"))]
    (wait-for-threads [thread])
    (is (= @result ["done"]))))

(deftest threads-finish-in-original-order
  (let [result  (atom [])
        pool    (threadpool 1 "lonely worker")
        threads (map #(submit pool
                              (Thread/sleep (+ 60 (* 150 (rem % 3))))
                              (swap! result conj %)) (range 6))]
    (wait-for-threads threads)
    (is (= @result [0 1 2 3 4 5]))))
