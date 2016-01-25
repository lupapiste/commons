(defproject lupapiste/commons "0.6.0"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ontodev/excel "0.2.4" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.13"]
                 [org.flatland/ordered "1.5.3"]
                 [ring/ring-core "1.4.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.taoensso/timbre "4.2.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [prismatic/schema "1.0.4"]
                 [org.slf4j/slf4j-log4j12 "1.7.14"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]

  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}}
)
