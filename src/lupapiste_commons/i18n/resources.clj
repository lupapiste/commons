(ns lupapiste-commons.i18n.resources
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [dk.ative.docjure.spreadsheet :as xls]
            [flatland.ordered.map :refer [ordered-map]]
            [lupapiste-commons.i18n.txt-resources :as txt-resources])
  (:import [java.io FileOutputStream]
           [java.text SimpleDateFormat]
           [java.util Date]
           [org.apache.poi.xssf.usermodel XSSFCell XSSFSheet XSSFWorkbook]))

(set! *warn-on-reflection* true)

(defn make-row-strings [key languages strings]
  (->> (for [lang languages]
         (get strings lang))
       (map txt-resources/nil->empty-str)
       (cons (txt-resources/write-key key))))

(defn sheet->map [^XSSFSheet excel-sheet]
  (let [kw-or-nil #(when-not (s/blank? %)
                     (keyword %))
        read-row  #(map (comp s/trim str xls/read-cell)
                        (xls/cell-seq %))
        [x & xs]  (xls/row-seq excel-sheet)
        languages (->> (read-row x)
                       rest
                       (keep kw-or-nil ))
        rows      (keep  (fn [x]
                           (let [[r :as row ] (read-row x)]
                             (when-not (s/blank? r)
                               row)))
                         xs)]
    {:languages    languages
     :translations (->> rows
                        (map (fn [[k & vs]]
                               [(txt-resources/read-key k) (zipmap languages vs)]))
                        (into (ordered-map)))}))

(defn excel->map [excel-file]
  (let [workbook   (xls/load-workbook excel-file)
        sheet-maps (map sheet->map (xls/sheet-seq workbook))]
    (assert (or (= (count sheet-maps) 1)
                (->> sheet-maps (map (comp set :languages)) (apply =)))
            "Sheet languages mismatch.")
    {:languages    (-> sheet-maps first :languages)
     :translations (->> sheet-maps (map :translations) (apply merge))}))

(defn create-row [^XSSFSheet sheet row-strings row-index & {:keys [cell-fn] :or {cell-fn nil}}]
  (let [row (.createRow sheet ^long row-index)]
    (doall (map-indexed (fn [^long index ^String value]
                          (let [cell (.createCell row index)]
                            (when (fn? cell-fn)
                              (cell-fn cell))
                            (.setCellValue cell value)))
                        row-strings))))

(defn bold-font-cell [^XSSFWorkbook wb ^XSSFCell cell]
  (let [style (.createCellStyle wb)
        font (.createFont wb)]
    (.setBold font true)
    (.setFont style font)
    (.setCellStyle cell style)))

(defn write-excel [{:keys [languages translations]} excel-file]
  (let [wb (XSSFWorkbook.)
        sheet (.createSheet wb "translations")]
    (create-row sheet (concat ["key"] (map name languages)) 0
                :cell-fn (partial bold-font-cell wb))
    (doall (map-indexed (fn [index [key strings]]
                          (let [row-strings (make-row-strings key languages strings)]
                            (create-row sheet row-strings (inc index))))
                        translations))
    (doseq [column (range 3)]
      (.autoSizeColumn sheet column))
    (with-open [out (FileOutputStream. (io/file excel-file))]
      (.write wb out))))

(defn missing-localizations-excel
  "Writes missing shared localizations to excel file.
   If excel file is not provided, will create the file to user home dir."
  ([]
   (let [date-str (.format (SimpleDateFormat. "yyyyMMdd") (Date.))
         filename (str (System/getProperty "user.home")
                       "/lupapiste_commons_translations_"
                       date-str
                       ".xlsx")]
     (missing-localizations-excel (io/file filename))))
  ([file]
   (missing-localizations-excel file "resources/shared_translations.txt"))
  ([target-file translations-file]
   (write-excel
     (txt-resources/missing-translations (txt-resources/txt->map translations-file))
     target-file)))
