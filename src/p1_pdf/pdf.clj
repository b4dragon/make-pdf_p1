(ns p1-pdf.pdf
  (:require [clj-pdf.core :as pdf]
            [p1-pdf.image :as img]))
  

(def image-fmt {:xscale 0.9
                :yscale 0.9
                :align :center})
(def setting (read-string (slurp "setting.edn")))

(defn build [pdf-name images]
  (let [image-fmt (:image setting)
        fmt {:xscale (:xscale image-fmt)
             :yscale (:yscale image-fmt)
             :align (:align image-fmt)}]
    (pdf/pdf [{:header (:header setting)
               :author (:author setting)
               :size (:size setting)
               :doc-header [""]
               :footer (get-in setting [:footer :text])
               :page-numbers (get-in setting [:footer :page-numbers])}
              (for [image images]
                [:image fmt (:image image)])]
             pdf-name)))
  
(defn make-pdf [files filename]
  (->> files
       (pmap #(img/make-image % (:image-width setting)))
       (remove nil?)
       (build filename)))
       
