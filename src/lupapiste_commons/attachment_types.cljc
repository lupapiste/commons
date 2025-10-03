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
   :tutkintotodistus
   :suostumus])

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
                 :ympariston_tietomalli_BIM
                :arkkitehtimalli_BIM
                 :rakennemalli_BIM
                 :talotekniikkamalli_BIM
                 :rakennuspaikkamalli_BIM]
   :tietomallien_liitteet [:tarkastustulos
                           :tarkastustulos_bcf
                           :tarkastusraportti
                           :muu_tietomallin_liite]
   :muut [:erityismenettelyasiakirja
          :keskustelu
          :kuvaus
          :luonnos
          :piirustusluettelo
          :sijoituslupaasiakirja
          :suorituskyvyttomyysvakuusasiakirja
          :tutkimus
          :vakuusasiakirja
          :kuulutusasiakirja
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
   :katselmukset_ja_tarkastukset [:katselmuksen_tai_tarkastuksen_poytakirja
                                  :loppukatselmuksen_poytakirja]
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
   :katselmukset_ja_tarkastukset [:katselmuksen_tai_tarkastuksen_poytakirja
                                  :loppukatselmuksen_poytakirja]
   :ennakkoluvat_ja_lausunnot [:lausunto]
   :muut [:muu
          :paatos
          :paatosote
          :sopimus
          :keskustelu]])

(def Ymparisto-types
  "Attachment types for YM, YI, YL, VVVL and MAL"
  [:ilmoitus-poikkeuksellisesta-tilanteesta [:kayttoturvallisuustiedote]
   :jatteen_kerays [:vastaanottopaikan_tiedot]
   :maastoliikennelaki-kilpailut-ja-harjoitukset [:asemapiirros-kilpailu-tai-harjoitusalueesta]
   :paapiirustus [:leikkauspiirros
                  :asemapiirros]
   :ilmoituslomakkeet [:ilmoituslomake]
   :erityissuunnitelmat [:yvalain_mukainen_arviointiselostus
                         :luonnonsuojelulain_arviointi
                         :kivenmurskaamo
                         :selvitys_jalkihoitotoimenpiteista
                         :ottamissuunnitelma
                         :kaivannaisjatteen_jatehuoltosuunnitelma]
   :tarkkailu_raportointi [:kayttotarkkailu
                           :paastotarkkailu
                           :vaikutustarkkailu
                           :mittausmenetelmat_laitteet_laskentamenetelmat_laadunvarmistus
                           :raportointi_tarkkailuohjelmat]
   :vahinkoarvio_estavat_toimenpiteet [:toimenpiteet_vesistoon_kohdistuvien_vahinkojen_ehkaisemiseksi
                                       :korvausestiys_vesistoon_kohdistuvista_vahingoista
                                       :toimenpiteet_muiden_kuin_vesistovahinkojen_ehkaisemiseksi]
   :kartat [:yleiskartta
            :lantapatterin-sijainti
            :luonnonmuistomerkin-sijainti-kartalla
            :sailion-ja-rakenteiden-sijainti-kartalla
            :jatteen-sijainti
            :ottamispaikan-sijainti
            :sijaintikartta
            :kartta-melun-ja-tarinan-leviamisesta]
   :laitoksen_tiedot [:voimassa_olevat_ymparistolupa_vesilupa
                      :muut_paatokset_sopimukset
                      :selvitys_ymparistovahinkovakuutuksesta]
   :vapautushakemukset [:analyysitulos-kaivovedesta
                        :asemapiirros
                        :varallisuusselvitys
                        :jatevesiselvitys]
   :osapuolet [:yhdistyksen-johtoryhman-poytakirja
               :yhdistyksen-saannot]
   :yleiset-alueet [:suunnitelmakartta]
   :rakennuspaikka [:karttaote
                    :ote_kiinteistorekisteristerista]
   :kaytostapoistetun-oljy-tai-kemikaalisailion-jattaminen-maaperaan [:sailion-tarkastuspoytakirja
                                                                      :kiinteiston-omistajien-suostumus]
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
   :vaikutukset_ymparistoon [:arvio_vaikutuksista_yleiseen_viihtyvyyteen_ihmisten_terveyteen
                             :arvio_vaikutuksista_luontoon_luonnonsuojeluarvoihin_rakennettuun_ymparistoon
                             :arvio_vaikutuksista_vesistoon_sen_kayttoon
                             :arvio_ilmaan_joutuvien_paastojen_vaikutuksista
                             :arvio_vaikutuksista_maaperaan_pohjaveteen
                             :arvio_melun_tarinan_vaikutuksista
                             :arvio_ymparistovaikutuksista]
   :koeluontoinen_toiminta [:kuvaus_toiminnasta
                            :raaka-aineet
                            :paasto_arviot
                            :selvitys_ymparistonsuojelutoimista]
   :paras_tekniikka_kaytanto [:arvio_tekniikan_soveltamisesta
                              :arvio_paastojen_vahentamistoimien_ristikkaisvaikutuksista
                              :arvio_kaytannon_soveltamisesta]
   :muistomerkin-rauhoittaminen [:kirjallinen-aineisto
                                 :lainhuutotodistus
                                 :kauppakirja
                                 :valokuva-kohteesta
                                 :kohdekuvaus
                                 :selvitys-omistusoikeudesta]
   :ottamisalue [:ote_alueen_peruskartasta
                 :ote_yleiskaavasta
                 :ote_asemakaavasta
                 :naapurit
                 :luettelo-naapureista-ja-asianosaisista]
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
   :muut [:vakuus_ottamisen_aloittamiseksi_ennen_luvan_lainvoimaa
          :vakuusasiakirja
          :selvitys_tieyhteyksista_oikeuksista
          :pohjavesitutkimus
          :muu
          :paatos
          :paatosote
          :keskustelu
          :natura-arvioinnin-tarveharkinta
          :asemapiirros_prosessien_paastolahteiden_sijainti
          :kiinteiston-omistajan-suostumus-luvan-hakemiseen
          :sopimus
          :haittapaivakirja
          :valokuva
          :rakennekuva
          :selvitys-jatteen-haitta-ainepitoisuuksista
          :selvitys-poikkeuksellisista-saaoloista
          :tiedot-levitettavasta-lannasta
          :ote_alueen_peruskartasta_sijainti_paastolahteet_olennaiset_kohteet
          :prosessikaavio_yksikkoprosessit_paastolahteet
          :selvitys_suuronnettomuuden_vaaran_arvioimiseksi]
   :hakija [:valtakirja
            :ottamisalueen_omistus_hallintaoikeus]
   :laitosalue_sen_ymparisto [:kaavamaaraysote
                              :tiedot_kiinteistoista
                              :tiedot_toiminnan_sijaintipaikasta
                              :kaavaote
                              :selvitys_pohjavesialueesta
                              :selvitys_rajanaapureista
                              :ote_asemakaavasta
                              :ote_yleiskaavasta]])

(def Ymparisto-types-v2
  "V2 of attachment types for YM, YI, YL, VVVL and MAL.
  Note! Order of group types is significant, see `ymparisto-type-group->ordinal`."
  [:hakija [:kauppakirja
            :selvitys-omistus-tai-hallintaoikeudesta
            :valtakirja]
   :hakemukset-ja-ilmoitukset [:asianosaisen-kuuleminen
                               :elainsuojelutaulukot
                               :hakemus
                               :ilmoituslomake
                               :kuuleminen
                               :kuulutus
                               :lausunto
                               :lausuntopyynto
                               :liite-vahimmaisetaisyydesta-poikkeamisesta
                               :liitelomake
                               :mielipide
                               :muistio
                               :muistutus
                               :poytakirjaote
                               :rekisterointi-ilmoitus
                               :selvitys
                               :selvityspyynto
                               :suostumus
                               :taydennyspyynto
                               :vastine
                               :vastinepyynto]
   :kartat-ja-piirustukset [:asemapiirros
                            :kartta-melun-ja-tarinan-leviamisesta
                            :karttaote
                            :leikkauskuvat
                            :maankaatopaikan-tayttosuunnitelma
                            :ote-asemakaavasta
                            :ote-maakuntakaavasta
                            :ote-peruskartasta
                            :ote-yleiskaavasta
                            :pohjapiirustus
                            :prosessikaavio-yksikkoprosessit-ja-paastolahteet
                            :rakennekuva
                            :sijaintikartta]
   :vahinkoarvio_estavat_toimenpiteet [:korvausestiys_vesistoon_kohdistuvista_vahingoista
                                       :toimenpiteet_muiden_kuin_vesistovahinkojen_ehkaisemiseksi
                                       :toimenpiteet_vesistoon_kohdistuvien_vahinkojen_ehkaisemiseksi]
   :muut-luvat-tai-sopimukset [:muut-paatokset-ja-sopimukset
                               :tiedot-ymparistovahinkovakuutuksesta
                               :voimassa-oleva-ymparistolupa-tai-vesilupa]
   :ymparistokuormitus [:hajumittaus-tai-mallinnus
                        :jatteen_hyodyntamista_kasittelya_koskevan_toiminnan_lisatiedot
                        :luontoselvitys
                        :maankaatopaikkaa-koskevan-lupahakemuksen-lisatiedot
                        :melumittaus-tai-mallinnus
                        :melupaastot_tarina
                        :paastot_ilmaan
                        :paastot_maaperaan_pohjaveteen
                        :paastot_vesistoon_viemariin
                        :polymittaus-tai-mallinnus
                        :selvitys_jatteiden_maaran_haitallisuuden_vahentamiseksi
                        :selvitys_paastojen_vahentamisesta_puhdistamisesta
                        :selvitys_vakavaraisuudesta_vakuudesta
                        :syntyvat_jatteet
                        :tiedot_pilaantuneesta_maaperasta]
   :vaikutukset_ymparistoon [:arvio_ilmaan_joutuvien_paastojen_vaikutuksista
                             :arvio_melun_tarinan_vaikutuksista
                             :arvio_vaikutuksista_luontoon_luonnonsuojeluarvoihin_rakennettuun_ymparistoon
                             :arvio_vaikutuksista_maaperaan_pohjaveteen
                             :arvio_vaikutuksista_vesistoon_sen_kayttoon
                             :arvio_vaikutuksista_yleiseen_viihtyvyyteen_ihmisten_terveyteen
                             :arvio_ymparistovaikutuksista]
   :paras-tekniikka-ja-paras-kaytanto [:arvio-tekniikan-ja-kaytannon-soveltamisesta]
   :laitoksen_toiminta [:arvio_ymparistoriskeista
                        :energiansaastosopimus
                        :kayttoturvallisuustiedote
                        :kemikaaliluettelo
                        :liikenne_liikennejarjestelyt
                        :selvitys_tuotannosta
                        :selvitys_ymparistoasioiden_hallintajarjestelmasta
                        :tiedot_energian
                        :tiedot_raaka-aineista
                        :tiedot_toiminnan_suunnitellusta
                        :vedenhankinta_viemarointi
                        :yleiskuvaus_toiminnasta
                        :yleisolle_tarkoitettu_tiivistelma]
   :laitosalue_sen_ymparisto [:selvitys_pohjavesialueesta
                              :selvitys_rajanaapureista
                              :tiedot_kiinteistoista
                              :tiedot_toiminnan_sijaintipaikasta]
   :jatteen_kerays [:vastaanottopaikan_tiedot]
   :kaytostapoistetun-oljy-tai-kemikaalisailion-jattaminen-maaperaan [:sailion-tarkastuspoytakirja]
   :koeluontoinen_toiminta [:kuvaus_toiminnasta
                            :paasto_arviot
                            :raaka-aineet
                            :selvitys_ymparistonsuojelutoimista]
   :vapautus-ja-poikkeushakemusten-liitteet [:analyysitulokset-kaivovedesta
                                             :selvitys-elamantilanteesta
                                             :selvitys-jatevesien-vahaisyydesta
                                             :selvitys-jatevesijarjestelmasta
                                             :selvitys-liittamisesta-viemariverkkoon
                                             :selvitys-maaperan-soveltuvuudesta-imeytykseen
                                             :selvitys-varallisuudesta]
   :erityissuunnitelmat [:kaivannaisjatteen_jatehuoltosuunnitelma
                         :kivenmurskaamo
                         :luonnonsuojelulain-mukainen-natura-arviointi
                         :luonto-ja-maisemaselvitys
                         :ottamissuunnitelma
                         :pohjavesiselvitys
                         :selvitys-suuronnettomuuden-vaaran-arvioimiseksi
                         :selvitys-tieyhteyksista-ja-oikeuksista
                         :selvitys_jalkihoitotoimenpiteista
                         :yvalain_mukainen_arviointiselostus]
   :tarkkailu_raportointi [:kayttotarkkailu
                           :mittausmenetelmat_laitteet_laskentamenetelmat_laadunvarmistus
                           :paastotarkkailu
                           :raportointi_tarkkailuohjelmat
                           :raportti
                           :vaikutustarkkailu]
   :muut [:jakelu
          :keskustelu
          :muu
          :paatos
          :paatoksen_tiedoksianto
          :kuulutusasiakirja
          :tarkastuspoytakirja
          :valokuva
          :lausunto_valituksesta
          :tuomioistuimen_lausuntopyynto
          :tuomioistuimen_paatos]])

(def ymparisto-type-group->ordinal (->> Ymparisto-types-v2
                                        (partition 2)
                                        (map-indexed (fn [idx [group _types]] [group idx]))
                                        (into {})))

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

;; NOTE: These are duplicated in Lupapiste attachment-multi-select.js,
;;       so if you edit these also edit that. Or refactor the duplication away!
(def types-not-transmitted-to-backing-system
  {:muut #{:paatos :paatosote :sopimus}
   :paatoksenteko #{:paatoksen_liite}})

(def types-marked-being-construction-time-attachments-by-permit-type
  {:R (merge (-> (apply hash-map Rakennusluvat-v2)
                 (select-keys [:erityissuunnitelmat]))
             {:suunnitelmat [:piha_tai_istutussuunnitelma]})})

(def all-attachment-types
  (merge-attachment-listings
    Allu
    Kiinteistotoimitus
    Rakennusluvat-v2
    YleistenAlueidenLuvat-v2
    Ymparisto-types ; For backward compatibility
    Ymparisto-types-v2))

(def model-phases
  "Phase of application that a BIM model is tied to; basically the model of the building
  before (submitted plan), during (amended plan) or after (actually completed plan) construction.
  The list is in chronological order when considering a single model."
  [:submission-time :construction-time :completion-time])
