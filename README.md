# Rage DB

Rage is a very simple but useful in memory data store for prototyping and for cases
where your datasets are small enough to work with in memory.

Data is manipulated in memory as basic clojure maps but stored to disk as plain JSON

## Use case example

You are prototyping a web crawler which runs every 10 minutes returning a small ish dataset
that is manipulated in memory. You need to store the results to disk and save them to AWS with a timestamp.

Rage db lets you work with simple clojure data structures and persist the results to disk as immutable JSON which can be read at a later date for analysis.

## Usage

Create a new database

```clojure

(ns foo
  (require [rage-db.core :refer all]))

;; Create a new in memory database
(def db (create "users"))

;; #<Atom@453fc370: #rage_db.core.DB{:db "users", :created 1408216769512, :data []}>

;; Insert some data

(insert db
  {:first "owain" :last "lewis" :email "owain@owainlewis.com"})

;; Now let's query for a user

(select db :email "owain@owainlewis.com")

;; [{:email "owain@owainlewis.com", :first "owain", :last "lewis"}]

;; Once we are done we can flush our data to disk

(save db)

;; "data/users-1408216769512"

```

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
