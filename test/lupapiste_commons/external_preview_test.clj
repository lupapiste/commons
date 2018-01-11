(ns lupapiste-commons.external-preview-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.external-preview :as ep]
            [clojure.java.io :as io]))

(def pdf "dev-resources/test-pdf.pdf")

(def muuntaja-url "http://fake-muuntaja.tech")

(deftest pdf-preview-is-generated-externally
  (with-open [input (io/input-stream (io/file pdf))]
    (with-redefs-fn
      {#'clj-http.client/post (fn [url opts]
                                (when (and (= url "http://fake-muuntaja.tech/pdf/pdf-preview")
                                           (= opts {:as        :stream
                                                    :multipart [{:name    "file"
                                                                 :content input
                                                                 :mime-type "application/pdf"}]}))
                                  {:body {:success true}}))}
      #(is (= (ep/create-preview input "application/pdf" muuntaja-url)
              {:success true})))))
