(ns lupapiste-commons.i18n
  (:require [clojure.java.io :as io]
            [flatland.ordered.map :refer [ordered-map]]
            [lupapiste-commons.i18n-resources :as resources]))

(def default-lang :fi)

(defn missing-translations? [strings]
  (seq (filter (fn [[_ v]] (empty? v))
               (dissoc strings default-lang))))

(defn replace-with-default-lang [strings]
  (into {} (for [[k v] strings]
             [k (if (empty? v) (get strings default-lang) v)])))

(defn read-translations
  ([] (read-translations (io/resource "translations.txt")))
  ([input]
   (let [{:keys [translations languages]} (resources/txt->map input)]
     (reduce (fn [acc [key strings]]
               (assoc acc
                      key
                      (if (missing-translations? strings)
                        (replace-with-default-lang strings)
                        strings)))
             (ordered-map)
             translations))))

(defn keys-by-language [{:keys [translations]} & {:keys [str-keys]
                                           :or {str-keys true}}]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]]
                      (assoc-in acc [lang (if str-keys (str key) key)] string))
                    acc
                    langs))
          {}
          translations))
