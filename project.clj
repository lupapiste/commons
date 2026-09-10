(defproject lupapiste/commons "5.4.1"
  :description "Common domain code and resources for lupapiste applications"
  :url "https://www.lupapiste.fi"
  :license {:name         "Eclipse Public License"
            :url          "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.12.6"]
                 [org.clojure/core.memoize "1.2.281"]
                 [dk.ative/docjure "1.22.0" :exclusions [org.apache.logging.log4j/log4j-api
                                                         org.apache.poi/poi-ooxml
                                                         org.apache.poi/poi]]
                 [org.apache.poi/poi-ooxml "5.5.1"]
                 [org.apache.poi/poi "5.5.1"]
                 [org.apache.logging.log4j/log4j-api "2.26.1"]
                 [org.flatland/ordered "1.15.12"]
                 [prismatic/schema "1.4.2"]
                 [metosin/schema-tools "0.14.0"]
                 [clj-http "3.13.1"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.26.0"]]
  :profiles {:dev      {:dependencies [[flare "0.2.9"]]
                        :injections   [(require 'flare.clojure-test)
                                       (flare.clojure-test/install!)]}
             :provided {:dependencies [[ring/ring-core "1.15.5"]
                                       [com.taoensso/timbre "6.8.0"]]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}})
