# Rage DB

Rage is a very simple but incredibly useful data store for prototyping and for cases where your datasets are small enough to work with in memory.

Data is manipulated in memory as basic clojure maps but stored to disk as plain JSON making it easy to transfer
your data between applications.

## Quickstart

[rage-db "0.1.0-SNAPSHOT"]

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

## Querying the database

Querying the data-set is no different from querying any other clojure map. You can use the ? function
to find data

```clojure

;; Find a user with a given email

(? db (fn [row] (= (:email row) "jack@twitter.com")))

```

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
