(ns steam-summary.gui
  (:require [steam-summary.core :as core])
  (:use seesaw.core
        seesaw.dev
        [seesaw.forms :exclude [separator]])
  (:gen-class))

(native!)
(debug!)

(defn -main
  "I will do a lot! ...eventually."
  [& args]
  (let [f (frame :title "Steam Summary" :on-close :dispose
                 :resizable? false :size [400 :by 165])
        form (forms-panel "right:65dlu, 5dlu, pref:grow, pref:grow, 5dlu"
               :items ["Earliest date:" (span (text :id :date-field) 2)
                       (next-line)
                       "Minimum price:" (span (text :id :price-field) 2)
                       (next-line)
                       "Minimum reviews:" (span (text :id :review-field) 2)
                       (next-line)
                       "Minimum score (%):" (span (text :id :score-field) 2)
                       (next-line)])
        b (button :text "Open" :halign :center :margin [0 25])
        content-panel (vertical-panel
                        :items
                          [form
                           (doto b
                             (.setAlignmentX java.awt.Component/CENTER_ALIGNMENT))])]
    (.. f getRootPane (setDefaultButton b))
    (-> f
        (config! :content content-panel)
        show!)))





