(ns lupapiste-commons.i18n-test
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.test :refer :all]
            [lupapiste-commons.i18n :refer :all]))

(deftest use-default-lang
  (testing "String from default language used if no translation present"
    (let [input [["label" "fi" "moi"]
                 ["label" "sv" ""]]
          string (s/join "\n" (map #(s/join " " (mapv pr-str %)) input))]
      (is (= {:languages [:fi :sv]
              :translations {'label {:fi "moi" :sv "moi"}}}
             (read-translations (.getBytes string)))))))
