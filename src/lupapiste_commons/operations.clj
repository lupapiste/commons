(ns lupapiste-commons.operations)

(def operation-tree-for-R
  ["Rakentaminen ja purkaminen"
   [["Uuden rakennuksen rakentaminen"
     [["kerrostalo-rivitalo" :kerrostalo-rivitalo]
      ["pientalo" :pientalo]
      ["Vapaa-ajan asuinrakennus" :vapaa-ajan-asuinrakennus]
      ["Varasto, sauna, autotalli tai muu talousrakennus" :varasto-tms]
      ["teollisuusrakennus" :teollisuusrakennus]
      ["Muun rakennuksen rakentaminen" :muu-uusi-rakentaminen]]]
    ["Rakennuksen-laajentaminen"
     [["kerrostalo-rt-laaj" :kerrostalo-rt-laaj]
      ["pientalo-laaj" :pientalo-laaj]
      ["vapaa-ajan-rakennus-laaj" :vapaa-ajan-rakennus-laaj]
      ["talousrakennus-laaj" :talousrakennus-laaj]
      ["teollisuusrakennus-laaj" :teollisuusrakennus-laaj]
      ["muu-rakennus-laaj" :muu-rakennus-laaj]]]
    ["Rakennuksen korjaaminen tai muuttaminen"
     [["kayttotark-muutos" :kayttotark-muutos]
      ["sisatila-muutos" :sisatila-muutos]
      ["Rakennuksen julkisivun tai katon muuttaminen" :julkisivu-muutos]
      ["Markatilan laajentaminen" :markatilan-laajentaminen]
      ["linjasaneeraus" :linjasaneeraus]
      ["Parvekkeen tai terassin lasittaminen" :parveke-tai-terassi]
      ["Perustusten tai kantavien rakenteiden muuttaminen tai korjaaminen" :perus-tai-kant-rak-muutos]
      ["Takan ja savuhormin rakentaminen" :takka-tai-hormi]
      ["Asuinhuoneiston jakaminen tai yhdistaminen" :jakaminen-tai-yhdistaminen]]]
    ["Rakennelman rakentaminen"
     [["Auto- tai grillikatos, vaja, kioski tai vastaava" :auto-katos]
      ["Masto, piippu, sailio, laituri tai vastaava" :masto-tms]
      ["Mainoslaite" :mainoslaite]
      ["Aita" :aita]
      ["Maalampokaivon poraaminen tai lammonkeruuputkiston asentaminen" :maalampo]
      ["Rakennuksen jatevesijarjestelman uusiminen" :jatevesi]]]
    ["Rakennuksen purkaminen" :purkaminen]
    ["Maisemaa muutava toimenpide"
     [["Puun kaataminen" :puun-kaataminen]
      ["tontin-jarjestelymuutos" :tontin-jarjestelymuutos]
      ["Muu-tontti-tai-korttelialueen-jarjestelymuutos" :muu-tontti-tai-kort-muutos]
      ["Kaivaminen, louhiminen tai maan tayttaminen" :kaivuu]
      ["Muu maisemaa muuttava toimenpide" :muu-maisema-toimenpide]]]
    ["rakennustyo-muutostoimenpiteet"
     [["Suunnittelija" :suunnittelijan-nimeaminen]
      ["Tyonjohtaja" :tyonjohtajan-nimeaminen-v2]
      ["rak-valm-tyo" :rak-valm-tyo]
      ["Aloitusoikeus" :aloitusoikeus]
      ["raktyo-aloit-loppuunsaat" :raktyo-aloit-loppuunsaat]]]]])

(def operation-tree-for-YA
  ["yleisten-alueiden-luvat"
   [["sijoituslupa"
     [["pysyvien-maanalaisten-rakenteiden-sijoittaminen"
       [["vesi-ja-viemarijohtojen-sijoittaminen" :ya-sijoituslupa-vesi-ja-viemarijohtojen-sijoittaminen]
        ["maalampoputkien-sijoittaminen" :ya-sijoituslupa-maalampoputkien-sijoittaminen]
        ["kaukolampoputkien-sijoittaminen" :ya-sijoituslupa-kaukolampoputkien-sijoittaminen]
        ["sahko-data-ja-muiden-kaapelien-sijoittaminen" :ya-sijoituslupa-sahko-data-ja-muiden-kaapelien-sijoittaminen]
        ["rakennuksen-tai-sen-osan-sijoittaminen" :ya-sijoituslupa-rakennuksen-tai-sen-osan-sijoittaminen]]]
      ["pysyvien-maanpaallisten-rakenteiden-sijoittaminen"
       [["ilmajohtojen-sijoittaminen" :ya-sijoituslupa-ilmajohtojen-sijoittaminen]
        ["muuntamoiden-sijoittaminen" :ya-sijoituslupa-muuntamoiden-sijoittaminen]
        ["jatekatoksien-sijoittaminen" :ya-sijoituslupa-jatekatoksien-sijoittaminen]
        ["leikkipaikan-tai-koiratarhan-sijoittaminen" :ya-sijoituslupa-leikkipaikan-tai-koiratarhan-sijoittaminen]
        ["rakennuksen-pelastuspaikan-sijoittaminen" :ya-sijoituslupa-rakennuksen-pelastuspaikan-sijoittaminen]]]
     ["muu-sijoituslupa" :ya-sijoituslupa-muu-sijoituslupa]]]
    ["katulupa"
     [["kaivaminen-yleisilla-alueilla"
       [["vesi-ja-viemarityot" :ya-katulupa-vesi-ja-viemarityot]
        ["maalampotyot" :ya-katulupa-maalampotyot]
        ["kaukolampotyot" :ya-katulupa-kaukolampotyot]
        ["kaapelityot" :ya-katulupa-kaapelityot]
        ["kiinteiston-johto-kaapeli-ja-putkiliitynnat" :ya-katulupa-kiinteiston-johto-kaapeli-ja-putkiliitynnat]]]
      ["liikennealueen-rajaaminen-tyokayttoon"
       [["nostotyot" :ya-kayttolupa-nostotyot]
        ["vaihtolavat" :ya-kayttolupa-vaihtolavat]
        ["kattolumien-pudotustyot" :ya-kayttolupa-kattolumien-pudotustyot]
        ["muu-liikennealuetyo" :ya-kayttolupa-muu-liikennealuetyo]]]
      ["yleisen-alueen-rajaaminen-tyomaakayttoon"
       [["talon-julkisivutyot" :ya-kayttolupa-talon-julkisivutyot]
        ["talon-rakennustyot" :ya-kayttolupa-talon-rakennustyot]
        ["muu-tyomaakaytto" :ya-kayttolupa-muu-tyomaakaytto]]]]]
    ["kayttolupa"
     [["tapahtumat" :ya-kayttolupa-tapahtumat]
      ["harrastustoiminnan-jarjestaminen" :ya-kayttolupa-harrastustoiminnan-jarjestaminen]
      ["mainokset" :ya-kayttolupa-mainostus-ja-viitoitus]
      ["metsastys" :ya-kayttolupa-metsastys]
      ["vesistoluvat" :ya-kayttolupa-vesistoluvat]
      ["terassit" :ya-kayttolupa-terassit]
      ["kioskit" :ya-kayttolupa-kioskit]
      ["muu-kayttolupa" :ya-kayttolupa-muu-kayttolupa]]]
    ["jatkoaika" :ya-jatkoaika]]])

(def operation-tree-for-P
  ["Poikkeusluvat ja suunnittelutarveratkaisut" :poikkeamis])

(def operation-tree-for-Y
  ["Ymp\u00e4rist\u00f6luvat"
   [;; permit/YI
    ["Meluilmoitus" :meluilmoitus]

    ;; permit/MAL
    ["maa-ainesten_ottaminen" :maa-aineslupa]

    ;; permit/YL
    ["ympariston-pilaantumisen-vaara"
     [["uusi-toiminta" :yl-uusi-toiminta]
      ["olemassa-oleva-toiminta" :yl-olemassa-oleva-toiminta]
      ["toiminnan-muutos" :yl-toiminnan-muutos]]]

    ;; permit/VVVL
    ["vapautus-vesijohdosta-ja-viemariin-liitymisvelvollisuudeseta"
     [["vesijohdosta" :vvvl-vesijohdosta]
      ["viemarista" :vvvl-viemarista]
      ["vesijohdosta-ja-viemarista" :vvvl-vesijohdosta-ja-viemarista]
      ["hulevesiviemarista" :vvvl-hulevesiviemarista]]]]])

(def operation-tree-for-KT ; aka kiinteistotoimitus aka maanmittaustoimitukset
  ["maanmittaustoimitukset"
   [["tonttijaon-hakeminen" :tonttijaon-hakeminen]
    ["tonttijaon-muutoksen-hakeminen" :tonttijaon-muutoksen-hakeminen]
    ["tontin-lohkominen" :tontin-lohkominen]
    ["tilan-rekisteroiminen-tontiksi" :tilan-rekisteroiminen-tontiksi]
    ["yhdistaminen" :yhdistaminen]
    ["halkominen" :halkominen]
    ["rasitetoimitus" :rasitetoimitus]
    ["tilusvaihto" :tilusvaihto]
    ["rajankaynnin-hakeminen" :rajankaynnin-hakeminen]
    ["rajannayton-hakeminen" :rajannayton-hakeminen]
    ["rakennuksen-sijainti" :rakennuksen-sijainti]
    ["ya-lohkominen" :ya-lohkominen]]])

(def operation-tree-for-MM
  ["maankayton-muutos" ; kaavat
  [["asemakaava-laadinta" :asemakaava-laadinta]
   ["asemakaava-muutos" :asemakaava-muutos]
   ["ranta-asemakaava-laadinta" :ranta-asemakaava-laadinta]
   ["ranta-asemakaava-muutos" :ranta-asemakaava-muutos]
   ["yleiskaava-laadinta" :yleiskaava-laadinta]
   ["yleiskaava-muutos" :yleiskaava-muutos]]])
