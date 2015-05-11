(ns lupapiste-commons.i18n
  (:require [clojure.java.io :as io]
            [lupapiste-commons.i18n-resources :as resources]))

(defn read-translations []
  (resources/txt->map (io/resource "translations.txt")))

(defn keys-by-language [translations]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]] (assoc-in acc [lang key] string))
                    acc
                    langs))
          {}
          translations))
