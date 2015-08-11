(defproject lupapiste/commons "0.5.6-SNAPSHOT"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "https://deus.solita.fi/Solita/code/files/lupapiste/repositories/lupapiste-commons/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ontodev/excel "0.2.3" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.12"]
                 [org.flatland/ordered "1.5.3"]
                 [ring/ring-core "1.4.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [com.taoensso/timbre "4.0.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [prismatic/schema "0.4.3"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]

  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}}

  :deploy-repositories {"snapshots" {:url ***REMOVED***
                                     :username ***REMOVED***
                                     :password ***REMOVED***}
                        "releases" {:url ***REMOVED***
                                    :username ***REMOVED***
                                    :password ***REMOVED***
                                    :sign-releases false}})
