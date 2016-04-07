(ns lupapiste-commons.route-utils
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defonce index-response (atom nil))

(defn process-index-response [{:strs [git-commit]}]
  (if @index-response
    @index-response
    (with-open [is (-> (io/resource "public/index.html")
                       (io/input-stream))]
      (let [content (slurp is)
            version (if git-commit (subs git-commit 0 7) "dev")
            replaced-html (-> (string/replace content ".css" (str ".css?v=" version))
                              (string/replace ".js" (str ".js?v=" version)))
            response {:status 200
                      :body replaced-html
                      :headers {"Content-Type" "text/html"
                                "Content-Length" (str (count replaced-html))
                                "Cache-Control" "no-cache, no-store, must-revalidate"
                                "Pragma" "no-cache"
                                "Expires" "0"}}]
        (reset! index-response response)
        response))))
