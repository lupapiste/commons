(ns lupapiste-commons.tos-metadata-schema
  (:require [schema.core :as s :include-macros true]))

(def Vuodet (s/both s/Int (s/pred #(>= % 0) 'equal-or-greater-than-zero)))

(def Salassapitoaika {:type :salassapitoaika
                      :schema Vuodet})

(def Salassapitoperuste {:type :salassapitoperuste
                         :schema s/Str})

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
                   :subfields [{:type :arkistointi :values arkistointi}
                               {:type :perustelu :schema s/Str}
                               {:type :laskentaperuste :values laskentaperuste}
                               {:type :pituus :schema Vuodet}]})

(def Henkilötiedot
  {:type :henkilotiedot
   :values [:ei-sisalla :sisaltaa :sisaltaa-arkaluonteisia]})

(def Tila {:type :tila
           :schema s/Str})

(defn sailytysaika-schema-map []
  (->> (:subfields SailytysAika)
       (map (fn [dep]
              [(:type dep) (if (:schema dep) (:schema dep) (apply s/enum (:values dep)))]))
       (into {})))

(def MetaDataMap
  {:julkisuusluokka (apply s/enum (:values Julkisuusluokka))
   (s/optional-key :salassapitoaika) Vuodet
   (s/optional-key :salassapitoperuste) s/Str
   (s/optional-key :turvallisuusluokka) (apply s/enum (:values Turvallisuusluokka))
   (s/optional-key :suojaustaso) (apply s/enum (:values Suojaustaso))
   :sailytysaika (sailytysaika-schema-map)
   :henkilotiedot (apply s/enum (:values Henkilötiedot))})

(def AsiakirjaMetaDataMap
  (merge MetaDataMap {:tila s/Str}))

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
                        (not= (:arkistointi sailytysaika) :määräajan) (assoc-in [:sailytysaika :pituus] (get-in default-metadata [:sailytysaika :pituus]))
                        (= julkisuusluokka :julkinen)                 (dissoc :salassapitoaika :salassapitoperuste :turvallisuusluokka)))))

(def common-metadata-fields
  [Julkisuusluokka Suojaustaso Henkilötiedot SailytysAika])
