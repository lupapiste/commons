(ns lupapiste-commons.archive-metadata-schema
  (:require [schema.core :as s]
            [lupapiste-commons.tos-metadata-schema :as tms]))

(def full-document-metadata
  (merge
    {:type (s/if map? {:type-group s/Str :type-id s/Str} s/Str)
     :contents s/Str
     (s/optional-key :size) s/Str
     (s/optional-key :scale) s/Str
     :kuntalupatunnus s/Str
     (s/optional-key :applicationId) s/Str
     (s/optional-key :buildingIds) [s/Str]
     (s/optional-key :nationalBuildingIds) [s/Str]
     (s/optional-key :propertyId) s/Str
     :operation s/Str
     :tosFunction {:name s/Str :code s/Str}
     :phase s/Str
     :permitDate s/Inst
     :verdictDate s/Int
     :verdictGivenBy s/Str
     :pykala s/Str
     :tilanne s/Str
     :tilannePvm s/Inst
     :address s/Str
     :municipality s/Str
     :postalCode s/Str
     (s/optional-key :location) [s/Num]
     :applicant s/Str
     :original-file-name s/Str
     :archiver s/Str
     :archivingDate s/Inst
     :kayttotarkoitus s/Str
     (s/optional-key :suunnittelija) s/Str
     :kieli s/Str
     :versio s/Str}
    tms/AsiakirjaMetaDataMap))
