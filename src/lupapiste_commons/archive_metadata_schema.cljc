(ns lupapiste-commons.archive-metadata-schema
  (:require [schema.core :as s]
            [lupapiste-commons.tos-metadata-schema :as tms]))

(def full-document-metadata
  (merge
    ;; The keys that are shared with lupapiste are generally in English
    {:type (s/if map? {:type-group s/Str :type-id s/Str} s/Str)
     (s/optional-key :contents) s/Str
     (s/optional-key :size) s/Str
     (s/optional-key :scale) s/Str
     (s/optional-key :applicationId) s/Str
     (s/optional-key :buildingIds) [s/Str]
     (s/optional-key :nationalBuildingIds) [s/Str]
     (s/optional-key :propertyId) s/Str
     :applicant s/Str
     :operation s/Str
     :tosFunction {:name s/Str :code s/Str}
     :address s/Str
     :organization s/Str
     :municipality s/Str
     (s/optional-key :location) [s/Num]  ;; Coordinates
     (s/optional-key :postinumero) s/Str
     :kuntalupatunnus s/Str
     :lupaPvm s/Inst
     :paatosPvm s/Inst
     (s/optional-key :paatoksentekija) s/Str
     :tiedostonimi s/Str
     (s/optional-key :kasittelija) {(s/optional-key :username) s/Str (s/optional-key :firstName) s/Str :lastName s/Str}
     (s/optional-key :arkistoija) {(s/optional-key :username) s/Str (s/optional-key :firstName) s/Str :lastName s/Str}
     :arkistointiPvm s/Inst
     :kayttotarkoitus s/Str
     (s/optional-key :suunnittelija) s/Str
     :kieli s/Str
     :versio s/Str}
    tms/AsiakirjaMetaDataMap))
