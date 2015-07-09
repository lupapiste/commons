(ns lupapiste-commons.i18n.extract
  (:require [clojure.java.io :as io]
            [clojure.set :refer [difference]]
            [clojure.string :as s]
            [flatland.ordered.map :refer [ordered-map]]
            [lupapiste-commons.i18n.resources :as commons-resources]
            [lupapiste-commons.i18n.core :as commons-core])
  (:import [java.io PushbackReader]))

(defn simple-translation-call? [tr-sym form]
  (and (list? form)
       (= tr-sym (first form))
       (string? (second form))))

(defn strings-from [file tr-sym]
  (binding [*read-eval* false
            *data-readers* (assoc *data-readers* 'js identity)]
    (with-open [in (-> (io/reader file) PushbackReader.)]
      (map second (filter (partial simple-translation-call? tr-sym)
                          (tree-seq sequential? identity
                                    (doall (take-while #(not= % :eof)
                                                       (repeatedly #(read {:eof :eof :read-cond :allow} in))))))))))

(defn extract-strings [translation-function-name]
  (let [tr-sym (symbol translation-function-name)
        translations-file "resources/translations.txt"
        {:keys [translations languages] :as my-translations} (commons-resources/txt->map translations-file)
        all-translations (commons-core/merge-translations (commons-resources/txt->map (io/resource "shared_translations.txt"))
                                                          my-translations)
        current-keys (map symbol (distinct (mapcat (fn [file] (strings-from file tr-sym))
                                                   (filter #(re-find #"\.clj.$" (.getName %))
                                                           (file-seq (io/file "src"))))))
        missing-keys (difference (set current-keys) (set (keys (:translations all-translations))))]
    (println "Adding following keys to:" translations-file)
    (doseq [k missing-keys]
      (println (str (pr-str k))))
    (commons-resources/write-txt {:languages languages
                                  :translations (loop [acc translations
                                                       rest-keys missing-keys]
                                                  (if-let [k (first rest-keys)]
                                                    (recur (assoc acc k (assoc (get acc k (ordered-map)) :fi (name k) :sv "")) (rest rest-keys))
                                                    acc))} translations-file)
    (println "Done")))
