(ns lupapiste-commons.i18n
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn read-translations []
  (edn/read-string (slurp (io/resource "i18n.edn"))))

(defn keys-by-language [translations]
  (reduce (fn [acc [key langs]]
            (reduce (fn [acc [lang string]] (assoc-in acc [lang key] string))
                    acc
                    langs))
          {}
          (apply merge (vals translations))))
