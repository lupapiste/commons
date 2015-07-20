(ns lupapiste-commons.tos-metadata-schema
  (:require [schema.core :as s :include-macros true]
            [clojure.string :as string]
            [lupapiste-commons.shared-utils :refer [dissoc-in]]))

(def Vuodet (s/both s/Int (s/pred #(>= % 0) 'equal-or-greater-than-zero)))

(def NonEmptyStr (s/both s/Str (s/pred #(not (string/blank? %)) 'non-empty-string)))

(def Salassapitoaika {:type :salassapitoaika
                      :schema Vuodet})

(def Salassapitoperuste {:type :salassapitoperuste
                         :schema NonEmptyStr})

(def Turvallisuusluokka {:type :turvallisuusluokka
                         :values [:turvallisuusluokka1
                                  :turvallisuusluokka2
                                  :turvallisuusluokka3
                                  :turvallisuusluokka4
                                  :ei-turvallisuusluokkaluokiteltu]})

(def Suojaustaso {:type :suojaustaso
                  :values [:suojaustaso1 :suojaustaso2 :suojaustaso3 :suojaustaso4]})

(def Julkisuusluokka {:type :julkisuusluokka
                      :values [:julkinen :osittain-salassapidettava :salainen]
                      :dependencies {:osittain-salassapidettava [Salassapitoaika Salassapitoperuste Turvallisuusluokka]
                                     :salainen [Salassapitoaika Salassapitoperuste Turvallisuusluokka]}})

(def arkistointi [:ei :ikuisesti :määräajan :toistaiseksi])
(def laskentaperuste [:lupapäätöspäivä
                      :päätöksen_lainvoimaisuuspäivä
                      :rakennuksen_purkamispäivä
                      :vakuuksien_voimassaoloaika])

(def SailytysAika {:type :sailytysaika
                   :subfields [{:type :arkistointi
                                :values arkistointi
                                :dependencies {:määräajan [{:type :pituus :schema Vuodet}]
                                               :toistaiseksi [{:type :laskentaperuste :values laskentaperuste}]}}
                               {:type :perustelu :schema NonEmptyStr}]})

(def Henkilötiedot
  {:type :henkilotiedot
   :values [:ei-sisalla :sisaltaa :sisaltaa-arkaluonteisia]})

(def Tila {:type :tila
           :schema NonEmptyStr})

(defn attr-map->schema-pair [attr-map]
  (if-let [schema (cond
                    (:schema attr-map) (:schema attr-map)
                    (:values attr-map) (apply s/enum (:values attr-map)))]
    {(:type attr-map) schema}
    attr-map))

(defn ui-desc->schema-map [desc-map]
  (cond-> desc-map
    (:dependencies desc-map) (merge (->> (:dependencies desc-map)
                                         (map #(->> (val %)
                                                    (map (fn [v] (->> (ui-desc->schema-map v)
                                                                      (map (fn [[k v]] [(s/optional-key k) v]))
                                                                      (into {}))))
                                                    (into {})))
                                         (apply merge)))
    (:subfields desc-map) (merge (->> (:subfields desc-map)
                                      (map (fn [dep] (ui-desc->schema-map dep)))
                                      (into {})
                                      (hash-map (:type desc-map))))
    (or (:schema desc-map) (:values desc-map)) (merge (attr-map->schema-pair desc-map))
    true (dissoc :type :schema :values :subfields :dependencies)))

(def MetaDataMap
  {:julkisuusluokka (apply s/enum (:values Julkisuusluokka))
   (s/optional-key :salassapitoaika) (:schema Salassapitoaika)
   (s/optional-key :salassapitoperuste) (:schema Salassapitoperuste)
   (s/optional-key :turvallisuusluokka) (apply s/enum (:values Turvallisuusluokka))
   (s/optional-key :suojaustaso) (apply s/enum (:values Suojaustaso))
   :sailytysaika (:sailytysaika (ui-desc->schema-map SailytysAika))
   :henkilotiedot (apply s/enum (:values Henkilötiedot))})

(def AsiakirjaMetaDataMap
  (merge MetaDataMap {:tila (:schema Tila)}))

(def default-metadata
  {:julkisuusluokka :julkinen
   :sailytysaika {:arkistointi :ei
                  :perustelu ""
                  :laskentaperuste (first laskentaperuste)
                  :pituus 0}
   :henkilotiedot :ei-sisalla
   :suojaustaso :suojaustaso1})

(def asiakirja-default-metadata
  (merge default-metadata {:tila "Luonnos"}))

(defn sanitize-metadata [{:keys [sailytysaika julkisuusluokka] :as metadata}]
  (let [schema (if (:tila metadata) AsiakirjaMetaDataMap MetaDataMap)]
    (s/validate schema
                (cond-> metadata
                  (not= (:arkistointi sailytysaika) :määräajan)    (dissoc-in [:sailytysaika :pituus])
                  (not= (:arkistointi sailytysaika) :toistaiseksi) (dissoc-in [:sailytysaika :laskentaperuste])
                  (= julkisuusluokka :julkinen)                    (dissoc :salassapitoaika :salassapitoperuste :turvallisuusluokka)))))

(def common-metadata-fields
  [Julkisuusluokka Suojaustaso Henkilötiedot SailytysAika])
