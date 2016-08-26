(ns steam-summary.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.browse :as b]))

(def ^:private BASE_URL "http://store.steampowered.com/search/results/?sort_by=Released_DESC&category1=998&os=win")

(def ^:private NAME_SELECTOR [:span.title])
(def ^:private URL_SELECTOR [:a.search_result_row])
(def ^:private DATE_SELECTOR [:div.search_released])
(def ^:private PRICE_SELECTOR [:div.search_price])
(def ^:private SCORE_SELECTOR [:div.search_reviewscore])
(def ^:private SELECTORS #{NAME_SELECTOR URL_SELECTOR SCORE_SELECTOR DATE_SELECTOR PRICE_SELECTOR})
(def ^:private DATE_FORMATS ["d MMM, yy" "MMM yy" "MMM d, yy"])

(defn- parse-date [s formats]
  (loop [[format & formats] formats]
    (when format
      (if-let [res (try (.parse (java.text.SimpleDateFormat. format) s)
                        (catch Exception e nil))]
        res
        (recur formats)))))

(defn- compare-dates [s1 s2]
  (compare (parse-date s1) (parse-date s2)))

(defn- fetch-page [url page-num]
  (html/html-resource (java.net.URL. (str url "&page=" page-num))))

(defn- parse-game-data [[url name date reviews price]]
  (let [review-string (-> reviews :content second :attrs :data-store-tooltip)
        review-seq (when review-string (re-seq #"\d+" review-string))]
    {:url (:href (:attrs url))
     :name (html/text name)
     :date (parse-date (html/text date) DATE_FORMATS)
     :review-score (first review-seq)
     :review-number (second review-seq)
     :price (re-find #"\d+" (html/text price))}))

(defn- get-games [url page-num]
  (let [games (partition 5
                         (html/select (read-string (slurp "steam")) SELECTORS))]
    (map parse-game-data games)))

; URL - Name - Date - Reviews - Price

;(nth (nth (partition 5
;            ;(map html/text
;                  (html/select (read-string (slurp "steam"))
;                               #{NAME_SELECTOR URL_SELECTOR SCORE_SELECTOR DATE_SELECTOR PRICE_SELECTOR}))
;       3)
;  3)


(defn open-in-browser [[first-url & urls]]
  (let [sacrificial-future (future (b/browse-url first-url))]
    (doseq [url urls] (b/browse-url url))
    (future-cancel sacrificial-future)))
    ; RIP Future 2016-2016. Your sacrifice will not be in vain!

(defn open-valid-games [min-date min-price min-reviews min-score])

