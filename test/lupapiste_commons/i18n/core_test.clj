(ns lupapiste-commons.i18n.core-test
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.test :refer :all]
            [lupapiste-commons.i18n.core :refer :all]))

(deftest read-translations
  (testing ":fallback-to-default-lang flag treats empty strings as missing value
            and replaces them with default language resouces value"
    (let [input [["label" "fi" "moi"]
                 ["label" "sv" ""]]
          string (s/join "\n" (map #(s/join " " (mapv pr-str %)) input))]
      (is (= {:languages [:fi :sv]
              :translations {'label {:fi "moi" :sv "moi"}}}
             (read-translations (.getBytes string) :fallback-to-default-lang true)))))

  (testing "By default empty string are not considered as missing translations"
    (let [input [["label" "fi" "moi"]
                 ["label" "sv" ""]]
          string (s/join "\n" (map #(s/join " " (mapv pr-str %)) input))]
      (is (= {:languages [:fi :sv]
              :translations {'label {:fi "moi" :sv ""}}}
             (read-translations (.getBytes string)))))))
