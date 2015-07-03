(defproject lupapiste/commons "0.5.1"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "https://deus.solita.fi/Solita/code/files/lupapiste/repositories/lupapiste-commons/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ontodev/excel "0.2.3" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.11"]
                 [org.flatland/ordered "1.5.3"]
                 [ring/ring-core "1.3.2"]
                 [com.stuartsierra/component "0.2.3"]
                 [com.taoensso/timbre "3.4.0"]
                 [org.clojure/tools.nrepl "0.2.10"]]
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
