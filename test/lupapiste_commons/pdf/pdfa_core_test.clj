(ns lupapiste-commons.pdf.pdfa-core-test
  (:require [lupapiste-commons.pdf.pdfa-core :as pdfa-core]
            [midje.sweet :refer :all]
            [taoensso.timbre :as timbre :refer [trace tracef debug debugf info infof warn warnf error errorf fatal fatalf]]
            [lupapiste-commons.pdf.pdfa-core :as pdfa]
            [clojure.java.io :as io])
  (:import (org.apache.pdfbox.pdmodel PDDocument)
           (java.io File FileOutputStream FileInputStream)
           (java.util List ArrayList)
           (com.lowagie.text.rtf.parser RtfParser)
           (com.lowagie.text Document)
           (com.lowagie.text.pdf PdfWriter)))


(facts "Valid embedded Fonts "
       (fact "default Font loads  " (.getPostscriptFontName (.getBaseFont (pdfa-core/font {:size 22}))) => "NimbusSanL-Regu")
       (fact "Given existing font loads" (.getPostscriptFontName (.getBaseFont (pdfa-core/font {:size 22 :ttf-name "fonts/n019003l.afm"}))) => "NimbusSanL-Regu")
       (fact "Given non-existing font throws exception " (.getPostscriptFontName (.getBaseFont (pdfa-core/font {:size 22 :ttf-name "fonts/n019003lXXXX.afm"}))) => (throws IllegalArgumentException))
       (fact "Given illegal font-file extension throws exception " (.getPostscriptFontName (.getBaseFont (pdfa-core/font {:size 22 :ttf-name "fonts/n019003l.txt"}))) => (throws IllegalArgumentException))
       )

(facts "Valid PDF/A metadata "
       (let [pdf-markup [{:title  "Lupapiste.fi"
                          :size   "a4"
                          :footer {:text  (clojure.string/join " - " ["Lupapiste.fi"
                                                                      "dd.MM.yyyy HH:mm" (str (System/currentTimeMillis))
                                                                      "application.export.page"])
                                   :align :right}
                          :pages  true}

                         ;;[:image {:xscale 1 :yscale 1} (ImageIO/read (io/resource "public/img/logo-v2-flat.png"))]
                         [:spacer]
                         [:heading {:style {:size 20}} "lang + tos"]
                         [:spacer]
                         [:line]
                         [:spacer]
                         [:list {:symbol "…"}
                          [:paragraph
                           "Rakentaminen, ylläpito ja käyttö (3)"
                           [:list
                            [:paragraph
                             "Rakennusvalvonta (0)"
                             [:list
                              [:paragraph
                               "Rakennuslupamenettely (1)"
                               [:list
                                [:paragraph
                                 "Neuvonta ()"
                                 [:list
                                  [:paragraph
                                   "Asiakkaan yhteydenotto asiointipalvelussa ()"
                                   [:list
                                    [:paragraph "todo localize: :ilmoitus" nil]]]]]
                                [:paragraph
                                 "Käsittelyssä ()"
                                 [:list
                                  [:paragraph
                                   "Hakemus jätetään käsittelyyn ()"
                                   [:list
                                    [:paragraph "todo localize: :hakemus" nil]
                                    [:paragraph
                                     "todo localize: :muut.muu"
                                     nil]]]]]]]]]]]]]
             file (File/createTempFile "test-pdf-core-" ".pdf")
             fis (FileOutputStream. file)]
         (with-open [out (io/output-stream file)]
           (pdfa/pdf pdf-markup out))
         (debug "  test file: " (.getAbsolutePath file))
         (let [doc (PDDocument/load file)
               info (.getDocumentInformation doc)]
           (fact "title" (.getPropertyStringValue info "Title") => "Lupapiste.fi")
           (fact "keywords" (.getKeywords info) => "Lupapiste.fi, XMP, Metadata")

           ;(debug "dc meta:                   "  (.getInputStreamAsString (.getMetadata (.getDocumentCatalog doc))))
           )
         #_(.delete file)))

#_(facts "rtf to pdf/a"
         (debug "env: " (System/getenv "os.name"))
         (System/setProperty "SystemRoot" "/usr/share")
         (.exec (Runtime/getRuntime) "env --unset \"BASH_FUNC_mc%%\"") ;

         (let [file (File. "/home/michaelho/Documents/Lounaslista 2015 vk46.rtf")
               fis (FileInputStream. file)
               parser (RtfParser. nil)
               doc (Document.)
               output-stream (FileOutputStream. "/tmp/test-rtf-to-pdf.pdf")
               pdf-writer (PdfWriter/getInstance doc output-stream)
               ]
           (.open doc)
           (.convertRtfDocument parser fis doc)
           (.close doc)
           )
         )

