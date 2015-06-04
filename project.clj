(defproject lupapiste/commons "0.4.0-SNAPSHOT"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "https://deus.solita.fi/Solita/code/files/lupapiste/repositories/lupapiste-commons/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ontodev/excel "0.2.3" :exclusions [xml-apis org.apache.poi/poi-ooxml]]
                 [org.apache.poi/poi-ooxml "3.11"]
                 [org.flatland/ordered "1.5.3"]
                 [ring/ring-core "1.3.2"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.8.0"]]

  :profiles {:dev {:dependencies [[flare "0.2.9"]]
                   :injections [(require 'flare.clojure-test)
                                (flare.clojure-test/install!)]}}

  ;; Offer portable source as .cljc in the jar file
  :filespecs [{:type :fn
               :fn (fn [p]
                     (let [portable-source "src/lupapiste_commons/attachment_types.clj"]
                       {:type :bytes
                        :path (str (.replaceFirst portable-source "^src/" "") "c")
                        :bytes (slurp portable-source)}))}]

  :deploy-repositories {"snapshots" {:url ***REMOVED***
                                     :username ***REMOVED***
                                     :password ***REMOVED***}
                        "releases" {:url ***REMOVED***
                                    :username ***REMOVED***
                                    :password ***REMOVED***
                                    :sign-releases false}})
