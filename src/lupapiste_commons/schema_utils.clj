(ns lupapiste-commons.schema-utils
  (:require [schema.core :as s])
  (:import (schema.core EnumSchema)
           (java.text SimpleDateFormat ParseException)))

(defn- get-in-metadata-map [map ks]
  (let [k (first ks)
        value (get map k (get map (s/optional-key k)))]
    (if (and (map? value) (next ks))
      (get-in-metadata-map value (next ks))
      value)))

(defn parse-iso-8601-date [date-str]
  (let [format (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX")
        format2 (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")]
    (try (.parse format date-str)
         (catch ParseException _
           (.parse format2 date-str)))))

(defn- parse-string-value [schema v]
  (cond
    (not (string? v)) v
    (= EnumSchema (type schema)) (keyword v)
    (= s/Int schema) (Integer/parseInt v)
    (= s/Num schema) (Double/parseDouble v)
    (= s/Inst schema) (parse-iso-8601-date v)
    :else v))

(defn- convert-value-to-schema-type [schema-map ks v]
  (if-let [schema (get-in-metadata-map schema-map ks)]
    (if (sequential? schema)
      (map #(parse-string-value (first schema) %) v)
      (parse-string-value schema v))
    v))

(defn coerce-metadata-to-schema
  ([schema-map m]
   (coerce-metadata-to-schema schema-map m []))
  ([schema-map m ks]
   (->> m
        (map (fn [[k v]] (let [new-k (keyword k)
                               new-ks (conj ks new-k)
                               new-v (if (map? v)
                                       (coerce-metadata-to-schema schema-map v new-ks)
                                       (convert-value-to-schema-type schema-map new-ks v))]
                           [new-k new-v])))
        (into {}))))
