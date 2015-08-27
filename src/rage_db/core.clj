(ns rage-db.core
  (:refer-clojure :exclude [flush])
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split]]
            [rage-db.protocol :refer :all]
            [rage-db.memory :refer :all]
            [cheshire.core :as json]))

;; -------------------------------------------------------------------
;;
;; RAGE DB. A simple in memory file backed database for prototyping
;;
;; Rage is a very simple but useful in memory data store for prototyping and for cases
;; where your datasets are small enough to work with in memory.
;; Data is manipulated in memory as basic clojure maps but stored to disk as plain JSON

;; -------------------------------------------------------------------

(def ^:dynamic *directory* "data")

(defrecord RDB [db-name store])

(defn ^{:doc
  "Creates a new in memory database using
   clojure.lang.PersistentArrayMap as the underlying store
   e.g
     (create \"users\")"}
  create
  [db]
  (atom (RDB. (name db) {})))

;; Default empty in memory db
(def mem-db (create :mem))

;; -------------------------------------------------------------------

(defn as-json [record]
  (json/generate-string record))

;; Meta functions
;; -------------------------------------------------------------------

(defn size
  "Returns the number of records in a key space"
  [db ks]
  (if-let [data-for-key (get-in @db [:store ks])]
    (count data-for-key)
     0))

;; Storage
;; -------------------------------------------------------------------

(defn- build-file-path
  "Helper function that builds the path to store data on disk"
  [path]
  (format "%s" path))

;; TODO the data directory idea is stupid. Allow users to pass in a path to the data folder

(defn dump
  "Save the database to disk. Files are saved in the form
   table-timestamp i.e users-140821234234"
  [database]
  (let [{:keys [db-name store]} @database
        file-name (str db-name "-" (System/currentTimeMillis))
        file-path file-name
        _ (spit file-path
            (json/generate-string store {:pretty true}))]
     file-name))

(defn load-db
  "Read json from the data directory"
  [db-name]
  (when (.exists
          (io/file
            (build-file-path db-name)))
    (let [[name timestamp]
            (split db-name #"-")]
      (atom
        (RDB.
           name
           (json/parse-string (slurp db-name)))))))

;; Database compression for persistance
;; -------------------------------------------------------------------

(defn read-gzip [file-path]
  (with-open [in (java.util.zip.GZIPInputStream.
                   (clojure.java.io/input-stream file-path))]
  (slurp in)))

(defn write-gzip
  "Write text to gzip"
  [data filename]
  (with-open [w (-> filename
                    io/output-stream
                    java.util.zip.GZIPOutputStream.
                    io/writer)]
  (binding [*out* w]
    (println data))))
