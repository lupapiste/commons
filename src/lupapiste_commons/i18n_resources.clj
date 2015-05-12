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

(defn read-key [s]
  (if (.endsWith s "!")
    (vary-meta (symbol (s/replace s #"!$" "")) assoc :source-changed true)
    (symbol s)))

(defn excel->map [excel-file]
  (with-open [in (io/input-stream excel-file)]
    (let [workbook (xls/load-workbook in)
          sheets (seq workbook)
          header (->> sheets
                      ffirst
                      xls/read-row
                      rest
                      (map (comp keyword s/trim)))
          rows (->> sheets
                    (map (fn [x] (map xls/read-row x)))
                    (apply concat)
                    rest
                    (map #(map s/trim %)))]
      (reduce (fn [acc [key & values]]
                (assoc acc (read-key key) (zipmap header values)))
              (ordered-map)
              (filter #(not (empty? (first %))) rows)))))

(defn map->txt [data txt-file]
  (let [header (->> ["key" "fi" "sv"]
                    (map pr-str)
                    (s/join " "))]
    (spit txt-file (str header "\n"))
    (doseq [[key {:keys [fi sv]}] data]
      (spit txt-file (str (s/join " " (map pr-str [(write-key key) fi sv])) "\n") :append :true))))

(defn txt->map [file]
  (with-open [reader (io/reader file)]
    (let [lines (line-seq reader)
          langs (-> lines
                    first
                    (s/split #" ")
                    rest
                    (->> (map (comp keyword edn/read-string))))]
      (reduce (fn [acc line]
                (with-open [in (-> line StringReader. PushbackReader.)]
                  (let [[key & translations] (take-while #(not= % :eof)
                                                         (repeatedly #(edn/read {:eof :eof} in)))]
                    (assoc acc (read-key key) (zipmap langs translations)))))
              (ordered-map)
              (line-seq reader)))))

(defn create-row [sheet row-data row-index]
  (let [header-row (.createRow sheet row-index)]
    (doall (map-indexed (fn [index header]
                          (let [cell (.createCell header-row index)]
                            (.setCellValue cell header)))
                        row-data))))

(defn nil->empty-str [x]
  (if (nil? x) "" x))

(defn write-excel [data excel-file]
  (let [wb (XSSFWorkbook.)
        sheet (.createSheet wb "translations")]
    (create-row sheet ["key" "fi" "sv"] 0)
    (doall (map-indexed (fn [index [key {:keys [fi sv]}]]
                          (create-row sheet [(write-key key) (nil->empty-str fi) (nil->empty-str sv)] (inc index)))
                        data))
    (doseq [column (range 3)]
      (.autoSizeColumn sheet column))
    (with-open [out (java.io.FileOutputStream. excel-file)]
      (.write wb out))))

(defn missing-translations [data]
  (reduce (fn [acc [key {:keys [fi] :as strings}]]
            (cond-> acc
              (empty? (dissoc strings :fi)) (assoc key fi)
              (empty? fi)                   (assoc key "")
              (source-changed? key)         (assoc key fi)
              (some empty? (vals strings))  (assoc key fi)))
          (ordered-map)
          data))
