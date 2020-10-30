(ns front.view
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))

(defn print-as-pdf [content]
  (let [w (.open js/window  "" "PRINT" "height=550 width=900,top=100,left=150")]
    (.write (.-document w)  "<html><head><title>print as pdf</title>")
    (.write (.-document w)              "</head><body> <style>@page{size: 7in 9.25in; margin:27mm 16mm 27mm 16mm;}</style>")
    
    (.write (.-document w)
             (str (.-innerHTML (js/document.getElementById "printing-part"))))
    
                 ;; "</head><body> <style>@page{size: 7in 9.25in; margin:27mm 16mm 27mm 16mm;}</style>"
                 ;; content
   (.write (.-document w)       "</body></html>")
    (.close (.-document w))
    (.focus w)
    (.print w)
    (.close w)))

(defn test-print-as-pdf []
  [:button {:class "button"
            :on-click #(print-as-pdf (.-innerHTML (js/document.getElementById "app")))}
   "as pdf"])

(defn select-file []
  (let [on-change (fn [e]
                    (let [file (aget (.-files (.-target e)) 0)]
                      (prn "###" (.-webkitRelativePath (.-files (.-target e))))
                      (if (.startsWith (.-type file) "image")
                        (let [rdr (new js/FileReader)
                              image-area (.getElementById js/document "div-0")]
                          (set! (.-onload rdr) (fn [_]
                                                 (let [img (.getElementById js/document "img-0")]
                                                   (set! (.-innerHTML image-area) "")
                                                   (set! (.-src img) (.-result rdr))
                                                   (set! (.-class img) "img"))))
                                                   ;; (.appendChild image-area img))))
                          (.readAsDataURL rdr file))
                        (js/alert "choose image file"))))]
                      
    [:input {:type "file"
             :id "files"
             :name "files[]"
             :multiple true
             :directory true
             ;; :webkitdirectory true
             :on-change on-change}]))
  
(defn main-page []
  [:div
   [test-print-as-pdf]
   [select-file]
   [:div {:class nil
          :id "printing-part"}
    (for [i (range 1)]
      ;; [:section {:class "sheet padding-10mm"}
      [:div {:class  "page"
             :id (str "div-" i)}
       [:img {:id (str "img-" i)
              :src nil}]])]])
       

    




;;;;;;;;;;;;

(fx/on-fx-thread
 (fx/create-component
  {:fx/type :stage
   :showing true
   :title "cljsfx example"
   :width 300
   :height 100
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :alignment :center
                  :children [{:fx/type :label
                              :text "hello world"}]}}}))
(def renderer
  (fx/create-renderer))

(defn root [{:keys [showing]}]
  {:fx/type :stage
   :showing showing
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :padding 50
                  :children [{:fx/type :button
                              :text "close"
                              :on-action (fn [_]
                                           (renderer {:fx/type root
                                                      :showing false}))}]}}})

(renderer {:fx/type root
           :showing true})

(def ext-with-html
  (fx/make-ext-with-props
   {:html (fx.prop/make
           (fx.mutator/setter #(.load (.getEngine ^WebView %1) %2)) ;;#(.loadContent (.getEngine ^WebView %1) %2))
           fx.lifecycle/scalar)}))

(fx/on-fx-thread
 (fx/create-component
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type ext-with-html
                  :props {:html (.toString (.toURL (.toURI (new File "resources/public/index.html"))))}
                  :desc {:fx/type :web-view}}}}))

