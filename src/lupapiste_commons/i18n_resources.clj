(ns lupapiste-commons.i18n-resources
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [flatland.ordered.map :refer [ordered-map]]
            [ontodev.excel :as xls]
            [clojure.edn :as edn])
  (:import [org.apache.poi.xssf.usermodel XSSFWorkbook]
           [java.io PushbackReader StringReader]))

(defn source-changed? [sym]
  (:source-changed (meta sym)))

(defn write-key [sym]
  (cond-> (name sym)
    (source-changed? sym) (str "!")))

(defn read-key [string]
  (if (.endsWith string "!")
    (-> (s/replace string #"!$" "")
        symbol
        (with-meta {:source-changed true}))
    (symbol string)))

(defn nil->empty-str [x]
  (if (nil? x) "" x))

(defn make-row-strings [key languages strings]
  (->> (for [lang languages]
         (get strings lang))
       (map nil->empty-str)
       (cons (write-key key))))

(defn excel->map [excel-file]
  (with-open [in (io/input-stream excel-file)]
    (let [workbook (xls/load-workbook in)
          sheets (seq workbook)
          languages (->> sheets
                         ffirst
                         xls/read-row
                         rest
                         (map (comp keyword s/trim)))
          rows (->> sheets
                    (map (fn [x] (map xls/read-row x)))
                    (apply concat)
                    rest
                    (map #(map s/trim %)))]
      {:languages (vec languages)
       :translations (reduce (fn [acc [key & values]]
                               (assoc acc (read-key key) (zipmap languages values)))
                             (ordered-map)
                             (filter #(not (empty? (first %))) rows))})))

(defn map->lines [{:keys [languages translations]}]
  (let [header (->> languages
                    (map name)
                    (cons "key")
                    (map pr-str)
                    (s/join " "))]
    (list* (str header "\n")
           (for [[key strings] translations]
             (->> (make-row-strings key languages strings)
                  (map pr-str)
                  (s/join " ")
                  (#(str % "\n")))))))

(defn write-lines [lines txt-file]
  (spit txt-file (first lines))
  (doseq [line (rest lines)]
    (spit txt-file line :append :true)))

(defn write-txt [data txt-file]
  (write-lines (map->lines data) txt-file))

(defn txt->map [input]
  (with-open [reader (io/reader input)]
    (let [lines (line-seq reader)
          languages (-> lines
                        first
                        (s/split #" ")
                        rest
                        (->> (map (comp keyword edn/read-string))))]
      {:languages (vec languages)
       :translations (reduce (fn [acc line]
                               (with-open [in (-> line StringReader. PushbackReader.)]
                                 (let [[key & translations] (take-while #(not= % :eof)
                                                                        (repeatedly #(edn/read {:eof :eof} in)))]
                                   (assoc acc (read-key key) (zipmap languages translations)))))
                             (ordered-map)
                             (rest lines))})))

(defn create-row [sheet row-strings row-index]
  (let [row (.createRow sheet row-index)]
    (doall (map-indexed (fn [index value]
                          (let [cell (.createCell row index)]
                            (.setCellValue cell value)))
                        row-strings))))

(defn write-excel [{:keys [languages translations]} excel-file]
  (let [wb (XSSFWorkbook.)
        sheet (.createSheet wb "translations")]
    (create-row sheet (concat ["key"] (map name languages)) 0)
    (doall (map-indexed (fn [index [key strings]]
                          (let [row-strings (make-row-strings key languages strings)]
                            (create-row sheet row-strings (inc index))))
                        translations))
    (doseq [column (range 3)]
      (.autoSizeColumn sheet column))
    (with-open [out (java.io.FileOutputStream. excel-file)]
      (.write wb out))))

(defn missing-translations [{:keys [translations]}]
  (reduce (fn [acc [key {:keys [fi] :as strings}]]
            (cond-> acc
              (empty? (dissoc strings :fi)) (assoc key fi)
              (empty? fi)                   (assoc key "")
              (source-changed? key)         (assoc key fi)
              (some empty? (vals strings))  (assoc key fi)))
          (ordered-map)
          translations))
