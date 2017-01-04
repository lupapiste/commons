(ns lupapiste-commons.tos-metadata-schema
  (:require [clojure.string :as string]
            [lupapiste-commons.shared-utils :refer [dissoc-in]]
            [schema.core :as s]))

(def Vuodet (s/constrained s/Int #(>= % 0) 'equal-or-greater-than-zero))

(def NonEmptyStr (s/constrained s/Str #(not (string/blank? %)) 'non-empty-string))

(def Salassapitoaika {:type :salassapitoaika
                      :schema Vuodet})

(def Salassapitoperuste {:type :salassapitoperuste
                         :schema NonEmptyStr})

(def Turvallisuusluokka {:type :turvallisuusluokka
                         :values [:ei-turvallisuusluokkaluokiteltu
                                  :turvallisuusluokka4
                                  :turvallisuusluokka3
                                  :turvallisuusluokka2
                                  :turvallisuusluokka1]})

(def Suojaustaso {:type :suojaustaso
                  :values [:ei-luokiteltu :suojaustaso4 :suojaustaso3 :suojaustaso2 :suojaustaso1]})

(def Kayttajaryhma {:type :kayttajaryhma
                    :values [:viranomaisryhma :lausunnonantajaryhma]})

(def Kayttajaryhmakuvaus {:type :kayttajaryhmakuvaus
                          :values [:muokkausoikeus :lukuoikeus]})

(def SalassapidonPaattymisajankohta {:type :security-period-end
                                     :schema s/Inst
                                     :calculated true})

(def Salassapitotiedot [Salassapitoaika SalassapidonPaattymisajankohta Salassapitoperuste Suojaustaso Turvallisuusluokka Kayttajaryhma Kayttajaryhmakuvaus])

(def Julkisuusluokka {:type :julkisuusluokka
                      :values [:julkinen :osittain-salassapidettava :salainen]
                      :dependencies {:osittain-salassapidettava Salassapitotiedot
                                     :salainen Salassapitotiedot}})

(def arkistointi [:ei :ikuisesti :määräajan :toistaiseksi])
(def laskentaperuste [:rakennuksen_purkamispäivä
                      :vakuuksien_voimassaoloaika])

(def sailytysaika-perustelu-suggestions
  [:custom-justification :al-17413 :al-11665 :kuntaliitto-14a :kuntaliitto-14b :kuntaliitto-1-s1 :oma-tarve])

(def SailytysAika {:type :sailytysaika
                   :require-role :archivist
                   :subfields [{:type :arkistointi
                                :values arkistointi
                                :dependencies {:määräajan [{:type :pituus :schema Vuodet}
                                                           {:type :retention-period-end :schema s/Inst :calculated true}]
                                               :toistaiseksi [{:type :laskentaperuste :values laskentaperuste}]}}
                               {:type :perustelu :schema NonEmptyStr}]})

(def Henkilotiedot
  {:type :henkilotiedot
   :values [:ei-sisalla :sisaltaa :sisaltaa-arkaluonteisia]})

(def Tila {:type :tila
           :values [:luonnos :valmis :arkistoidaan :arkistoitu]})

(def Myyntipalvelu {:type :myyntipalvelu
                    :schema s/Bool})

(def Nakyvyys {:type :nakyvyys
               :values [:julkinen :viranomainen :asiakas-ja-viranomainen]})

(def Kieli {:type :kieli
            :values [:fi :sv :en]})

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
    true (dissoc :type :schema :values :subfields :dependencies :require-role :calculated)))

(def MetaDataMap
  {:julkisuusluokka (apply s/enum (:values Julkisuusluokka))
   (s/optional-key :salassapitoaika) (:schema Salassapitoaika)
   (s/optional-key :security-period-end) (:schema SalassapidonPaattymisajankohta)
   (s/optional-key :salassapitoperuste) (:schema Salassapitoperuste)
   (s/optional-key :turvallisuusluokka) (apply s/enum (:values Turvallisuusluokka))
   (s/optional-key :suojaustaso) (apply s/enum (:values Suojaustaso))
   (s/optional-key :kayttajaryhma) (apply s/enum (:values Kayttajaryhma))
   (s/optional-key :kayttajaryhmakuvaus) (apply s/enum (:values Kayttajaryhmakuvaus))
   :sailytysaika (:sailytysaika (ui-desc->schema-map SailytysAika))
   :henkilotiedot (apply s/enum (:values Henkilotiedot))
   :kieli (apply s/enum (:values Kieli))})

(def AsiakirjaMetaDataMap
  (merge MetaDataMap {:tila (apply s/enum (:values Tila))
                      :myyntipalvelu (:schema Myyntipalvelu)
                      :nakyvyys (apply s/enum (:values Nakyvyys))}))

(defn valid-dependencies? [{{:keys [arkistointi pituus laskentaperuste]} :sailytysaika
                            :keys [julkisuusluokka salassapitoaika salassapitoperuste turvallisuusluokka suojaustaso kayttajaryhma kayttajaryhmakuvaus]}]
  (cond-> true
          (= :toistaiseksi arkistointi) (and laskentaperuste)
          (= :määräajan arkistointi) (and pituus)
          (not= :julkinen julkisuusluokka) (and salassapitoaika salassapitoperuste turvallisuusluokka suojaustaso kayttajaryhma kayttajaryhmakuvaus)))

(def ConstrainedMetadataMap
  (s/constrained MetaDataMap valid-dependencies? "All required keys present in metadata"))

(def ConstrainedAsiakirjaMetadataMap
  (s/constrained AsiakirjaMetaDataMap valid-dependencies? "All required keys present in metadata"))

(def default-metadata
  {:julkisuusluokka :julkinen
   :sailytysaika {:arkistointi :ei
                  :perustelu ""
                  :laskentaperuste (first laskentaperuste)
                  :pituus 0}
   :henkilotiedot :ei-sisalla
   :kieli :fi})

(def asiakirja-default-metadata
  (merge default-metadata {:tila :luonnos
                           :myyntipalvelu false
                           :nakyvyys :julkinen}))

(defn remove-unrecognized-keys [metadata schema-or-constrained]
  (let [schema (get schema-or-constrained :schema schema-or-constrained)
        known-keys (->> (map #(or (:k %) %) (keys schema))
                        (into #{}))]
    (->> metadata
         (filter (fn [[k _]] (known-keys k)))
         (into {}))))

(defn remove-conditional-keys [{:keys [sailytysaika julkisuusluokka] :as metadata}]
  (cond-> metadata
          (not= (:arkistointi sailytysaika) :määräajan)    (dissoc-in [:sailytysaika :pituus])
          (not= (:arkistointi sailytysaika) :määräajan)    (dissoc-in [:sailytysaika :retention-period-end])
          (not= (:arkistointi sailytysaika) :toistaiseksi) (dissoc-in [:sailytysaika :laskentaperuste])
          (= julkisuusluokka :julkinen)                    (dissoc :salassapitoaika :salassapitoperuste :turvallisuusluokka :suojaustaso :kayttajaryhma :kayttajaryhmakuvaus :security-period-end)))

(defn sanitize-metadata [metadata]
  (let [schema (if (:tila metadata) ConstrainedAsiakirjaMetadataMap ConstrainedMetadataMap)]
    (s/validate schema
                (-> metadata
                    (remove-conditional-keys)
                    (remove-unrecognized-keys schema)))))

(def common-metadata-fields
  [Julkisuusluokka Henkilotiedot Kieli SailytysAika])

(def asiakirja-metadata-fields
  [Tila Myyntipalvelu Nakyvyys])

(defn sailytysaika-to-s2-xml [{:keys [arkistointi] :as sailytysaika}]
  (let [replacement-map {:pituus :RetentionPeriod
                         :retention-period-end :RetentionPeriodEnd
                         :perustelu :RetentionReason}]
    (cond-> (->> (map (fn [[k v]] [(get replacement-map k k) v]) sailytysaika)
                 (into {}))
            (#{:ikuisesti :toistaiseksi} (keyword arkistointi)) (assoc :RetentionPeriod 999999))))

(def lp-to-s2-xml-key-map
  {:tila :Status
   :henkilotiedot :Restriction.PersonalData
   :julkisuusluokka :Restriction.PublicityClass
   :salassapitoaika :Restriction.SecurityPeriod
   :security-period-end :Restriction.SecurityPeriodEnd
   :suojaustaso :Restriction.Protectionlevel
   :kayttajaryhma :Restriction.AccessRight.Role
   :kayttajaryhmakuvaus :Restriction.AccessRight.Description
   :turvallisuusluokka :Restriction.SecurityClass
   :salassapitoperuste :Restriction.SecurityReason
   :tosFunction :Function
   :kieli :Language})

(defn replace-metadata-keys-with-s2-xml-keys [metadata]
  (->> metadata
       (map (fn [[k v]]
              (if (= :sailytysaika k)
                (sailytysaika-to-s2-xml v)
                [(get lp-to-s2-xml-key-map k k) v])))
       (into {})))
