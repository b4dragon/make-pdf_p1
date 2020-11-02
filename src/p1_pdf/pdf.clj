(ns p1-pdf.pdf
  (:require [clj-pdf.core :as pdf]
            [p1-pdf.image :as img]))
  

(def image-fmt {:xscale 0.9
                :yscale 0.9
                :align :center})
(def pdf-meta (read-string (slurp "meta.edn")))

(defn build [pdf-name images]
  (let [image-fmt (:image pdf-meta)
        fmt {:xscale (:xscale image-fmt)
             :yscale (:yscale image-fmt)
             :align (:align image-fmt)}]
    (pdf/pdf [{;;(when-not (empty? (:header pdf-meta))
                 ;;:header (:header pdf-meta))
               :author "나님"
               :size "A4"
               :doc-header ["inspired by " "sdfsdf"]
               :footer (get-in pdf-meta [:footer :text])}
              (for [image images]
                [:image fmt image])]
             (str pdf-name ".pdf"))))
  
(defn make-pdf [files filename]
  (->> files
       (map img/make-image)
       (remove nil?)
       (build filename)))
       
