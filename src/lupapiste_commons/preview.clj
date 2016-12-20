(ns lupapiste-commons.preview
  (:require [taoensso.timbre :refer [debugf warnf]]
            [clojure.java.io :as io])
  (:import (org.apache.pdfbox.pdmodel PDDocument)
           (org.apache.pdfbox.io MemoryUsageSetting)
           (org.apache.pdfbox.tools.imageio ImageIOUtil)
           (java.awt.image BufferedImage)
           (java.awt RenderingHints)
           (java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream File InputStream)
           (javax.imageio ImageIO)
           (org.apache.pdfbox.rendering PDFRenderer)))

(def rez 600.0)
; The aspect ratio should be about sqrt(2) or less, i.e. A paper series aspect ratio is OK
(def min-aspect 0.7)
(def max-aspect 1.45)

(defn- buffered-image-to-input-stream
  "Converts BufferedImage inputStream"
  [^BufferedImage image]
  (let [output (ByteArrayOutputStream.)]
    (ImageIOUtil/writeImage image "jpg" output 72 0.5)
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
          scale (min (/ rez (- original-width crop-x)) (/ rez (- original-height crop-y)))
          width (* scale (- original-width crop-x))
          height (* scale (- original-height crop-y))
          new-image (BufferedImage. width height BufferedImage/TYPE_INT_RGB)]
      (debugf "scale-image rez: %s x %s, crop: %s x %s, scale: %s" original-width original-height crop-x crop-y scale)
      (doto (.createGraphics new-image)
        (.setRenderingHint RenderingHints/KEY_INTERPOLATION, RenderingHints/VALUE_INTERPOLATION_BICUBIC)
        (.setRenderingHint RenderingHints/KEY_RENDERING, RenderingHints/VALUE_RENDER_QUALITY)
        (.setRenderingHint RenderingHints/KEY_ANTIALIASING, RenderingHints/VALUE_ANTIALIAS_ON)
        (.drawImage image, 0, 0, width, height, crop-x, crop-y, original-width, original-height, nil)
        (.dispose))
      new-image)))

(defn- ^BufferedImage pdf-to-buffered-image
  "Converts 1. page from PDF to BufferedImage"
  [pdf-input]
  (let [input (if (= (type pdf-input) String)
                (FileInputStream. pdf-input)
                pdf-input)]
    (with-open [document (PDDocument/load input (MemoryUsageSetting/setupMixed (* 100 1024 1024)))]
      (let [crop-box (-> (.getPage document 0) (.getCropBox))
            original-width (.getWidth crop-box)
            original-height (.getHeight crop-box)
            ; If the image is too wide / high, we have to crop it to a more manageable aspect ratio
            ; and because the PDFRenderer does not directly support this, we first render in 2 x target resolution
            ; and then scale the image down to the final target size (with cropping)
            crop? (must-crop? original-width original-height)
            target-rez (if crop? (* 2 rez) rez)
            scale (->> (max original-width original-height) (/ target-rez) float)]
        (debugf "scale for pdf preview: %s" scale)
        (cond-> (-> (PDFRenderer. document) (.renderImage 0 scale))
                crop? scale-image)))))

(defn- ^BufferedImage raster-to-buffered-image
  "Converts Raster image to BufferedImage"
  [input]
  (-> (ImageIO/read ^InputStream (io/input-stream input))
      scale-image))

(defn converter [content-type]
  (cond
    (= "application/pdf" content-type) pdf-to-buffered-image
    (re-matches (re-pattern "(image/(gif|jpeg|png|tiff))") content-type) raster-to-buffered-image))

(defn- ^BufferedImage to-buffered-image
  "Tries to read content to image by JAI or apache.pdfbox. Retuns nil on fail"
  [content content-type]
  (try
    (when-let [op (converter content-type)]
      (op content))
    (catch Exception e (warnf "preview to-buffered-image was unable to read content of a %s file: %s" content-type e))))

(defn create-preview
  "Tries to create preview image IF content type can be processed to image by JAI or apache.pdfbox. Retuns nil on fail"
  [content content-type]
  (some-> (to-buffered-image content content-type)
          buffered-image-to-input-stream))

(defn placeholder-image [] (ByteArrayInputStream. (byte-array 0)))
