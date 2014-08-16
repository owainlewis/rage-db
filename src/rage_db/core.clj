(ns rage-db.core
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]))

(def ^:dynamic *directory* "data")

(defn build-file-path [path]
  (format "%s/%s" *directory* path))

(defn save
  "Save the database to disk. Files are saved in the form
   table-timestamp i.e users-140821234234"
  [database]
  (let [{:keys [db created data]} @database
        file-path (apply str [*directory* "/" db "-" created])]
    (do
      (spit file-path (json/write-str data))
      file-path)))

(defn read
  ""
  [db-name]
  (when (.exists (io/file (build-file-path db-name)))
    (let [[name timestamp] (clojure.string/split db-name #"-")]
      {:db name
       :created timestamp
       :data (json/read-str (slurp (str *directory* "/" db-name)))})))

(defrecord DB [db created data])

(defn create
  "Creates a new database"
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
  [db fn]
  (let [query-set (:data @db)]
    (filter fn query-set)))

(defn select [db k v]
  (? db (fn [row]
    (= (get row k) v))))
