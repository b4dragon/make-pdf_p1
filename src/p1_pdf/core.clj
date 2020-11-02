(ns p1-pdf.core
  (:require [p1-pdf.view :as view])
  (:gen-class))

(defn -main []
  (view/view))

(comment
  (javafx.application.Platform/exit)
  )
