(ns rage-db.postgres
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json]))

;; PostgreSQL adapter for Rage

(defn db []
  (let [db-host "localhost"
        db-port 5432
        db-name "rage"]
  {:classname "org.postgresql.Driver" ; must be in classpath
   :subprotocol "postgresql"
   :subname (str "//" db-host ":" db-port "/" db-name)}))
