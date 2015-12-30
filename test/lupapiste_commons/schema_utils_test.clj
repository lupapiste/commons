(ns lupapiste-commons.schema-utils-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.tos-metadata-schema :as tms]
            [lupapiste-commons.schema-utils :as schema-utils]))

(def sample-metadata
  {"henkilotiedot" "ei-sisalla", "julkisuusluokka" "salainen",
   "sailytysaika" {"arkistointi" "määräajan", "laskentaperuste" "lupapäätöspäivä", "perustelu" "Laki", "pituus" 5}
   "suojaustaso" "suojaustaso1", "salassapitoaika" 5, "salassapitoperuste" "perustelu", "turvallisuusluokka" "turvallisuusluokka4"
   "tila" "luonnos", "myyntipalvelu" true, "nakyvyys" "julkinen"})

(deftest coercion-to-schema-works
  (let [coerced (schema-utils/coerce-metadata-to-schema tms/AsiakirjaMetaDataMap sample-metadata)]
    (is (= :lupapäätöspäivä (get-in coerced [:sailytysaika :laskentaperuste])))))
