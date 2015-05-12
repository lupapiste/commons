(ns lupapiste-commons.i18n-resources-test
  (:require [lupapiste-commons.i18n-resources :refer :all]
            [clojure.test :refer :all])
  (:import [java.io File]))

(deftest roundtrip
  (testing "What is put into a pipe comes out of it staying the same, where pipe is: map -> txt -> map -> excel -> map"
    (let [txt-file (doto (File/createTempFile "translations" ".txt"))
          excel-file (doto (File/createTempFile "translations" ".xlsx"))]
      (let [translations {'label {:fi "Hyvää huomenta" :sv "Gud morgon"}
                          '^:source-changed button {:fi "Ok" :sv "Jättebra"}}
            _ (map->txt translations txt-file)
            from-txt (txt->map txt-file)
            _ (write-excel from-txt excel-file)
            from-excel (excel->map excel-file)]
        (.delete txt-file)
        (.delete excel-file)
        (is (= translations from-excel))))))
