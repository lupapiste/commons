(ns lupapiste-commons.archive-metadata-schema
  (:require [schema.core :as s]
            [clojure.string :as str]
            [lupapiste-commons.tos-metadata-schema :as tms]
            [lupapiste-commons.attachment-types :as attachment-types]
            [lupapiste-commons.building-classifications :as building-classifications]
            [lupapiste-commons.operations :as operations]
            [lupapiste-commons.usage-types :as usages]))

(def document-types [:hakemus :ilmoitus :neuvontapyyntö :case-file])

(defn groups->dotted-keywords [groups]
  (reduce
    (fn [acc [group values]]
      (reduce (fn [acc value]
                (->> [group value]
                     (map name)
                     (str/join ".")
                     keyword
                     (conj acc)))
              acc
              values))
    []
    (partition 2 groups)))

(def document-and-attachment-types (-> document-types
                                       (concat (groups->dotted-keywords attachment-types/Rakennusluvat-v2))
                                       (concat (groups->dotted-keywords attachment-types/YleistenAlueidenLuvat-v2))))

(def valid-operations (concat operations/r-operations
                              operations/ya-operations
                              operations/deprecated-ya-operations
                              operations/p-operations
                              operations/archiving-project-operations))

(def valid-usage-types (map :name usages/rakennuksen-kayttotarkoitus))

(def valid-building-classfications (map :name building-classifications/rakennuksen-rakennusluokka))

(def Coordinates (s/pair s/Num "x" s/Num "y"))

(defn- some-location-and-permit-id-exist?
  [{:keys [location-etrs-tm35fin location-wgs84 applicationId kuntalupatunnukset]}]
  (and (or (seq location-etrs-tm35fin)
           (seq location-wgs84))
       (or applicationId
           (seq kuntalupatunnukset))))


(defn some-user-data-exists? [{:keys [userId username firstName lastName]}]
  (or userId username firstName lastName))

(def UserData
  (s/constrained
    {(s/optional-key :userId)    tms/NonEmptyStr
     (s/optional-key :username)  tms/NonEmptyStr
     (s/optional-key :firstName) tms/NonEmptyStr
     (s/optional-key :lastName)  tms/NonEmptyStr}
    some-user-data-exists?))

(def db-property-id-pattern #"^\d{14}$")
(def PropertyId (s/constrained s/Str #(re-matches db-property-id-pattern %)))

(def full-document-metadata
  (merge
    ;; The keys that are shared with lupapiste are generally in English
    {:type                                          (apply s/enum document-and-attachment-types)
     (s/optional-key :contents)                     tms/NonEmptyStr
     (s/optional-key :size)                         tms/NonEmptyStr
     (s/optional-key :scale)                        tms/NonEmptyStr
     (s/optional-key :applicationId)                tms/NonEmptyStr
     (s/optional-key :buildingIds)                  [tms/NonEmptyStr]
     (s/optional-key :nationalBuildingIds)          [tms/NonEmptyStr]
     (s/optional-key :propertyId)                   PropertyId
     (s/optional-key :projectDescription)           tms/NonEmptyStr
     :applicants                                    [tms/NonEmptyStr]
     :operations                                    [(apply s/enum valid-operations)]
     :tosFunction                                   {:name tms/NonEmptyStr :code tms/NonEmptyStr}
     :address                                       tms/NonEmptyStr
     :organization                                  tms/NonEmptyStr
     :municipality                                  tms/NonEmptyStr
     (s/optional-key :location-etrs-tm35fin)        Coordinates
     (s/optional-key :location-wgs84)               Coordinates
     (s/optional-key :postinumero)                  tms/NonEmptyStr
     :kuntalupatunnukset                            [tms/NonEmptyStr]
     (s/optional-key :lupapvm)                      s/Inst
     (s/optional-key :paatospvm)                    s/Inst
     (s/optional-key :jattopvm)                     s/Inst
     (s/optional-key :paatoksentekija)              tms/NonEmptyStr
     :tiedostonimi                                  tms/NonEmptyStr
     (s/optional-key :kasittelija)                  UserData
     (s/optional-key :arkistoija)                   UserData
     :arkistointipvm                                s/Inst
     (s/optional-key :kayttotarkoitukset)           [(apply s/enum valid-usage-types)]
     (s/optional-key :rakennusluokat)               [(apply s/enum valid-building-classfications)]
     (s/optional-key :suunnittelijat)               [tms/NonEmptyStr]
     :kieli                                         tms/NonEmptyStr
     :versio                                        tms/NonEmptyStr
     (s/optional-key :kylanumero)                   tms/NonEmptyStr
     (s/optional-key :kylanimi)                     {:fi tms/NonEmptyStr
                                                     :sv tms/NonEmptyStr}
     (s/optional-key :foremen)                      tms/NonEmptyStr
     (s/optional-key :tyomaasta-vastaava)           tms/NonEmptyStr
     (s/optional-key :closed)                       s/Inst
     (s/optional-key :drawing-wgs84)                [{:type        tms/NonEmptyStr
                                                      :coordinates [s/Any]}]
     (s/optional-key :ramLink)                      s/Str
     (s/optional-key :deleted)                      s/Inst
     (s/optional-key :deletion-explanation)         s/Str

     (s/optional-key :permit-expired)               s/Bool
     (s/optional-key :permit-expired-date)          s/Inst
     (s/optional-key :demolished)                   s/Bool
     (s/optional-key :demolished-date)              s/Inst}
    tms/AsiakirjaMetaDataMap))

(def validation-schema-for-onkalo-update-metadata
  (-> (dissoc full-document-metadata (s/optional-key :location-wgs84))
      (assoc :type s/Keyword
             (s/optional-key :history) s/Any
             (s/optional-key :kuntalupatunnukset-muutossyy) tms/NonEmptyStr
             :location-wgs84 [{:type        tms/NonEmptyStr
                               :coordinates [s/Any]}])))

(def full-case-file-metadata
  (dissoc full-document-metadata :nakyvyys :myyntipalvelu))

(def full-document-metadata-location-required
  (s/constrained full-document-metadata some-location-and-permit-id-exist?))
