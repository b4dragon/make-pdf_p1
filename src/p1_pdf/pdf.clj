(ns p1-pdf.pdf
  (:require [clj-pdf.core :as pdf]
            [p1-pdf.image :as img]))

(def image-fmt {:xscale 0.9
                :yscale 0.9
                :align :center})

(defn build [pdf-name images]
  (pdf/pdf [{}
            (for [image images]
              [:image image-fmt image])]
           (str pdf-name ".pdf")))


(defn make-pdf [files filename]
  (future
  (->> files
       (map img/make-image)
       (remove nil?)
       (build filename))))
