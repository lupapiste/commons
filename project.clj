(defproject lupapiste/commons "0.5.43"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ontodev/excel "0.2.3" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.12"]
                 [org.flatland/ordered "1.5.3"]
                 [ring/ring-core "1.4.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [com.taoensso/timbre "4.1.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [prismatic/schema "1.0.4"]
                 [org.slf4j/slf4j-log4j12 "1.7.7"]]
  :plugins [[lein-midje "3.1.1"]
            [com.jakemccrary/lein-test-refresh "0.8.0"]]

  :profiles {:dev {:dependencies [[midje "1.7.0" :exclusions [org.clojure/tools.namespace]]
                                  [flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}}
)
