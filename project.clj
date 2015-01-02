(defproject rage-db "1.0"
  :description "Lightweight disk based json database"
  :url "http://owainlewis.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins      [[lein-midje "3.1.1"]]}}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.3.1"]])
