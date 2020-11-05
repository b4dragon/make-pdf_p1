(ns p1-pdf.pdf
  (:require [clj-pdf.core :as pdf]
            [p1-pdf.image :as img]))
  
(def setting (read-string (slurp "setting.edn")))

(defn build [pdf-name images]
    (pdf/pdf [(:default setting)
              (for [image images]
                [:image (:image setting) (:image image)])]
             pdf-name))
  
(defn make-pdf [files filename]
  (let [img-width (get-in setting [:image-quality :image-width])]
    (->> files
         (map #(img/make-image % img-width))
         (remove nil?)
         (build filename))))
         
