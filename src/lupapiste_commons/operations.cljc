(ns lupapiste-commons.operations)

(def r-operations
  "R operations allowed in archiving"
  [:kerrostalo-rivitalo
   :pientalo
   :vapaa-ajan-asuinrakennus
   :varasto-tms
   :julkinen-rakennus
   :teollisuusrakennus
   :muu-uusi-rakentaminen
   :laajentaminen
   :kerrostalo-rt-laaj
   :pientalo-laaj
   :vapaa-ajan-rakennus-laaj
   :talousrakennus-laaj
   :teollisuusrakennus-laaj
   :muu-rakennus-laaj
   :perus-tai-kant-rak-muutos
   :kayttotark-muutos
   :sisatila-muutos
   :julkisivu-muutos
   :jakaminen-tai-yhdistaminen
   :markatilan-laajentaminen
   :linjasaneeraus
   :takka-tai-hormi
   :parveke-tai-terassi
   :muu-laajentaminen
   :auto-katos
   :masto-tms
   :mainoslaite
   :aita
   :maalampo
   :jatevesi
   :muu-rakentaminen
   :purkaminen
   :kaivuu
   :puun-kaataminen
   :tontin-jarjestelymuutos
   :muu-maisema-toimenpide
   :tontin-ajoliittyman-muutos
   :paikoutysjarjestus-muutos
   :kortteli-yht-alue-muutos
   :muu-tontti-tai-kort-muutos
   :tyonjohtajan-nimeaminen
   :tyonjohtajan-nimeaminen-v2
   :suunnittelijan-nimeaminen
   :jatkoaika
   :aiemmalla-luvalla-hakeminen
   :rak-valm-tyo
   :aloitusoikeus
   :raktyo-aloit-loppuunsaat])

(def ya-operations
  "YA operations allowed in archiving"
  [:ya-kayttolupa-tapahtumat
   :ya-kayttolupa-harrastustoiminnan-jarjestaminen
   :ya-kayttolupa-metsastys
   :ya-kayttolupa-vesistoluvat
   :ya-kayttolupa-terassit
   :ya-kayttolupa-kioskit
   :ya-kayttolupa-muu-kayttolupa
   :ya-kayttolupa-mainostus-ja-viitoitus
   :ya-kayttolupa-nostotyot
   :ya-kayttolupa-vaihtolavat
   :ya-kayttolupa-kattolumien-pudotustyot
   :ya-kayttolupa-muu-liikennealuetyo
   :ya-kayttolupa-talon-julkisivutyot
   :ya-kayttolupa-talon-rakennustyot
   :ya-kayttolupa-muu-tyomaakaytto
   :ya-katulupa-vesi-ja-viemarityot
   :ya-katulupa-maalampotyot
   :ya-katulupa-kaukolampotyot
   :ya-katulupa-kaapelityot
   :ya-katulupa-kiinteiston-johto-kaapeli-ja-putkiliitynnat
   :ya-sijoituslupa-vesi-ja-viemarijohtojen-sijoittaminen
   :ya-sijoituslupa-maalampoputkien-sijoittaminen
   :ya-sijoituslupa-kaukolampoputkien-sijoittaminen
   :ya-sijoituslupa-sahko-data-ja-muiden-kaapelien-sijoittaminen
   :ya-sijoituslupa-rakennuksen-tai-sen-osan-sijoittaminen
   :ya-sijoituslupa-ilmajohtojen-sijoittaminen
   :ya-sijoituslupa-muuntamoiden-sijoittaminen
   :ya-sijoituslupa-jatekatoksien-sijoittaminen
   :ya-sijoituslupa-leikkipaikan-tai-koiratarhan-sijoittaminen
   :ya-sijoituslupa-rakennuksen-pelastuspaikan-sijoittaminen
   :ya-sijoituslupa-muu-sijoituslupa
   :ya-jatkoaika])

(def p-operations
  "P operations allowed in archiving"
  [:poikkeamis])
