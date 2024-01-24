(ns lupapiste-commons.schema-utils
  (:require [schema.core :as s])
  (:import (schema.core EnumSchema)
           (java.text SimpleDateFormat ParseException)))

(set! *warn-on-reflection* true)

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

(defn- coerce-to-enum-type [schema v]
  (let [enum-val-type (-> schema vals ffirst)]
    (cond
      (and (keyword? enum-val-type) (or (string? v) (keyword? v))) (keyword v)
      (and (string? enum-val-type) (keyword? v)) (name v)
      (string? enum-val-type) (str v)
      (nil? v) v
      :else
      (do (throw (Exception. (str "Coercion from " (type v) " to " (type enum-val-type) " is not supported.")))))))

(defn- parse-value [schema v]
  (cond
    (= schema.core.One (type schema)) (parse-value (:schema schema) v)
    (= EnumSchema (type schema)) (coerce-to-enum-type schema v)
    (and (= s/Str schema) (keyword? v)) (name v)
    (and (= s/Keyword schema) (string? v)) (keyword v)
    (not (string? v)) v
    (= s/Int schema) (Integer/parseInt v)
    (= s/Num schema) (Double/parseDouble v)
    (= s/Inst schema) (parse-iso-8601-date v)
    :else v))

(defn- convert-value-to-schema-type [schema-map ks v]
  (if-let [schema (get-in-metadata-map schema-map ks)]
    (if (sequential? schema)
      (map #(parse-value (first schema) %) v)
      (parse-value schema v))
    v))

(defn coerce-metadata-to-schema
  ([schema-map m]
   (coerce-metadata-to-schema schema-map m []))
  ([schema-map-or-constrained m ks]
   (let [schema-map (if (= schema.core.Constrained (type schema-map-or-constrained))
                      (second (first schema-map-or-constrained))
                      schema-map-or-constrained)]
     (->> m
          (map (fn [[k v]] (let [new-k (keyword k)
                                 new-ks (conj ks new-k)
                                 new-v (if (map? v)
                                         (coerce-metadata-to-schema schema-map v new-ks)
                                         (convert-value-to-schema-type schema-map new-ks v))]
                             [new-k new-v])))
          (into {})))))

(defn- remove-empty-value [value]
  (if (sequential? value)
    (->> (map remove-empty-value value)
         (remove nil?))
    (when-not (and (or (map? value) (string? value)) (empty? value))
      value)))

(defn remove-blank-keys [metadata]
  (reduce
    (fn [acc [k v]]
      (let [value (cond->> v
                           (map? v) remove-blank-keys
                           true remove-empty-value)]
        (if-not (nil? value)
          (assoc acc k value)
          acc)))
    {}
    metadata))
