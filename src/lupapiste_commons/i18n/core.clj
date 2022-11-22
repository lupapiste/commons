(ns lupapiste-commons.i18n.core
  (:require [clojure.java.io :as io]
            [clojure.set :refer [union]]
            [flatland.ordered.map :refer [ordered-map]]
            [lupapiste-commons.i18n.txt-resources :as txt-resources]))

(def default-lang :fi)

(defn missing-translations? [strings default-lang]
  (seq (filter (fn [[_ v]] (empty? v))
               (dissoc strings default-lang))))

(defn replace-missing [strings default-lang]
  (into {} (for [[k v] strings]
             [k (if (empty? v) (get strings default-lang) v)])))

(defn replace-missing-texts [translations default-lang]
  (reduce (fn [acc [key strings]]
            (assoc acc key (replace-missing strings default-lang)))
          (ordered-map)
          translations))

(defn read-translations
  ([] (read-translations (io/resource "translations.txt")))
  ([input & {:keys [fallback-to-default-lang]}]
   (if fallback-to-default-lang
     (update-in (txt-resources/txt->map input) [:translations] replace-missing-texts default-lang)
     (txt-resources/txt->map input))))

(defn combine-vec-or-map [left right]
  (if (every? vector? [left right])
    (vec (distinct (union left right)))
    (conj left right)))

(defn merge-translations [& translation-maps]
  (apply merge-with combine-vec-or-map translation-maps))

(defn keys-by-language [{:keys [translations]} & {:keys [str-keys] :or {str-keys true}}]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]]
                      (assoc-in acc [lang (if str-keys (str key) key)] string))
                    acc
                    langs))
          {}
          translations))
