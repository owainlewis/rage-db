(ns rage-db.core
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]))
          
;; Rage is a very simple but useful in memory data store for prototyping and for cases 
;; where your datasets are small enough to work with in memory.

;; Data is manipulated in memory as basic clojure maps but stored to disk as plain JSON

(def ^:dynamic *directory* "data")

(defn- build-file-path
  "Helper function that builds the path to store data on disk
   By default all data will be stored in /data"
  [path]
  (format "%s/%s" *directory* path))

(defn save
  "Save the database to disk. Files are saved in the form
   table-timestamp i.e users-140821234234"
  [database]
  (let [{:keys [db created data]} @database
        file-path (apply str [*directory* "/" db "-" created])]
    (do
      (spit file-path (json/generate-string data {:pretty true}))
        file-path)))

(defn read
  "Read json from the data directory"
  [db-name]
  (when (.exists (io/file (build-file-path db-name)))
    (let [[name timestamp] (clojure.string/split db-name #"-")]
      {:db name
       :created timestamp
       :data (json/parse-string (slurp (str *directory* "/" db-name)))})))

(defrecord DB [db created data])

(defn create
  "Creates a new in memory database"
  [name]
  (atom
    (DB. name (System/currentTimeMillis) [])))

(defn insert
  "Insert a single row into the data set
   e.g (insert db {:a 1 :b 2})"
  [db row]
  (let [_ (swap! db update-in [:data] merge row)]
    true))

(defn insert-many
  "Insert many records into the data set
   in one go"
  [db & rows]
  (reduce insert db
     (into [] rows)))

(defn flush
  "Flushes all records from the database"
  [db]
  (swap! db assoc-in [:data] []))

(defn ?
  "Query the dataset with a function i.e
     (? db (fn [row] (= :email row) \"owain@owainlewis.com\""
  [db fn]
  (let [query-set (:data @db)]
    (filter fn query-set)))

(defn select 
  "A helper function that makes it easy to query the dataset where
   a key k is equal to a given value v"
  [db k v]
  (? db (fn [row]
    (= (get row k) v))))
