(ns lupapiste-commons.i18n.resources
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [flatland.ordered.map :refer [ordered-map]]
            [ontodev.excel :as xls]
            [clojure.edn :as edn])
  (:import [org.apache.poi.xssf.usermodel XSSFWorkbook]
           [org.apache.poi.ss.usermodel Font]
           [java.io PushbackReader StringReader]
           [java.util Date]
           [java.text SimpleDateFormat]))

(defn source-changed? [sym]
  (:source-changed (meta sym)))

(defn write-key [sym]
  (cond-> (str sym)
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

(defn sheet->map [^org.apache.poi.xssf.usermodel.XSSFSheet excel-sheet]
  (let [languages (->> excel-sheet
                       first
                       xls/read-row
                       rest
                       (map (comp keyword s/trim)))
        rows (->> (map xls/read-row excel-sheet)
                  rest
                  (map #(map s/trim %)))]
    {:languages (vec languages)
     :translations (reduce (fn [acc [key & values]]
                             (assoc acc (read-key key) (zipmap languages values)))
                           (ordered-map)
                           (filter #(not (empty? (first %))) rows))}))

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

(defn write-txt [{:keys [translations]} txt-file]
  (spit txt-file "") ;; truncate txt-file
  (doseq [[key strings] translations]
    (doseq [[lang text] strings]
      (spit txt-file (str (pr-str (write-key key)) " " (pr-str (name lang)) " " (pr-str (nil->empty-str text)) "\n") :append true))))

(defn txt->map [input]
  (with-open [reader (io/reader input)]
    (let [translations (loop [acc (ordered-map)
                              lines (line-seq reader)]
                         (if-let [line (first lines)]
                           (let [in (-> line StringReader. PushbackReader.)
                                 key (read-key (edn/read in))
                                 lang (edn/read in)
                                 text (edn/read in)]
                             (recur (assoc acc key (assoc (get acc key (ordered-map)) (keyword lang) text)) (rest lines)))
                           acc))]
      {:languages (-> translations first val keys vec)
       :translations translations})))

(defn create-row [sheet row-strings row-index & {:keys [cell-fn] :or {cell-fn nil}}]
  (let [row (.createRow sheet row-index)]
    (doall (map-indexed (fn [index value]
                          (let [cell (.createCell row index)]
                            (when (fn? cell-fn)
                              (cell-fn cell))
                            (.setCellValue cell value)))
                        row-strings))))

(defn bold-font-cell [wb cell]
  (let [style (.createCellStyle wb)
        font (.createFont wb)]
    (.setBoldweight font Font/BOLDWEIGHT_BOLD)
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
    (with-open [out (java.io.FileOutputStream. excel-file)]
      (.write wb out))))

(defn missing-translations [{:keys [translations languages]}]
  {:languages
   languages
   :translations
   (reduce (fn [acc [key {:keys [fi] :as strings}]]
             (cond-> acc
               (empty? (dissoc strings :fi)) (assoc key {:fi fi})
               (empty? fi)                   (assoc key {:fi ""})
               (source-changed? key)         (assoc key {:fi fi})
               (some empty? (vals strings))  (assoc key {:fi fi})))
           (ordered-map)
           translations)})

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
     (missing-translations (txt->map translations-file))
     target-file)))
