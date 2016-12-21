(ns lupapiste-commons.preview-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.preview :as preview]
            [clojure.java.io :as io])
  (:import (javax.imageio ImageIO)))

(def pdf-1 "dev-resources/test-pdf.pdf")
(def jpg-1 "resources/preview-sample-1.jpg")
(def jpg-2 "resources/preview-sample-2.jpg")
(def image-1 (ImageIO/read (io/resource jpg-1)))
(def image-2 (ImageIO/read (io/resource jpg-2)))

(deftest to-buffered-image-fails-quietly
  (is (nil? (#'preview/to-buffered-image nil "application/pdf")))
  (is (nil? (#'preview/to-buffered-image nil "image/png")))
  (is (nil? (#'preview/to-buffered-image nil "text/plain")))
  (is (nil? (#'preview/to-buffered-image nil "image/vnd.dwg"))))

(deftest pdf-to-buffered-image-works
  (let [img (#'preview/pdf-to-buffered-image pdf-1)]
    (is (= (.getHeight img) 600))
    (is (= (.getWidth img) 424))))

(deftest scale-image-works
  (is (= (.getWidth (#'preview/scale-image image-1)) 424)))

(deftest image-cropping-works
  (let [scaled (#'preview/scale-image image-2)]
    (is (= (.getWidth scaled) 419))
    (is (= (.getHeight scaled) 600))))

(deftest buffered-image-to-input-stream-returns-correct-stream
  ;; NB: changing preview compression fails this test
  (is (= (.available (#'preview/buffered-image-to-input-stream image-1)) 811937)))

(deftest pdf-to-buffered-image-works-with-jbig2
  (is (= (.getWidth (#'preview/pdf-to-buffered-image "dev-resources/jbig2.pdf")) 600)))

(deftest pdf-to-buffered-image-works-with-jpeg2000
  (is (= (.getWidth (#'preview/pdf-to-buffered-image "dev-resources/jpeg2000.pdf")) 600)))
