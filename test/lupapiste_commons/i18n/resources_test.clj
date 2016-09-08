(ns lupapiste-commons.i18n.resources-test
  (:require [lupapiste-commons.i18n.resources :refer :all]
            [clojure.test :refer :all])
  (:import [java.io File]))

(def translations
  {:languages [:fi :sv]
   :translations {'label {:fi "Hyvää huomenta" :sv "Gud morgon"}
                  '^:source-changed button {:fi "Ok" :sv "Jättebra"}}})

(defn- source-name-equals [source-name]
  (fn [key]
    (= (:source-name (meta key))
       source-name)))

(deftest roundtrip
  (testing "What is put into a pipe comes out of it staying the same, where pipe is: map -> txt -> map -> excel -> map"
    (let [txt-file (doto (File/createTempFile "translations" ".txt"))
          excel-file (doto (File/createTempFile "translations" ".xlsx"))]
      (let [_ (write-txt translations txt-file)
            from-txt (txt->map txt-file)
            _ (write-excel from-txt excel-file)
            from-excel (excel->map excel-file)]
        (is (= (every? (source-name-equals (.getName txt-file)) (keys from-txt))))
        (is (= (every? (source-name-equals (.getName excel-file)) (keys from-excel))))
        (.delete txt-file)
        (.delete excel-file)
        (is (= translations from-excel))))))

(deftest find-missing-translations
  (let [no-texts {:languages [:fi :sv]
                  :translations {'label {}}}
        empty-default-text {:languages [:fi :sv]
                            :translations {'label {:fi ""}}}
        empty-swedish-text {:languages [:fi :sv]
                            :translations {'label {:fi "Heippa" :sv ""}}}
        source-text-changed {:languages [:fi :sv]
                             :translations {'^:source-changed label {:fi "Punainen" :sv "Blå"}}}]
    (is (= (ffirst (:translations (missing-translations no-texts)))
           'label))
    (is (= (ffirst (:translations (missing-translations empty-default-text)))
           'label))
    (is (= (ffirst (:translations (missing-translations empty-swedish-text)))
           'label))
    (is (= (ffirst (:translations (missing-translations source-text-changed)))
           'label))))

(deftest missing-translations-with-lang
  (let [empty-english {:languages [:fi :sv :en]
                       :translations {'label {:fi "Heippa" :sv "Hej" :en ""}}}
        missing-english {:languages [:fi :sv]
                         :translations {'label {:fi "Heippa" :sv "Hej"}}}]
    (is (= (ffirst (:translations (missing-translations empty-english)))
           'label))
    (is (nil? (ffirst (:translations (missing-translations empty-english :sv)))) "English check is discarded")
    (is (= (ffirst (:translations (missing-translations empty-english :en)))
           'label))
    (is (= (ffirst (:translations (missing-translations missing-english :en)))
           'label))
    (is (= (:languages (missing-translations empty-english :en))
           [:fi :en]) "Fi and selected language returned in languages")))

(deftest write-key-test
  (let [loc-key (symbol "application/rtf")]
    (is (= (write-key loc-key) "application/rtf") "'Namespace' should not be stripped off")
    (is (= (write-key (with-meta loc-key {:source-changed true}))
           "application/rtf!")
        "When source is changed, '!' is added to key")))

(deftest empty-lines-test
  (let [file-with-empty-lines (File/createTempFile "translations" ".txt")]
    (write-txt translations file-with-empty-lines)
    (spit file-with-empty-lines
          (str  "\n\n"
                (#'lupapiste-commons.i18n.resources/txt-line "moi" "fi" "Moro!")
                (#'lupapiste-commons.i18n.resources/txt-line "moi" "sv" "Hej!"))
          :append true)
    (let [from-txt (txt->map file-with-empty-lines)]
      (is (= (:translations from-txt)
             (merge (:translations translations)
                    {'moi {:fi "Moro!"
                           :sv "Hej!"}}))))
    (.delete file-with-empty-lines)))
