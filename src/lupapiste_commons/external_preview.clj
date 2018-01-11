(ns lupapiste-commons.external-preview
  (:require [clj-http.client :as http]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [lupapiste-commons.preview :as preview])
  (:import [java.io InputStream]))

(def pdf-preview-path "/pdf/pdf-preview")

(defn placeholder-image-is []
  (io/input-stream (io/resource "no-preview-available.jpg")))

(defn- fetch-jpeg-thumbnail [muuntaja-url ^InputStream pdf-file]
  (try
    (let [request-opts {:as        :stream
                        :multipart [{:name      "file"
                                     :content   pdf-file
                                     :mime-type "application/pdf"}]}]
      (-> (http/post (str muuntaja-url pdf-preview-path)
                     request-opts)
          :body))
    (catch Exception ex
      (timbre/error ex "Could not generate preview in muuntaja - using placeholder image")
      (placeholder-image-is))))

(defn- ^InputStream pdf-to-jpeg-stream
  "Converts first page of the PDF to BufferedImage"
  [muuntaja-url pdf-input]
  (with-open [is (io/input-stream pdf-input)]
    (fetch-jpeg-thumbnail muuntaja-url is)))

(defn ^InputStream create-preview
  "Tries to create preview image. Uses external muuntaja / laundry server for PDFs and local JVM for images.
   Returns always an input stream with JPEG image content. If the preview generation fails, returns a placeholder image."
  [content content-type muuntaja-url]
  (if (= "application/pdf" content-type)
    (pdf-to-jpeg-stream muuntaja-url content)
    (try
      (or (preview/create-preview content content-type)
          (placeholder-image-is))
      (catch Throwable t
        (timbre/error t "Preview generation failed - using placeholder image")
        (preview/placeholder-image-is)))))
