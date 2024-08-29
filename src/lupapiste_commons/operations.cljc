(ns lupapiste-commons.operations)

(def r-operations
  "R operations allowed in archiving"
  [:aiemmalla-luvalla-hakeminen
   :aita
   :aloitusoikeus
   :asuinrakennus
   :auto-katos
   :jakaminen-tai-yhdistaminen
   :jatevesi
   :jatkoaika
   :julkinen-rakennus
   :julkisivu-muutos
   :kaivuu
   :kayttotark-muutos
   :kerrostalo-rivitalo
   :kerrostalo-rt-laaj
   :konversio
   :konversio-rakennus
   :kortteli-yht-alue-muutos
   :laajentaminen
   :linjasaneeraus
   :maalampo
   :mainoslaite
   :markatilan-laajentaminen
   :masto-tms
   :muu-laajentaminen
   :muu-maisema-toimenpide
   :muu-rakennus-laaj
   :muu-rakentaminen
   :muu-tontti-tai-kort-muutos
   :muu-uusi-rakentaminen
   :paikoutysjarjestus-muutos
   :parveke-tai-terassi
   :perus-tai-kant-rak-muutos
   :pientalo
   :pientalo-laaj
   :purkaminen
   :puun-kaataminen
   :rak-valm-tyo
   :rakennustietojen-korjaus
   :raktyo-aloit-loppuunsaat
   :rasite-tai-yhteisjarjestely
   :sisatila-muutos
   :suunnittelijan-nimeaminen
   :takka-tai-hormi
   :talousrakennus-laaj
   :teollisuusrakennus
   :teollisuusrakennus-laaj
   :tontin-ajoliittyman-muutos
   :tontin-jarjestelymuutos
   :tyonjohtajan-nimeaminen
   :tyonjohtajan-nimeaminen-v2
   :vapaa-ajan-asuinrakennus
   :vapaa-ajan-rakennus-laaj
   :varasto-tms])

(def ya-operations
  "YA operations allowed in archiving"
  [:ya-jatkoaika
   :ya-katulupa-kaapelityot
   :ya-katulupa-kaukolampotyot
   :ya-katulupa-kiinteiston-johto-kaapeli-ja-putkiliitynnat
   :ya-katulupa-maalampotyot
   :ya-katulupa-muu-liikennealuetyo
   :ya-katulupa-vesi-ja-viemarityot
   :ya-kayttolupa-harrastustoiminnan-jarjestaminen
   :ya-kayttolupa-kattolumien-pudotustyot
   :ya-kayttolupa-kioskit
   :ya-kayttolupa-mainostus-ja-viitoitus
   :ya-kayttolupa-metsastys
   :ya-kayttolupa-muu-kayttolupa
   :ya-kayttolupa-muu-tyomaakaytto
   :ya-kayttolupa-nostotyot
   :ya-kayttolupa-talon-julkisivutyot
   :ya-kayttolupa-talon-rakennustyot
   :ya-kayttolupa-tapahtumat
   :ya-kayttolupa-terassit
   :ya-kayttolupa-vaihtolavat
   :ya-kayttolupa-vesistoluvat
   :ya-sijoituslupa-ilmajohtojen-sijoittaminen
   :ya-sijoituslupa-jatekatoksien-sijoittaminen
   :ya-sijoituslupa-kaukolampoputkien-sijoittaminen
   :ya-sijoituslupa-leikkipaikan-tai-koiratarhan-sijoittaminen
   :ya-sijoituslupa-maalampoputkien-sijoittaminen
   :ya-sijoituslupa-muu-sijoituslupa
   :ya-sijoituslupa-muuntamoiden-sijoittaminen
   :ya-sijoituslupa-rakennuksen-pelastuspaikan-sijoittaminen
   :ya-sijoituslupa-rakennuksen-tai-sen-osan-sijoittaminen
   :ya-sijoituslupa-sahko-data-ja-muiden-kaapelien-sijoittaminen
   :ya-sijoituslupa-vesi-ja-viemarijohtojen-sijoittaminen])

(def ymp-operations
  "YI, YM, YL, MAL and VVVL operations allowed in archiving"
  [:meluilmoitus
   :yleinen-ilmoitus
   :koeluontoinen-toiminta
   :ilmoitus-poikkeuksellisesta-tilanteesta
   :ylijaamamaiden-hyodyntaminen
   :jatteiden-hyodyntaminen-maarakentamisessa
   :rekisterointi-ilmoitus
   :lannan-varastointi
   :lannan-levittamisesta-poikkeustilanteessa
   :pima
   :kaytostapoistetun-oljy-tai-kemikaalisailion-jattaminen-maaperaan
   :talousjatevesien-kasittelysta-poikkeaminen
   :maa-aineslupa
   :maa-aineslupa-jatkoaika
   :maa-ainesten-kotitarveotto
   :yhteiskasittely-maa-aines-ja-ymparistoluvalle
   :yl-uusi-toiminta
   :yl-olemassa-oleva-toiminta
   :yl-toiminnan-muutos
   :vvvl-vesijohdosta
   :vvvl-viemarista
   :vvvl-vesijohdosta-ja-viemarista
   :vvvl-hulevesiviemarista
   :jatteen-keraystoiminta
   :muistomerkin-rauhoittaminen
   :maastoliikennelaki-kilpailut-ja-harjoitukset
   :vesiliikennelaki-kilpailut-ja-harjoitukset
   :yl-puiden-kaataminen
   :ymparistoluvan-selventaminen
   :leirintaalueilmoitus
   :kirjallinen-vireillepano
   :aloituslupa])

(def deprecated-ya-operations
  "Add retired YA operations here to keep them valid in archiving. Leave translations available."
  [:ya-kayttolupa-muu-liikennealuetyo])

(def p-operations
  "P operations allowed in archiving"
  [:poikkeamis])

(def archiving-project-operations
  [:archiving-project])
