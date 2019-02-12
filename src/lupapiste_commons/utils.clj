(ns lupapiste-commons.utils
  (:import [java.util.jar JarFile Manifest]
           [java.net URL]
           [java.io InputStream]))

(defn get-build-info [jar-name]
  (or (when-first [^URL url (filter #(.contains (.toExternalForm ^URL %) jar-name)
                                    (enumeration-seq (.. (Thread/currentThread)
                                                         getContextClassLoader
                                                         (getResources JarFile/MANIFEST_NAME))))]
        (with-open [^InputStream in (.openStream url)]
          (into {} (for [[attr value] (.getAttributes (Manifest. in) "build-info")]
                     [(.toString attr) value]))))
      {}))
