(ns lupapiste-commons.preview
  (:require [taoensso.timbre :refer [debugf warnf]]
            [clojure.java.io :as io])
  (:import (java.awt.image BufferedImage)
           (java.awt RenderingHints)
           (java.io ByteArrayOutputStream ByteArrayInputStream InputStream)
           (javax.imageio ImageIO ImageWriter ImageWriteParam IIOImage)
           [javax.imageio.stream ImageOutputStream]))

(def rez 600.0)
; The aspect ratio should be about sqrt(2) or less, i.e. A paper series aspect ratio is OK
(def min-aspect 0.7)
(def max-aspect 1.45)

(defn- write-jpeg [^BufferedImage image output]
  (let [^ImageWriter writer    (.next (ImageIO/getImageWritersByFormatName "jpeg"))
        ^ImageWriteParam param (doto (.getDefaultWriteParam writer)
                                 (.setCompressionMode ImageWriteParam/MODE_EXPLICIT)
                                 (.setCompressionQuality (float 0.6)))]
    (with-open [^ImageOutputStream ios (ImageIO/createImageOutputStream output)]
      (.setOutput writer ios)
      (.write writer nil (IIOImage. image nil nil) param)
      (.dispose writer))))

(defn- buffered-image-to-input-stream
  "Converts BufferedImage inputStream"
  [^BufferedImage image]
  (let [output (ByteArrayOutputStream.)]
    (write-jpeg image output)
    (ByteArrayInputStream. (.toByteArray output))))

(defn- size-ok? [^BufferedImage image]
  (if (and (< (.getWidth image) 45000) (< (.getHeight image) 45000))
    true
    (do
      (warnf "Image size (%d x %d) is too big for preview [byte array length exceeds MAX_INTEGER]" (.getWidth image) (.getHeight image))
      false)))

(defn- must-crop? [width height]
  (not (< min-aspect (/ width height) max-aspect)))

(defn- crop-amount [x y]
  (if (< (/ x y) min-aspect)
    (int (- y (/ x min-aspect)))
    0))

(defn- ^BufferedImage scale-image
  "Crops and scales BufferedImage to predefined resolution"
  [^BufferedImage image]
  (when (size-ok? image)
    (let [original-width (.getWidth image)
          original-height (.getHeight image)
          crop-x (crop-amount original-height original-width)
          crop-y (crop-amount original-width original-height)
          cropped-width (- original-width crop-x)
          cropped-height (- original-height crop-y)
          scale (min (/ rez cropped-width) (/ rez cropped-height))
          width (* scale cropped-width)
          height (* scale cropped-height)
          new-image (BufferedImage. width height BufferedImage/TYPE_INT_RGB)]
      (debugf "scale-image rez: %s x %s, crop: %s x %s, scale: %s" original-width original-height crop-x crop-y scale)
      (doto (.createGraphics new-image)
        (.setRenderingHint RenderingHints/KEY_INTERPOLATION, RenderingHints/VALUE_INTERPOLATION_BICUBIC)
        (.setRenderingHint RenderingHints/KEY_RENDERING, RenderingHints/VALUE_RENDER_QUALITY)
        (.setRenderingHint RenderingHints/KEY_ANTIALIASING, RenderingHints/VALUE_ANTIALIAS_ON)
        (.drawImage image, 0, 0, width, height, crop-x, crop-y, original-width, original-height, nil)
        (.dispose))
      new-image)))

(defn- ^BufferedImage raster-to-buffered-image
  "Converts Raster image to BufferedImage"
  [input]
  (-> (ImageIO/read ^InputStream (io/input-stream input))
      scale-image))

(defn converter [content-type]
  (when (re-matches (re-pattern "(image/(gif|jpeg|png|tiff))") content-type)
    raster-to-buffered-image))

(defn- ^BufferedImage to-buffered-image
  "Tries to read content to image by JAI. Returns nil on fail"
  [content content-type]
  (try
    (when-let [op (converter content-type)]
      (op content))
    (catch Exception e (warnf "preview to-buffered-image was unable to read content of a %s file: %s" content-type e))))

(defn create-preview
  "Tries to create preview image IF content type can be processed to image by JAI. Returns nil on fail"
  [content content-type]
  (some-> (to-buffered-image content content-type)
          buffered-image-to-input-stream))

(defn placeholder-image-is []
  (io/input-stream (io/resource "no-preview-available.jpg")))
