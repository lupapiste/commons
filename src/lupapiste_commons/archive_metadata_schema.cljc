(ns lupapiste-commons.archive-metadata-schema
  (:require [schema.core :as s]
            [lupapiste-commons.tos-metadata-schema :as tms]))

(def full-document-metadata
  (merge
    ;; The keys that are shared with lupapiste are generally in English
    {:type s/Str
     (s/optional-key :contents) s/Str
     (s/optional-key :size) s/Str
     (s/optional-key :scale) s/Str
     (s/optional-key :applicationId) s/Str
     (s/optional-key :buildingIds) [s/Str]
     (s/optional-key :nationalBuildingIds) [s/Str]
     (s/optional-key :propertyId) s/Str
     :applicant s/Str
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
     (s/optional-key :suunnittelija) s/Str
     :kieli s/Str
     :versio s/Str
     (s/optional-key :kylanumero) s/Str
     (s/optional-key :retention-period-end) s/Inst
     (s/optional-key :security-period-end) s/Inst}
    tms/AsiakirjaMetaDataMap))
