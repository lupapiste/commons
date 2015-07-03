(ns lupapiste-commons.usage-types)

(def yhden-asunnon-talot "011 yhden asunnon talot")
(def rivitalot "021 rivitalot")
(def vapaa-ajan-asuinrakennus "041 vapaa-ajan asuinrakennukset")
(def talousrakennus "941 talousrakennukset")
(def rakennuksen-kayttotarkoitus [{:name yhden-asunnon-talot}
                                  {:name "012 kahden asunnon talot"}
                                  {:name "013 muut erilliset talot"}
                                  {:name rivitalot}
                                  {:name "022 ketjutalot"}
                                  {:name "032 luhtitalot"}
                                  {:name "039 muut asuinkerrostalot"}
                                  {:name vapaa-ajan-asuinrakennus}
                                  {:name "111 myym\u00e4l\u00e4hallit"}
                                  {:name "112 liike- ja tavaratalot, kauppakeskukset"}
                                  {:name "119 muut myym\u00e4l\u00e4rakennukset"}
                                  {:name "121 hotellit yms"}
                                  {:name "123 loma-, lepo- ja virkistyskodit"}
                                  {:name "124 vuokrattavat lomam\u00f6kit ja -osakkeet"}
                                  {:name "129 muut majoitusliikerakennukset"}
                                  {:name "131 asuntolat yms"}
                                  {:name "139 muut asuntolarakennukset"}
                                  {:name "141 ravintolat yms"}
                                  {:name "151 toimistorakennukset"}
                                  {:name "161 rautatie- ja linja-autoasemat, lento- ja satamaterminaalit"}
                                  {:name "162 kulkuneuvojen suoja- ja huoltorakennukset"}
                                  {:name "163 pys\u00e4k\u00f6intitalot"}
                                  {:name "164 tietoliikenteen rakennukset"}
                                  {:name "169 muut liikenteen rakennukset"}
                                  {:name "211 keskussairaalat"}
                                  {:name "213 muut sairaalat"}
                                  {:name "214 terveyskeskukset"}
                                  {:name "215 terveydenhuollon erityislaitokset"}
                                  {:name "219 muut terveydenhuoltorakennukset"}
                                  {:name "221 vanhainkodit"}
                                  {:name "222 lasten- ja koulukodit"}
                                  {:name "223 kehitysvammaisten hoitolaitokset"}
                                  {:name "229 muut huoltolaitosrakennukset"}
                                  {:name "231 lasten p\u00e4iv\u00e4kodit"}
                                  {:name "239 muualla luokittelemattomat sosiaalitoimen rakennukset"}
                                  {:name "241 vankilat"}
                                  {:name "311 teatterit, ooppera-, konsertti- ja kongressitalot"}
                                  {:name "312 elokuvateatterit"}
                                  {:name "322 kirjastot ja arkistot"}
                                  {:name "323 museot ja taidegalleriat"}
                                  {:name "324 n\u00e4yttelyhallit"}
                                  {:name "331 seura- ja kerhorakennukset yms"}
                                  {:name "341 kirkot, kappelit, luostarit ja rukoushuoneet"}
                                  {:name "342 seurakuntatalot"}
                                  {:name "349 muut uskonnollisten yhteis\u00f6jen rakennukset"}
                                  {:name "351 j\u00e4\u00e4hallit"}
                                  {:name "352 uimahallit"}
                                  {:name "353 tennis-, squash- ja sulkapallohallit"}
                                  {:name "354 monitoimihallit ja muut urheiluhallit"}
                                  {:name "359 muut urheilu- ja kuntoilurakennukset"}
                                  {:name "369 muut kokoontumisrakennukset"}
                                  {:name "511 yleissivist\u00e4vien oppilaitosten rakennukset"}
                                  {:name "521 ammatillisten oppilaitosten rakennukset"}
                                  {:name "531 korkeakoulurakennukset"}
                                  {:name "532 tutkimuslaitosrakennukset"}
                                  {:name "541 j\u00e4rjest\u00f6jen, liittojen, ty\u00f6nantajien yms opetusrakennukset"}
                                  {:name "549 muualla luokittelemattomat opetusrakennukset"}
                                  {:name "611 voimalaitosrakennukset"}
                                  {:name "613 yhdyskuntatekniikan rakennukset"}
                                  {:name "691 teollisuushallit"}
                                  {:name "692 teollisuus- ja pienteollisuustalot"}
                                  {:name "699 muut teollisuuden tuotantorakennukset"}
                                  {:name "711 teollisuusvarastot"}
                                  {:name "712 kauppavarastot"}
                                  {:name "719 muut varastorakennukset"}
                                  {:name "721 paloasemat"}
                                  {:name "722 v\u00e4est\u00f6nsuojat"}
                                  {:name "729 muut palo- ja pelastustoimen rakennukset"}
                                  {:name "811 navetat, sikalat, kanalat yms"}
                                  {:name "819 el\u00e4insuojat, ravihevostallit, maneesit yms"}
                                  {:name "891 viljankuivaamot ja viljan s\u00e4ilytysrakennukset"}
                                  {:name "892 kasvihuoneet"}
                                  {:name "893 turkistarhat"}
                                  {:name "899 muut maa-, mets\u00e4- ja kalatalouden rakennukset"}
                                  {:name "931 saunarakennukset"}
                                  {:name talousrakennus}
                                  {:name "999 muualla luokittelemattomat rakennukset"}
                                  {:name "ei tiedossa"}])
