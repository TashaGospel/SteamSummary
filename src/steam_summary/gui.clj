(ns steam-summary.gui
  (:require [steam-summary.core :as core]
            [clojure.edn :as edn])
  (:use seesaw.core
        seesaw.dev
        [seesaw.forms :exclude [separator]])
  (:import (java.awt Component))
  (:gen-class))

(native!)
(debug!)

(def DEFAULTS_FILE "default.edn")

(defn get-defaults []
  (try (edn/read-string (slurp DEFAULTS_FILE))
       (catch Exception e nil)))

(defn -main
  "I will do a lot! ...eventually."
  [& args]
  (let [defaults (get-defaults)
        f (frame :title "Steam Summary" :on-close :dispose
                 :resizable? false :size [400 :by 180])
        form (forms-panel "right:65dlu, 5dlu, pref:grow, pref:grow, 5dlu"
               :items ["Earliest date:" (span (text :id :date-field
                                                    :text (:min-date defaults)) 2)
                       (next-line)
                       "Minimum price:" (span (text :id :price-field
                                                    :text (:min-price defaults)) 2)
                       (next-line)
                       "Minimum reviews:" (span (text :id :review-field
                                                      :text (:min-reviews defaults)) 2)
                       (next-line)
                       "Minimum score (%):" (span (text :id :score-field
                                                        :text (:min-score defaults)) 2)
                       (next-line)])
        error-label (doto (label)
                          (.setAlignmentX Component/CENTER_ALIGNMENT))
        b (doto (button :text "Open" :halign :center :margin [0 25])
                (.setAlignmentX Component/CENTER_ALIGNMENT))
        get-criteria (fn []
                       {:min-date (value (select form [:#date-field]))
                        :min-price (Float. (value (select form [:#price-field])))
                        :min-reviews (Integer. (value (select form [:#review-field])))
                        :min-score (Integer. (value (select form [:#score-field])))})
        content-panel (vertical-panel
                        :items [form error-label b])]

    (listen b :action
            (fn [e]
              (try (core/open-valid-games (get-criteria))
                   (text! error-label "")
                   (catch Exception e
                     (text! error-label "Invalid parameters")))))
    (listen f :window-closed
            (fn [e]
              (spit DEFAULTS_FILE
                    {:min-date (.format
                                 (java.text.SimpleDateFormat. "MMM d, yyyy")
                                 (java.util.Date.))
                     :min-price (value (select form [:#price-field]))
                     :min-reviews (value (select form [:#review-field]))
                     :min-score (value (select form [:#score-field]))})))

    (.. f getRootPane (setDefaultButton b))
    (-> f
        (config! :content content-panel)
        show!)))