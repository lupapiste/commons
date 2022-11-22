(ns lupapiste-commons.i18n.resources-test
  (:require [clojure.test :refer :all]
            [dk.ative.docjure.spreadsheet :as xls]
            [lupapiste-commons.i18n.resources :as resources]
            [lupapiste-commons.i18n.txt-resources :as txt-resources])
  (:import [java.io File]))

(def translations
  {:languages    [:fi :sv]
   :translations {'label            {:fi "Hyvää huomenta" :sv "Gud morgon"}
                  '^:source-changed button {:fi "Ok" :sv "Jättebra"}}})

(deftest roundtrip
  (testing "What is put into a pipe comes out of it staying the same, where pipe is: map -> txt -> map -> excel -> map"
    (let [txt-file   (File/createTempFile "translations" ".txt")
          excel-file (File/createTempFile "translations" ".xlsx")
          _          (txt-resources/write-txt translations txt-file)
          from-txt   (txt-resources/txt->map txt-file)
          _          (resources/write-excel from-txt excel-file)
          from-excel (resources/excel->map excel-file)]
      (.delete txt-file)
      (.delete excel-file)
      (is (= translations from-txt from-excel)))))

(deftest find-missing-translations
  (let [no-texts {:languages [:fi :sv]
                  :translations {'label {}}}
        empty-default-text {:languages [:fi :sv]
                            :translations {'label {:fi ""}}}
        empty-swedish-text {:languages [:fi :sv]
                            :translations {'label {:fi "Heippa" :sv ""}}}
        source-text-changed {:languages [:fi :sv]
                             :translations {'^:source-changed label {:fi "Punainen" :sv "Blå"}}}]
    (is (= (ffirst (:translations (txt-resources/missing-translations no-texts)))
           'label))
    (is (= (ffirst (:translations (txt-resources/missing-translations empty-default-text)))
           'label))
    (is (= (ffirst (:translations (txt-resources/missing-translations empty-swedish-text)))
           'label))
    (is (= (ffirst (:translations (txt-resources/missing-translations source-text-changed)))
           'label))))

(deftest missing-translations-with-lang
  (let [empty-english {:languages [:fi :sv :en]
                       :translations {'label {:fi "Heippa" :sv "Hej" :en ""}}}
        missing-english {:languages [:fi :sv]
                         :translations {'label {:fi "Heippa" :sv "Hej"}}}]
    (is (= (ffirst (:translations (txt-resources/missing-translations empty-english)))
           'label))
    (is (nil? (ffirst (:translations (txt-resources/missing-translations empty-english :sv)))) "English check is discarded")
    (is (= (ffirst (:translations (txt-resources/missing-translations empty-english :en)))
           'label))
    (is (= (ffirst (:translations (txt-resources/missing-translations missing-english :en)))
           'label))
    (is (= (:languages (txt-resources/missing-translations empty-english :en))
           [:fi :en]) "Fi and selected language returned in languages")))

(deftest write-key-test
  (let [loc-key (symbol "application/rtf")]
    (is (= (txt-resources/write-key loc-key) "application/rtf") "'Namespace' should not be stripped off")
    (is (= (txt-resources/write-key (with-meta loc-key {:source-changed true}))
           "application/rtf!")
        "When source is changed, '!' is added to key")))

(deftest empty-lines-test
  (let [file-with-empty-lines (File/createTempFile "translations" ".txt")]
    (txt-resources/write-txt translations file-with-empty-lines)
    (spit file-with-empty-lines
          (str  "\n\n"
                (#'lupapiste-commons.i18n.txt-resources/txt-line "moi" "fi" "Moro!")
                (#'lupapiste-commons.i18n.txt-resources/txt-line "moi" "sv" "Hej!"))
          :append true)
    (let [from-txt (txt-resources/txt->map file-with-empty-lines)]
      (is (= (:translations from-txt)
             (merge (:translations translations)
                    {'moi {:fi "Moro!"
                           :sv "Hej!"}}))))
    (.delete file-with-empty-lines)))

(defn make-excel [& sheet-infos]
  (let [wb   (apply xls/create-workbook sheet-infos)
        file (File/createTempFile (str (gensym "testexcel")) ".xlsx")]
    (xls/save-workbook! (.getPath file) wb)
    file))

(deftest multiple-sheets
  (testing "OK"
    (let [file (make-excel "One" [["foo" "a" "b"]
                                  ["k1" "hello" "world"]
                                  ["k3" "road" "kill"]]
                           "Two" [["bar" "a" "b"]
                                  ["k2" "another" "time"]
                                  ["k3" "over" "ridden"]]
                           "eerhT" [[ "oppo" "b" "a"]
                                    [" k4 " "  Banana " " Acai "]])
          m    (resources/excel->map file)]
      (.delete file)
      (is (= m {:languages    [:a :b]
                :translations {'k1 {:a "hello" :b "world"}
                               'k2 {:a "another" :b "time"}
                               'k3 {:a "over" :b "ridden"}
                               'k4 {:a "Acai" :b "Banana"}}}))))
  (testing "Sheet mismmatch"
    (let [file (make-excel "One" [["foo" "a" "b"]
                                  ["k1" "hello" "world"]]
                           "Two" [["bar" "a" "c"]
                                  ["k2" "another" "time"]])]
      (is (thrown? AssertionError (resources/excel->map file)))
      (.delete file))))
