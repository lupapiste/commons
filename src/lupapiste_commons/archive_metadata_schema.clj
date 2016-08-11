(ns lupapiste-commons.archive-metadata-schema
  (:require [schema.core :as s]
            [clojure.string :as str]
            [lupapiste-commons.tos-metadata-schema :as tms]
            [lupapiste-commons.attachment-types :as attachment-types]))

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

(def document-and-attachment-types (concat document-types (groups->dotted-keywords attachment-types/Rakennusluvat-v2)))

(def full-document-metadata
  (merge
    ;; The keys that are shared with lupapiste are generally in English
    {:type (apply s/enum document-and-attachment-types)
     (s/optional-key :contents) s/Str
     (s/optional-key :size) s/Str
     (s/optional-key :scale) s/Str
     (s/optional-key :applicationId) s/Str
     (s/optional-key :buildingIds) [s/Str]
     (s/optional-key :nationalBuildingIds) [s/Str]
     (s/optional-key :propertyId) s/Str
     :applicants [s/Str]
     :operations [s/Str]
     :tosFunction {:name s/Str :code s/Str}
     :address s/Str
     :organization s/Str
     :municipality s/Str
     (s/optional-key :location-etrs-tm35fin) [s/Num]  ;; Coordinates
     (s/optional-key :location-wgs84) [s/Num]  ;; Coordinates
     (s/optional-key :postinumero) s/Str
     :kuntalupatunnukset [s/Str]
     :lupapvm s/Inst
     :paatospvm s/Inst
     (s/optional-key :paatoksentekija) s/Str
     :tiedostonimi s/Str
     (s/optional-key :kasittelija) {(s/optional-key :username) s/Str (s/optional-key :firstName) s/Str :lastName s/Str}
     (s/optional-key :arkistoija) {(s/optional-key :username) s/Str (s/optional-key :firstName) s/Str :lastName s/Str}
     :arkistointipvm s/Inst
     :kayttotarkoitukset [s/Str]
     (s/optional-key :suunnittelijat) [s/Str]
     :kieli s/Str
     :versio s/Str
     (s/optional-key :kylanumero) s/Str
     (s/optional-key :kylanimi) {:fi s/Str
                                 :sv s/Str}}
    tms/AsiakirjaMetaDataMap))

(def full-document-metadata-with-relaxed-type
  (assoc full-document-metadata :type s/Keyword))
