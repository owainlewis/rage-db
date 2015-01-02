(ns rage-db.postgres
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json]))

;; PostgreSQL adapter for Rage

;; CREATE TABLE users ( data json );

;; Insertions
;; INSERT INTO keyspace VALUES ( json-document )
;; Where
;; SELECT * FROM keyspace WHERE data->>'key' = 'v';

(defn value-to-json-pgobject [value]
  (doto (PGobject.)
    (.setType "json")
      (.setValue (json/generate-string value))))

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value] (value-to-json-pgobject value))

  clojure.lang.IPersistentVector
  (sql-value [value] (value-to-json-pgobject value)))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (json/parse-string value :key-fn keyword)
        :else value))))

(def db
  (let [db-host "localhost"
        db-port 5432
        db-name "rage"]
  {:classname "org.postgresql.Driver" ; must be in classpath
   :subprotocol "postgresql"
   :subname (str "//" db-host ":" db-port "/" db-name)}))

(defn insert [record]
  (jdbc/insert! db :users {:data record}))

(defn query []
  (jdbc/query db "SELECT * FROM users"))
