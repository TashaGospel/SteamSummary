(defproject steam-summary "0.1.3"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [enlive "1.1.6"]
                 [seesaw "1.4.5"]]
  :main ^:skip-aot steam-summary.gui
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
