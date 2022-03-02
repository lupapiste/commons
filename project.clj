(defproject lupapiste/commons "0.12.11"
  :description "Common domain code and resources for lupapiste applications"
  :url "https://www.lupapiste.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [org.clojure/core.memoize "1.0.253"]
                 [ontodev/excel "0.2.4" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.16"]
                 [org.flatland/ordered "1.15.10"]
                 [com.taoensso/timbre "5.1.2"]
                 [prismatic/schema "1.2.0"]
                 [com.github.jai-imageio/jai-imageio-core "1.4.0"]
                 [com.github.jai-imageio/jai-imageio-jpeg2000 "1.4.0"]
                 [clj-http "3.12.3"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]
  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}
             :provided {:dependencies [[com.stuartsierra/component "0.3.2"]
                                       [ring/ring-core "1.6.3"]]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}})
