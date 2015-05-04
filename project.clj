(defproject lupapiste/commons "0.1.0-SNAPSHOT"
  :description "Common domain code and resources for lupapiste and lupapiste-toj"
  :url "http://www.solita.fi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:url "https://deus.solita.fi/Solita/code/files/lupapiste/repositories/lupapiste-commons/"}
  :dependencies [[org.clojure/clojure "1.6.0"]]

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
