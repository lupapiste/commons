(ns lupapiste-commons.schema-utils-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.tos-metadata-schema :as tms]
            [lupapiste-commons.schema-utils :as schema-utils]
            [lupapiste-commons.archive-metadata-schema :as ams]))

(def sample-metadata
  {"henkilotiedot" "ei-sisalla", "julkisuusluokka" "salainen",
   "sailytysaika" {"arkistointi" "määräajan", "laskentaperuste" "lupapäätöspäivä", "perustelu" "Laki", "pituus" 5}
   "suojaustaso" "suojaustaso1", "salassapitoaika" 5, "salassapitoperuste" "perustelu", "turvallisuusluokka" "turvallisuusluokka4"
   "tila" "luonnos", "myyntipalvelu" true, "nakyvyys" "julkinen"})

(def full-archival-schema-sample
  {:tila "arkistoitu",
   :closed #inst"1969-12-31T23:59:59.992-00:00",
   :address "E'[N",
   :nakyvyys :viranomainen,
   :kylanimi {:fi "ZyRS<B<v9", :sv "HC9\""},
   :sailytysaika {:arkistointi :määräajan, :perustelu "8%"},
   :myyntipalvelu false,
   :type :rakennuspaikka.perustamistapalausunto,
   :kylanumero "D",
   :municipality "6mJ,F+*3",
   :organization ",v'A;$",
   :size "K#qX\"",
   :suunnittelijat ["foo" "bar"],
   :foremen "KN/4ZI^B",
   :applicationId "\\4*Sy<?J\\",
   :paatospvm #inst"1970-01-01T00:00:00.007-00:00",
   :drawing-wgs84 [{:type "xV>",
                    :coordinates [[-0.4609375 -2 4 -2.5 2 1 471 -0.875 -2.5]
                                  [-4.0 212 -186 1 208 2 19 -62]
                                  [-2 -0.9287109375 -3.5625]
                                  [-1.0]
                                  [0.65234375 0 14 -2 6.0 -2.0 0 0.625 27]]}],
   :kieli :en,
   :location-wgs84 ["25.0" "63.0"],
   :arkistointipvm #inst"1970-01-01T00:00:00.004-00:00",
   :versio "Dazg# `",
   :operations [:muu-tontti-tai-kort-muutos
                :ya-sijoituslupa-jatekatoksien-sijoittaminen
                :kerrostalo-rt-laaj
                :ya-sijoituslupa-rakennuksen-tai-sen-osan-sijoittaminen
                :tyonjohtajan-nimeaminen-v2
                :ya-sijoituslupa-kaukolampoputkien-sijoittaminen
                :kortteli-yht-alue-muutos
                :ya-sijoituslupa-maalampoputkien-sijoittaminen
                :aloitusoikeus
                :masto-tms],
   :tiedostonimi "~",
   :henkilotiedot :sisaltaa-arkaluonteisia,
   :kayttotarkoitukset ["342 seurakuntatalot"
                        "119 muut myymälärakennukset"
                        "161 rautatie- ja linja-autoasemat, lento- ja satamaterminaalit"
                        "239 muualla luokittelemattomat sosiaalitoimen rakennukset"],
   :paatoksentekija "Ud",
   :tosFunction {:name "t]49Jj5y/A", :code "59;NA"},
   :applicants ["börje" "arska"],
   :kuntalupatunnukset ["abcd"],
   :julkisuusluokka :salainen})

(deftest coercion-to-schema-works
  (let [coerced (schema-utils/coerce-metadata-to-schema tms/AsiakirjaMetaDataMap sample-metadata)]
    (is (= :lupapäätöspäivä (get-in coerced [:sailytysaika :laskentaperuste])))))

(deftest complex-coercion-works-too
  (let [coerced (schema-utils/coerce-metadata-to-schema ams/full-document-metadata-location-required
                                                        full-archival-schema-sample)]
    (is (= :arkistoitu (:tila coerced)))
    (is (= [25.0 63.0] (:location-wgs84 coerced)))))
