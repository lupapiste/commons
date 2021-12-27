(ns lupapiste-commons.preview-test
  (:require [clojure.test :refer :all]
            [lupapiste-commons.preview :as preview]
            [clojure.java.io :as io])
  (:import (javax.imageio ImageIO)))

(def jpg-1 "resources/preview-sample-1.jpg")
(def jpg-2 "resources/preview-sample-2.jpg")
(def image-1 (ImageIO/read (io/resource jpg-1)))
(def image-2 (ImageIO/read (io/resource jpg-2)))

(deftest to-buffered-image-fails-quietly
  (is (nil? (#'preview/to-buffered-image nil "application/pdf")))
  (is (nil? (#'preview/to-buffered-image nil "image/png")))
  (is (nil? (#'preview/to-buffered-image nil "text/plain")))
  (is (nil? (#'preview/to-buffered-image nil "image/vnd.dwg"))))

(deftest scale-image-works
  (is (= (.getWidth (#'preview/scale-image image-1)) 424)))

(deftest image-cropping-works
  (let [scaled (#'preview/scale-image image-2)]
    (is (= (.getWidth scaled) 419))
    (is (= (.getHeight scaled) 600))))

(deftest buffered-image-to-input-stream-returns-correct-stream
  ;; NB: changing preview compression fails this test
  (is (= (.available (#'preview/buffered-image-to-input-stream image-1)) 811937)))
