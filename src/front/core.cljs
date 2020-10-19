(ns front.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf :refer [dispatch dispatch-sync subscribe]]
            [front.view]
            [front.events]
            [front.subs]))

(defn app []
  (front.view/main-page))

(defn render []
  (rdom/render [app] (.getElementById js/document "app")))

(defn init []
  (prn "init")
  (render))

(defn re-init []
  (rf/clear-subscription-cache!)
  (init))
