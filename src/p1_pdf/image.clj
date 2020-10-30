(ns p1-pdf.image
  (:require [me.raynes.fs :as fs]
            [exif-processor.core :as exif]
            [mikera.image.core :as img]))

(def image-extention #{"jpg" "png" "bmp" "jpeg"})

(defn support-image? [file]
  (let [filename (condp = (type file)
                    java.io.File (->> file
                                      (.getName))
                    java.lang.String file)
        extention (.substring filename (+ 1 (.lastIndexOf filename ".")))]
    (not (nil? (image-extention extention)))))

(defn make-file-list [dir]
  (fs/list-dir dir))

(defn extract-rotation [file]
  (let [orientation (-> file
                        (.getPath)
                        exif/exif-for-filename
                        (get "Orientation"))]
    (if (nil? orientation)
      nil
      (->> orientation
           (re-find #"Rotate\s+(\d+)\s+CW")
           second))))

(defn rotate [image rotation]
  (if (nil? rotation)
    image
    (img/rotate image (Integer/parseInt rotation))))

(defn make-image [file]
  (when (support-image? file)
    (let [path (.getPath file)
          rotation (extract-rotation file)
          img (img/load-image path)]
      (rotate img rotation))))
