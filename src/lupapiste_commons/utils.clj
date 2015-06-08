(ns lupapiste-commons.utils)

(defn get-build-info [jar-name]
  (or (when-first [url (filter #(.contains (.toExternalForm %) jar-name)
                               (enumeration-seq (.. (Thread/currentThread)
                                                    getContextClassLoader
                                                    (getResources java.util.jar.JarFile/MANIFEST_NAME))))]
        (with-open [in (.openStream url)]
          (into {} (for [[attr value] (.getAttributes (java.util.jar.Manifest. in) "build-info")]
                     [(.toString attr) value]))))
      {}))
