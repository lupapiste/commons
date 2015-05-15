(ns lupapiste-commons.i18n
  (:require [clojure.java.io :as io]
            [flatland.ordered.map :refer [ordered-map]]
            [lupapiste-commons.i18n-resources :as resources]))

(def default-lang :fi)

(defn missing-translations? [strings default-lang]
  (seq (filter (fn [[_ v]] (empty? v))
               (dissoc strings default-lang))))

(defn replace-missing [strings default-lang]
  (into {} (for [[k v] strings]
             [k (if (empty? v) (get strings default-lang) v)])))

(defn replace-missing-texts [translations default-lang]
  (reduce (fn [acc [key strings]]
            (assoc acc key (if (missing-translations? strings default-lang)
                             (replace-missing strings default-lang)
                             strings)))
          (ordered-map)
          translations))

(defn read-translations
  ([] (read-translations (io/resource "translations.txt")))
  ([input]
   (update-in (resources/txt->map input) [:translations] replace-missing-texts default-lang)))

(defn keys-by-language [{:keys [translations]} & {:keys [str-keys] :or {str-keys true}}]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]]
                      (assoc-in acc [lang (if str-keys (str key) key)] string))
                    acc
                    langs))
          {}
          translations))
