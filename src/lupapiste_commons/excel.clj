(ns lupapiste-commons.excel
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [flatland.ordered.map :refer [ordered-map]]
            [ontodev.excel :as xls]
            [clojure.edn :as edn])
  (:import [org.apache.poi.xssf.usermodel XSSFWorkbook]))

(defn read-edn [edn-file]
  (edn/read-string (slurp edn-file)))

(defn write-edn [form edn-file]
  (binding [*print-length* nil]
    (spit edn-file (with-out-str (clojure.pprint/pprint form)))))

(defn sheet->map [sheet]
  (let [header (map keyword (rest (xls/read-row (first sheet))))
        rows (rest (map xls/read-row sheet))]
    (reduce (fn [acc row]
              (assoc acc (s/trim (first row)) (zipmap header (map s/trim (rest row)))))
            (ordered-map)
            (filter #(not (empty? (first %))) rows))))

(defn excel->map [excel-file]
  (with-open [in (io/input-stream excel-file)]
    (let [workbook (xls/load-workbook in)
          sheets (seq workbook)]
      (reduce (fn [acc sheet]
                (let [header (xls/read-row (first sheet))
                      rows (rest (map xls/read-row sheet))]
                  (assoc acc
                         (.getSheetName sheet)
                         (sheet->map sheet))))
              (ordered-map)
              sheets))))

(defn excel->edn [excel-file edn-file]
  (write-edn (excel->map excel-file) edn-file))

(defn create-row [sheet row-data row-index]
  (let [header-row (.createRow sheet row-index)]
    (doall (map-indexed (fn [index header]
                          (let [cell (.createCell header-row index)]
                            (.setCellValue cell header)))
                        row-data))))

(defn nil->empty-str [x]
  (if (nil? x) "" x))

(defn write-excel [data excel-file]
  (let [wb (XSSFWorkbook.)]
    (doseq [[group strings] data]
      (let [sheet (.createSheet wb group)]
        (create-row sheet ["key" "fi" "sv"] 0)
        (doall (map-indexed (fn [index [key {:keys [fi sv]}]]
                              (create-row sheet [key (nil->empty-str fi) (nil->empty-str sv)] (inc index)))
                            strings))))
    (with-open [out (java.io.FileOutputStream. excel-file)]
      (.write wb out))))

(defn add-keys-with-missing-translations [acc [key translations]]
  (if (some empty? (vals translations))
    (assoc acc key translations)
    acc))

(defn missing-translations [data]
  (reduce (fn [acc [group strings]]
            (let [missing (reduce add-keys-with-missing-translations {} strings)]
              (if (empty? missing)
                acc
                (assoc acc group missing))))
          {}
          data))
