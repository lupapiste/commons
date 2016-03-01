(ns lupapiste-commons.preview-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.preview :as preview]
            [clojure.java.io :as io])
  (:import (javax.imageio ImageIO)))

(def pdf-1 "dev-resources/test-pdf.pdf")
(def jpg-1 "resources/preview-sample-1.jpg")
(def image-1 (ImageIO/read (io/resource jpg-1)))

(deftest to-buffered-image-fails-quietly
  (is (nil? (#'preview/to-buffered-image nil "application/pdf")))
  (is (nil? (#'preview/to-buffered-image nil "image/png")))
  (is (nil? (#'preview/to-buffered-image nil "text/plain")))
  (is (nil? (#'preview/to-buffered-image nil "image/vnd.dwg"))))

(deftest pdf-to-buffered-image-works
  (is (= (.getWidth (#'preview/pdf-to-buffered-image pdf-1)) 1190)))

#_(deftest pdf-to-buffered-image-file
  (is (= (io/copy (preview/create-preview pdf-1 "application/pdf") (io/file "/tmp/a.jpg")) )))

(deftest scale-image-works
  (is (= (.getWidth (#'preview/scale-image image-1)) 428)))

(deftest buffered-image-to-input-stream-returns-correct-stream
  ;; NB: changing preview compression fails this test
  (is (= (.available (#'preview/buffered-image-to-input-stream image-1)) 667967)))

(deftest pdf-to-buffered-image-works-with-jbig2
  (is (= (.getWidth (#'preview/pdf-to-buffered-image "dev-resources/jbig2.pdf")) 450)))
