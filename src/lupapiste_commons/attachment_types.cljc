(ns lupapiste-commons.attachment-types)

(defn merge-attachment-listings
  "Merge two or more attachment listings and remove possible duplicates."
  [& listings]
  (letfn [(merge-with-acc [acc listing]
              (merge-with (comp vec distinct into)
                          acc
                          (apply hash-map listing)))]
    (into [] cat (reduce merge-with-acc {} listings))))

(def osapuolet
  [:cv
   :patevyystodistus
   :paa_ja_rakennussuunnittelijan_tiedot
   :tutkintotodistus])

(def osapuolet-v2
  [:cv
   :patevyystodistus
   :suunnittelijan_tiedot
   :tutkintotodistus])

(def Rakennusluvat
  [:hakija [:osakeyhtion_perustamiskirja
            :ote_asunto_osakeyhtion_hallituksen_kokouksen_poytakirjasta
            :ote_kauppa_ja_yhdistysrekisterista
            :valtakirja]
   :rakennuspaikan_hallinta [:jaljennos_kauppakirjasta_tai_muusta_luovutuskirjasta
                             :jaljennos_myonnetyista_lainhuudoista
                             :jaljennos_perunkirjasta
                             :jaljennos_vuokrasopimuksesta
                             :ote_asunto-osakeyhtion_kokouksen_poytakirjasta
                             :rasitesopimus
                             :rasitustodistus
                             :todistus_erityisoikeuden_kirjaamisesta
                             :kiinteiston_lohkominen]
   :rakennuspaikka [:kiinteiston_vesi_ja_viemarilaitteiston_suunnitelma
                    :ote_alueen_peruskartasta
                    :ote_asemakaavasta_jos_asemakaava_alueella
                    :ote_kiinteistorekisteristerista
                    :ote_ranta-asemakaavasta
                    :ote_yleiskaavasta
                    :rakennusoikeuslaskelma
                    :selvitys_rakennuspaikan_perustamis_ja_pohjaolosuhteista
                    :perustamistapalausunto
                    :tonttikartta_tarvittaessa]
   :paapiirustus [:asemapiirros
                  :paapiirustus
                  :pohjapiirros
                  :leikkauspiirros
                  :julkisivupiirros
                  :yhdistelmapiirros]
   :ennakkoluvat_ja_lausunnot [:elyn_tai_kunnan_poikkeamapaatos
                               :lausunto
                               :naapurien_suostumukset
                               :selvitys_naapurien_kuulemisesta
                               :suunnittelutarveratkaisu
                               :ymparistolupa]

   :rakentamisen_aikaiset [:erityissuunnitelma]
   :osapuolet osapuolet
   :muut [:energiataloudellinen_selvitys
          :energiatodistus
          :hulevesisuunnitelma
          :ilmanvaihtosuunnitelma
          :ilmoitus_vaestonsuojasta
          :jatevesijarjestelman_rakennustapaseloste
          :julkisivujen_varityssuunnitelma
          :kalliorakentamistekninen_suunnitelma
          :katselmuksen_tai_tarkastuksen_poytakirja
          :kerrosalaselvitys
          :keskustelu
          :kuvaus
          :liikkumis_ja_esteettomyysselvitys
          :lomarakennuksen_muutos_asuinrakennukseksi_selvitys_maaraysten_toteutumisesta
          :lammityslaitesuunnitelma
          :merkki_ja_turvavalaistussuunnitelma
          :palotekninen_selvitys
          :paloturvallisuusselvitys
          :paloturvallisuussuunnitelma
          :piha_tai_istutussuunnitelma
          :pohjaveden_hallintasuunnitelma
          :radontekninen_suunnitelma
          :rakennesuunnitelma
          :rakennetapaselvitys
          :rakennukseen_tai_sen_osaan_kohdistuva_kuntotutkimus_jos_korjaus_tai_muutostyo
          :rakennuksen_tietomalli_BIM
          :rakennusautomaatiosuunnitelma
          :riskianalyysi
          :sammutusautomatiikkasuunnitelma
          :selvitys_kiinteiston_jatehuollon_jarjestamisesta
          :selvitys_liittymisesta_ymparoivaan_rakennuskantaan
          :selvitys_purettavasta_rakennusmateriaalista_ja_hyvaksikaytosta
          :selvitys_rakennuksen_aaniteknisesta_toimivuudesta
          :selvitys_rakennuksen_kosteusteknisesta_toimivuudesta
          :selvitys_rakennuksen_rakennustaiteellisesta_ja_kulttuurihistoriallisesta_arvosta_jos_korjaus_tai_muutostyo
          :selvitys_rakennusjatteen_maarasta_laadusta_ja_lajittelusta
          :selvitys_rakennuspaikan_korkeusasemasta
          :selvitys_rakennuspaikan_terveellisyydesta
          :selvitys_rakenteiden_kokonaisvakavuudesta_ja_lujuudesta
          :selvitys_sisailmastotavoitteista_ja_niihin_vaikuttavista_tekijoista
          :selvitys_tontin_tai_rakennuspaikan_pintavesien_kasittelysta
          :sopimusjaljennos
          :suunnitelma_paloilmoitinjarjestelmista_ja_koneellisesta_savunpoistosta
          :vaestonsuojasuunnitelma
          :valaistussuunnitelma
          :valokuva
          :vesi_ja_viemariliitoslausunto_tai_kartta
          :vesikattopiirustus
          :ympariston_tietomalli_BIM
          :aitapiirustus
          :haittaaineet
          :hankeselvitys
          :ikkunadetaljit
          :karttaaineisto
          :kokoontumishuoneisto
          :korjausrakentamisen_energiaselvitys
          :maalampo_rakennettavuusselvitys
          :mainoslaitesuunnitelma
          :turvallisuusselvitys
          :yhteistilat
          :luonnos
          :muu
          :paatos
          :paatosote]])

(def Rakennusluvat-v2
  [:ennakkoluvat_ja_lausunnot  [:elyn_tai_kunnan_poikkeamapaatos
                                :naapurin_suostumus
                                :naapurin_kuuleminen
                                :naapurin_huomautus
                                :suunnittelutarveratkaisu
                                :ymparistolupa
                                :lausunto
                                :lausunnon_liite
                                :ennakkoneuvottelumuistio
                                :vesi_ja_viemariliitoslausunto_tai_kartta
                                :paatos_ajoliittymasta
                                :johtokartta
                                :maanalaisten_johtojen_sijaintiselvitys]
   :hakija [:osakeyhtion_perustamiskirja
            :ote_asunto_osakeyhtion_hallituksen_kokouksen_poytakirjasta
            :ote_kauppa_ja_yhdistysrekisterista
            :valtakirja]
   :osapuolet osapuolet-v2
   :paapiirustus [:aitapiirustus
                  :asemapiirros
                  :julkisivupiirustus
                  :leikkauspiirustus
                  :pohjapiirustus
                  :muu_paapiirustus]
   :rakennuspaikan_hallinta [:todistus_hallintaoikeudesta
                             :ote_yhtiokokouksen_poytakirjasta
                             :rasitesopimus
                             :rasitustodistus
                             :todistus_erityisoikeuden_kirjaamisesta
                             :kiinteiston_lohkominen
                             :sopimusjaljennos]
   :rakennuspaikka [:karttaaineisto
                    :karttaote
                    :ote_alueen_peruskartasta
                    :ote_asemakaavasta_jos_asemakaava_alueella
                    :ote_kiinteistorekisteristerista
                    :ote_ranta-asemakaavasta
                    :ote_yleiskaavasta
                    :perustamistapalausunto
                    :pintavaaitus
                    :rakennusoikeuslaskelma
                    :tonttikartta_tarvittaessa
                    :selvitys_rakennuspaikan_korkeusasemasta]
   :selvitykset [:yhteistilat
                 :energiataloudellinen_selvitys
                 :energiatodistus
                 :haittaaineselvitys
                 :kokoontumishuoneisto
                 :kosteudenhallintaselvitys
                 :kuntotarkastusselvitys
                 :laadunvarmistusselvitys
                 :liikkumis_ja_esteettomyysselvitys
                 :lomarakennuksen_muutos_asuinrakennukseksi_selvitys_maaraysten_toteutumisesta
                 :maalampo_porausraportti
                 :maalampo_rakennettavuusselvitys
                 :meluselvitys
                 :rakennukseen_tai_sen_osaan_kohdistuva_kuntotutkimus_jos_korjaus_tai_muutostyo
                 :selvitys_rakennusjatteen_maarasta_laadusta_ja_lajittelusta
                 :rakennuttajan_valvojaa_koskeva_selvitys
                 :rakenteellisen_turvallisuuden_alustava_riskiarvio
                 :rh_tietolomake
                 :riskianalyysi
                 :selvitys_kiinteiston_jatehuollon_jarjestamisesta
                 :selvitys_liittymisesta_ymparoivaan_rakennuskantaan
                 :selvitys_rakennuksen_kunnosta
                 :selvitys_rakennuksen_rakennustaiteellisesta_ja_kulttuurihistoriallisesta_arvosta_jos_korjaus_tai_muutostyo
                 :selvitys_rakennuksen_terveellisyydesta
                 :selvitys_rakennuksen_aaniteknisesta_toimivuudesta
                 :selvitys_rakennuspaikan_perustamis_ja_pohjaolosuhteista
                 :selvitys_rakennuspaikan_terveellisyydesta
                 :selvitys_sisailmastotavoitteista_ja_niihin_vaikuttavista_tekijoista
                 :selvitys_tontin_tai_rakennuspaikan_pintavesien_kasittelysta
                 :tarinaselvitys
                 :muu_selvitys]
   :suunnitelmat [:hankeselvitys
                  :julkisivujen_varityssuunnitelma
                  :jatevesijarjestelman_suunnitelma
                  :selvitys_rakennuksen_kosteusteknisesta_toimivuudesta
                  :mainoslaitesuunnitelma
                  :opastesuunnitelma
                  :piha_tai_istutussuunnitelma
                  :valaistussuunnitelma
                  :muu_suunnitelma]
   :erityissuunnitelmat [:hulevesisuunnitelma
                         :iv_suunnitelma
                         :ikkunadetaljit
                         :kalliorakentamistekninen_suunnitelma
                         :kvv_suunnitelma
                         :lammityslaitesuunnitelma
                         :pintatasaussuunnitelma
                         :pohjarakennesuunnitelma
                         :pohjaveden_hallintasuunnitelma
                         :radontekninen_suunnitelma
                         :rakennesuunnitelma
                         :sahkosuunnitelma
                         :tulisija_ja_hormisuunnitelma]
   :pelastusviranomaiselle_esitettavat_suunnitelmat [:merkki_ja_turvavalaistussuunnitelma
                                                     :palotekninen_lausunto
                                                     :paloturvallisuussuunnitelma
                                                     :savunpoistosuunnitelma
                                                     :sammutusautomatiikkasuunnitelma
                                                     :suunnitelma_paloilmoitinjarjestelmista_ja_koneellisesta_savunpoistosta
                                                     :turvallisuusselvitys
                                                     :ilmoitus_vaestonsuojasta
                                                     :vaestonsuojasuunnitelma
                                                     :muu_pelastusviranomaisen_suunnitelma]
   :paatoksenteko [:hakemus
                   :ilmoitus
                   :paatos
                   :paatosote
                   :valitusosoitus
                   :poytakirjaote
                   :muistio
                   :paatoksen_liite
                   :paatosehdotus]
   :muutoksenhaku [:huomautus
                   :valitus
                   :oikaisuvaatimus]
   :katselmukset_ja_tarkastukset [:aloituskokouksen_poytakirja
                                  :katselmuksen_liite
                                  :katselmuksen_tai_tarkastuksen_poytakirja
                                  :kayttoonottokatselmuksen_poytakirja
                                  :loppukatselmuksen_poytakirja
                                  :tarkastusasiakirja
                                  :tarkastusasiakirjan_yhteeveto]
   :tietomallit [:rakennuksen_tietomalli_BIM
                 :ympariston_tietomalli_BIM]
   :muut [:erityismenettelyasiakirja
          :keskustelu
          :kuvaus
          :luonnos
          :piirustusluettelo
          :sijoituslupaasiakirja
          :suorituskyvyttomyysvakuusasiakirja
          :tutkimus
          :vakuusasiakirja
          :valokuva
          :muu]])

(def YleistenAlueidenLuvat
  [:yleiset-alueet [:aiemmin-hankittu-sijoituspaatos
                    :asemapiirros
                    :liitoslausunto
                    :poikkileikkaus
                    :rakennuspiirros
                    :suunnitelmakartta
                    :tieto-kaivupaikkaan-liittyvista-johtotiedoista
                    :tilapainen-liikennejarjestelysuunnitelma
                    :tyyppiratkaisu
                    :tyoalueen-kuvaus
                    :valokuva
                    :valtakirja]
   :osapuolet osapuolet
   ;; This is needed for statement attachments to work.
   :muut [:muu
          :paatos
          :paatosote
          :lupaehto
          :keskustelu]])

(def YleistenAlueidenLuvat-v2
  [:yleiset-alueet [:aiemmin-hankittu-sijoituspaatos
                    :asemapiirros
                    :liitoslausunto
                    :poikkileikkaus
                    :rakennuspiirros
                    :suunnitelmakartta
                    :tieto-kaivupaikkaan-liittyvista-johtotiedoista
                    :tilapainen-liikennejarjestelysuunnitelma
                    :tyyppiratkaisu
                    :tyoalueen-kuvaus
                    :valokuva
                    :valtakirja]
   :osapuolet [:patevyystodistus]
   ;; This is needed for statement attachments to work.
   :katselmukset_ja_tarkastukset [:katselmuksen_tai_tarkastuksen_poytakirja]
   :ennakkoluvat_ja_lausunnot [:lausunto]
   :muut [:muu
          :paatos
          :paatosote
          :sopimus
          :keskustelu]])

(def Allu
  [:yleiset-alueet [ :suunnitelmakartta
                    :tilapainen-liikennejarjestelysuunnitelma
                    :tyoalueen-kuvaus
                    :valokuva
                    :valtakirja]
   :osapuolet [:patevyystodistus]
   ;; This is needed for statement attachments to work.
   :katselmukset_ja_tarkastukset [:katselmuksen_tai_tarkastuksen_poytakirja]
   :ennakkoluvat_ja_lausunnot [:lausunto]
   :muut [:muu
          :paatos
          :paatosote
          :sopimus
          :keskustelu]])

(def Ymparistoilmoitukset
  [:ilmoituslomakkeet [:ilmoituslomake]
   :kartat [:kartta-melun-ja-tarinan-leviamisesta]
   :rakennuspaikka [:karttaote]
   :hakija [:valtakirja]
   :paapiirustus [:asemapiirros]
   :muut [:muu
          :paatos
          :paatosote
          :keskustelu
          :rakennekuva
          :selvitys-jatteen-haitta-ainepitoisuuksista]])

(def Ymparistolupa
   [:laitoksen_tiedot [:voimassa_olevat_ymparistolupa_vesilupa
                       :muut_paatokset_sopimukset
                       :selvitys_ymparistovahinkovakuutuksesta]
    :laitosalue_sen_ymparisto [:tiedot_kiinteistoista
                               :tiedot_toiminnan_sijaintipaikasta
                               :kaavaote
                               :selvitys_pohjavesialueesta
                               :selvitys_rajanaapureista
                               :ote_asemakaavasta
                               :ote_yleiskaavasta]
    :laitoksen_toiminta [:yleiskuvaus_toiminnasta
                         :yleisolle_tarkoitettu_tiivistelma
                         :selvitys_tuotannosta
                         :tiedot_toiminnan_suunnitellusta
                         :tiedot_raaka-aineista
                         :tiedot_energian
                         :energiansaastosopimus
                         :vedenhankinta_viemarointi
                         :arvio_ymparistoriskeista
                         :liikenne_liikennejarjestelyt
                         :selvitys_ymparistoasioiden_hallintajarjestelmasta]
    :ymparistokuormitus [:paastot_vesistoon_viemariin
                         :paastot_ilmaan
                         :paastot_maaperaan_pohjaveteen
                         :tiedot_pilaantuneesta_maaperasta
                         :melupaastot_tarina
                         :selvitys_paastojen_vahentamisesta_puhdistamisesta
                         :syntyvat_jatteet
                         :selvitys_jatteiden_maaran_haitallisuuden_vahentamiseksi
                         :kaatopaikkaa_koskevan_lupahakemuksen_lisatiedot
                         :selvitys_vakavaraisuudesta_vakuudesta
                         :jatteen_hyodyntamista_kasittelya_koskevan_toiminnan_lisatiedot]
    :paras_tekniikka_kaytanto [:arvio_tekniikan_soveltamisesta
                               :arvio_paastojen_vahentamistoimien_ristikkaisvaikutuksista
                               :arvio_kaytannon_soveltamisesta]
    :vaikutukset_ymparistoon [:arvio_vaikutuksista_yleiseen_viihtyvyyteen_ihmisten_terveyteen
                              :arvio_vaikutuksista_luontoon_luonnonsuojeluarvoihin_rakennettuun_ymparistoon
                              :arvio_vaikutuksista_vesistoon_sen_kayttoon
                              :arvio_ilmaan_joutuvien_paastojen_vaikutuksista
                              :arvio_vaikutuksista_maaperaan_pohjaveteen
                              :arvio_melun_tarinan_vaikutuksista
                              :arvio_ymparistovaikutuksista]
    :tarkkailu_raportointi [:kayttotarkkailu
                            :paastotarkkailu
                            :vaikutustarkkailu
                            :mittausmenetelmat_laitteet_laskentamenetelmat_laadunvarmistus
                            :raportointi_tarkkailuohjelmat]
    :vahinkoarvio_estavat_toimenpiteet [:toimenpiteet_vesistoon_kohdistuvien_vahinkojen_ehkaisemiseksi
                                        :korvausestiys_vesistoon_kohdistuvista_vahingoista
                                        :toimenpiteet_muiden_kuin_vesistovahinkojen_ehkaisemiseksi]
    :muut [:asemapiirros_prosessien_paastolahteiden_sijainti
           :ote_alueen_peruskartasta_sijainti_paastolahteet_olennaiset_kohteet
           :prosessikaavio_yksikkoprosessit_paastolahteet
           :selvitys_suuronnettomuuden_vaaran_arvioimiseksi
           :muu
           :paatos
           :paatosote
           :keskustelu]])

(def Maa-ainesluvat
  [:hakija [:valtakirja
            :ottamisalueen_omistus_hallintaoikeus]
   :ottamisalue [:ote_alueen_peruskartasta
                 :ote_yleiskaavasta
                 :ote_asemakaavasta
                 :naapurit]
   :erityissuunnitelmat [:yvalain_mukainen_arviointiselostus
                         :luonnonsuojelulain_arviointi
                         :kivenmurskaamo
                         :selvitys_jalkihoitotoimenpiteista
                         :ottamissuunnitelma
                         :kaivannaisjatteen_jatehuoltosuunnitelma]
   :muut [:vakuus_ottamisen_aloittamiseksi_ennen_luvan_lainvoimaa
          :vakuusasiakirja
          :selvitys_tieyhteyksista_oikeuksista
          :pohjavesitutkimus
          :muu
          :paatos
          :paatosote
          :keskustelu]])

(def Kiinteistotoimitus
  [:hakija [:valtakirja
            :virkatodistus
            :ote_kauppa_ja_yhdistysrekisterista]
   :kartat [:rasitesopimuksen_liitekartta
            :liitekartta]
   :kiinteiston_hallinta [:jaljennos_perunkirjasta
                          :rasitesopimus
                          :ote_asunto-osakeyhtion_kokouksen_poytakirjasta
                          :sukuselvitys
                          :testamentti
                          :saantokirja
                          :tilusvaihtosopimus
                          :jakosopimus
                          :kaupparekisteriote
                          :yhtiojarjestys
                          :ote_osakeyhtion_yhtiokokouksen_poytakirjasta
                          :ote_osakeyhtion_hallituksen_kokouksen_poytakirjasta]
   :muut [:muu
          :paatos
          :paatosote
          :keskustelu]])

(def MuutYmparistoluvat-extra
  [:muistomerkin-rauhoittaminen [:kirjallinen-aineisto
                                 :lainhuutotodistus
                                 :kauppakirja
                                 :valokuva-kohteesta
                                 :kohdekuvaus
                                 :selvitys-omistusoikeudesta]
   :jatteen_kerays [:vastaanottopaikan_tiedot]
   :kaytostapoistetun-oljy-tai-kemikaalisailion-jattaminen-maaperaan [:sailion-tarkastuspoytakirja
                                                                      :kiinteiston-omistajien-suostumus]
   :ottamisalue [:luettelo-naapureista-ja-asianosaisista]
   :koeluontoinen_toiminta [:kuvaus_toiminnasta
                            :raaka-aineet
                            :paasto_arviot
                            :selvitys_ymparistonsuojelutoimista]
   :laitosalue_sen_ymparisto [:kaavamaaraysote]
   :ilmoitus-poikkeuksellisesta-tilanteesta [:kayttoturvallisuustiedote]
   :yleiset-alueet [:suunnitelmakartta]
   :rakennuspaikka [:karttaote :ote_kiinteistorekisteristerista]
   :paapiirustus [:leikkauspiirros]
   :kartat [:yleiskartta
            :lantapatterin-sijainti
            :luonnonmuistomerkin-sijainti-kartalla
            :sailion-ja-rakenteiden-sijainti-kartalla
            :jatteen-sijainti
            :ottamispaikan-sijainti
            :sijaintikartta]
   :maastoliikennelaki-kilpailut-ja-harjoitukset [:asemapiirros-kilpailu-tai-harjoitusalueesta]
   :muut [:natura-arvioinnin-tarveharkinta
          :vakuus_ottamisen_aloittamiseksi_ennen_luvan_lainvoimaa
          :vakuusasiakirja
          :selvitys_tieyhteyksista_oikeuksista
          :pohjavesitutkimus
          :asemapiirros_prosessien_paastolahteiden_sijainti
          :kiinteiston-omistajan-suostumus-luvan-hakemiseen
          :muu
          :paatos
          :paatosote
          :keskustelu]])

;; MuuYmpäristö luvat contains yhteiskäsittely-maa-aines-ja-ymparistoluvalle operation that should have the all
;; attachemet types from maa-aines and ympärstolupa and some extra types
(def MuutYmparistoluvat
  (merge-attachment-listings
    MuutYmparistoluvat-extra
    Ymparistolupa
    Maa-ainesluvat))

(def VesihuoltoVapautushakemukset                           ; VVVL - Vesihuoltolain mukaiset vapautushakemukset
  [:vapautushakemukset [:analyysitulos-kaivovedesta
                        :asemapiirros
                        :varallisuusselvitys
                        :jatevesiselvitys]
   :muut [:muu
          :paatos
          :paatosote
          :keskustelu]])

;; NOTE: These are duplicated in Lupapiste attachment-multi-select.js,
;;       so if you edit these also edit that. Or refactor the duplication away!
(def types-not-transmitted-to-backing-system
  {:muut #{:paatos :paatosote :sopimus}
   :paatoksenteko #{:paatoksen_liite}})

(def types-marked-being-construction-time-attachments-by-permit-type
  {:R (merge (-> (apply hash-map Rakennusluvat-v2)
                 (select-keys [:erityissuunnitelmat]))
             {:suunnitelmat [:piha_tai_istutussuunnitelma]})})
