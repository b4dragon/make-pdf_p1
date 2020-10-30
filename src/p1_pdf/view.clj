(ns p1-pdf.view
  (:require [cljfx.api :as fx]
            [p1-pdf.pdf :as pdf])
  (:import [java.io FileInputStream InputStream]
           [javafx.stage FileChooser]
           [javafx.event ActionEvent]
           [javafx.scene Node]))

(def *state
  (atom {:file nil}))


;; (defn set-noti [s]
;;   (dorun (prn "in the noti")
;;          (swap! *state :noti s)))

(defmulti handle ::event)

(defmethod handle ::open-file [{:keys [^ActionEvent fx/event]}]
  (let [window (.getWindow (.getScene ^Node (.getTarget event)))
        chooser (doto (FileChooser.)
                  (.setTitle "Open File"))]
    (when-let [files (.showOpenMultipleDialog chooser window)]
      {:state {:files files :content nil :noti "click make-pdf and wait until done"}})))

(defmethod handle ::make-pdf [{:keys [^ActionEvent fx/event files]}]
  (let [window (.getWindow (.getScene ^Node (.getTarget event)))
        files (:files @*state)]
    (pdf/make-pdf files "made")
    {:state {:files files :content nil :noti "done"}}))

(defn root-view [{:keys [files content noti]}]
  {:fx/type :stage
   :title "..........."
   :showing true
   :width 800
   :height 600
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :padding 30
                  :spacing 15
                  :children [{:fx/type :label
                              :text noti}
                             {:fx/type :h-box
                              :spacing 15
                              :alignment :center-left
                              :children [{:fx/type :button
                                          :text "Open file..."
                                          :on-action {::event ::open-file}}
                                         {:fx/type :button
                                          :text "make pdf"
                                          :on-action {::event ::make-pdf}}]}
                             {:fx/type :text-area
                              :v-box/vgrow :always
                              :editable false
                              :text (apply str (map #(str (.getName %) "\n") files))}]}}})

(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc #(root-view %))
    :opts {:fx.opt/map-event-handler
           (-> handle
               (fx/wrap-co-effects {:state (fx/make-deref-co-effect *state)})
               (fx/wrap-effects {:state (fx/make-reset-effect *state)
                                 :dispatch fx/dispatch-effect}))}))

(defn view []
  (fx/mount-renderer *state renderer))
