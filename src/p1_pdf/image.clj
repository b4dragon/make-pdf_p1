(ns p1-pdf.image
  (:require ;; [me.raynes.fs :as fs]
            [exif-processor.core :as exif]
            [mikera.image.core :as img]))

(def image-extention #{"jpg" "png" "bmp" "jpeg"})

(defn support-image? [file]
  (let [filename (condp = (type file)
                    java.io.File (->> file
                                      (.getName))
                    java.lang.String file)
        extention (.substring filename (+ 1 (.lastIndexOf filename ".")))]
    (prn "supported? " (not (nil? (image-extention extention))) " -" (.getName file))
    (not (nil? (image-extention extention)))))

;; (defn make-file-list [dir]
  ;; (fs/list-dir dir))

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

(defn rotation [image-info]
  (let [rotate-angle (:rotation image-info)]
    (if (nil? rotate-angle)
      image-info
      (assoc image-info :image (img/rotate (:image image-info) (Integer/parseInt rotate-angle))))))

(defn resizing [image-info]
  (let [width (:width image-info)
        img (:image image-info)]
    (if (nil? width)
      image-info
      (assoc image-info :image (img/resize img width)))))

(defn image-load [file width]
  {:rotation (extract-rotation file)
   :width width
   :file file
   :image (-> file
              (.getPath)
              img/load-image)})
              
(defn make-image [file resize-width]
  (when (support-image? file)
    (-> file
        (image-load resize-width)
        rotation
        resizing)))
      

