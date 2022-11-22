(ns lupapiste-commons.i18n.txt-resources
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [flatland.ordered.map :refer [ordered-map]])
  (:import [java.io File PushbackReader StringReader]
           [java.net URL]))

(set! *warn-on-reflection* true)

(defn source-changed? [sym]
  (:source-changed (meta sym)))

(defn write-key [sym]
  (cond-> (str sym)
    (source-changed? sym) (str "!")))

(defn nil->empty-str [x]
  (if (nil? x) "" x))

(defn read-key [^String string]
  (if (.endsWith string "!")
    (-> (s/replace string #"!$" "")
        symbol
        (with-meta {:source-changed true}))
    (symbol string)))

(defn- read-entry [line]
  (let [in (-> line StringReader. PushbackReader.)
        key (read-key (edn/read in))
        lang (edn/read in)
        text (edn/read in)]
    {:key key :lang lang :text text}))

(def ^:private empty-line? s/blank?)

(defn- filename [input]
  (cond (string? input) input
        (instance? File input) (.getName ^File input)
        (instance? URL input) (-> (.getFile ^URL input)
                                  (s/split #"/")
                                  last)
        :else nil))

(defn- merge-entry [acc {:keys [key lang text]} source-name]
  (let [[key current-translations] (or (find acc key)
                                       [key (ordered-map)])]
    (assoc acc
           (with-meta key
                      (merge (meta key)
                             {:source-name source-name}))
           (assoc current-translations
                  (keyword lang)
                  text))))

(defn txt->map [input]
  (with-open [reader (io/reader input)]
    (let [source-name (filename input)
          translations
                      (loop [acc (ordered-map)
                             lines (line-seq reader)]
                        (if-let [line (first lines)]
                          (if (empty-line? line)
                            (recur acc
                                   (rest lines))
                            (recur (merge-entry acc
                                                (read-entry line)
                                                source-name)
                                   (rest lines)))
                          acc))]
      {:languages (-> translations first val keys vec)
       :translations translations})))

(defn- txt-line [key lang text]
  (str (pr-str (write-key key)) " " (pr-str (name lang)) " " (pr-str (nil->empty-str text)) "\n"))

(defn write-txt [{:keys [translations]} txt-file]
  (spit txt-file "") ;; truncate txt-file
  (doseq [[key strings] translations]
    (doseq [[lang text] strings]
      (spit txt-file (txt-line key lang text) :append true))))

(defn missing-translations
  "Returns missing translations. If selected-lang is given, only that and :fi is regarded."
  [{:keys [translations languages]} & [selected-lang]]
  {:languages (if selected-lang
                [:fi (keyword selected-lang)]
                languages)
   :translations
   (reduce (fn [acc [key {:keys [fi] :as strings}]]
             (cond-> acc
               (empty? (dissoc strings :fi))               (assoc key {:fi fi})
               (empty? fi)                                 (assoc key {:fi ""})
               (source-changed? key)                       (assoc key {:fi fi})
               (if selected-lang
                 (s/blank? (get strings (keyword selected-lang)))
                 (some empty? (vals strings)))             (assoc key {:fi fi})))
           (ordered-map)
           translations)})
