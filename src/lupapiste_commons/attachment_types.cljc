(ns lupapiste-commons.attachment-types)

(def osapuolet
  [:cv
   :patevyystodistus
   :paa_ja_rakennussuunnittelijan_tiedot
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
   :muut [:muu]])

(def Ymparistoilmoitukset
  [:kartat [:kartta-melun-ja-tarinan-leviamisesta]
   :muut [:muu]])

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
           :muu]])

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
          :selvitys_tieyhteyksista_oikeuksista
          :pohjavesitutkimus
          :muu]])

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
   :muut [:muu]])