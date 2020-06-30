(defproject lupapiste/commons "0.10.19"
  :description "Common domain code and resources for lupapiste applications"
  :url "https://www.evolta.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [org.clojure/core.memoize "0.7.1"]
                 [ontodev/excel "0.2.4" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.16"]
                 [org.flatland/ordered "1.5.7"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [prismatic/schema "1.1.7"]
                 [org.slf4j/slf4j-log4j12 "1.7.25"]
                 [org.apache.pdfbox/pdfbox "2.0.8"]
                 [org.apache.pdfbox/pdfbox-tools "2.0.8"]
                 [com.github.jai-imageio/jai-imageio-core "1.3.1"]
                 [com.github.jai-imageio/jai-imageio-jpeg2000 "1.3.0"]
                 [clj-http "3.8.0"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]
  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}
             :provided {:dependencies [[com.stuartsierra/component "0.3.2"]
                                       [ring/ring-core "1.6.3"]]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}})
