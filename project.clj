(defproject lupapiste/commons "5.3.19"
  :description "Common domain code and resources for lupapiste applications"
  :url "https://www.lupapiste.fi"
  :license {:name         "Eclipse Public License"
            :url          "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :scm {:url "https://github.com/lupapiste/commons.git"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/core.memoize "1.1.266"]
                 [dk.ative/docjure "1.18.0"]
                 [org.flatland/ordered "1.15.12"]
                 [prismatic/schema "1.4.1"]
                 [clj-http "3.12.3"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.26.0"]]
  :profiles {:dev      {:dependencies [[flare "0.2.9"]]
                        :injections   [(require 'flare.clojure-test)
                                       (flare.clojure-test/install!)]}
             :provided {:dependencies [[ring/ring-core "1.13.0"]
                                       [com.taoensso/timbre "6.5.0"]]}}
  :cljsbuild {:builds {:dev {:source-paths ["src"]}}})
