(defproject lupapiste/commons "0.7.47"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "European Union Public License"
            :url "https://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [ontodev/excel "0.2.4" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.14"]
                 [org.flatland/ordered "1.5.4"]
                 [ring/ring-core "1.5.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.taoensso/timbre "4.2.1"]
                 [prismatic/schema "1.1.3"]
                 [org.slf4j/slf4j-log4j12 "1.7.21"]
                 [org.apache.pdfbox/pdfbox "2.0.2"]
                 [org.apache.pdfbox/pdfbox-tools "2.0.2"]
                 [com.github.jai-imageio/jai-imageio-core "1.3.1"]
                 [com.github.jai-imageio/jai-imageio-jpeg2000 "1.3.0"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]
  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}})
