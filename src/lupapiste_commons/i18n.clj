(ns lupapiste-commons.i18n
  (:require [clojure.java.io :as io]
            [lupapiste-commons.i18n-resources :as resources]))

(defn read-translations []
  (resources/txt->map (io/resource "translations.txt")))

(defn keys-by-language [{:keys [translations]} & {:keys [str-keys]
                                           :or {str-keys true}}]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]]
                      (assoc-in acc [lang (if str-keys (str key) key)] string))
                    acc
                    langs))
          {}
          translations))
