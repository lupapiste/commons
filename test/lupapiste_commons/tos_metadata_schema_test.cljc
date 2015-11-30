(ns lupapiste-commons.tos-metadata-schema-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.tos-metadata-schema :as tms]))

(def sample-metadata
  {"henkilotiedot" "ei-sisalla", "julkisuusluokka" "salainen",
   "sailytysaika" {"arkistointi" "määräajan", "laskentaperuste" "lupapäätöspäivä", "perustelu" "Laki", "pituus" 5}
   "suojaustaso" "suojaustaso1", "salassapitoaika" 5, "salassapitoperuste" "perustelu", "turvallisuusluokka" "turvallisuusluokka4"
   "tila" "luonnos", "myyntipalvelu" true, "nakyvyys" "julkinen"})

(deftest coercion-to-schema-works
  (let [coerced (tms/coerce-metadata-to-schema sample-metadata)]
    (is (= :lupapäätöspäivä (get-in coerced [:sailytysaika :laskentaperuste])))))
