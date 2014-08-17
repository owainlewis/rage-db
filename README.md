# Rage DB

Rage is a very simple but incredibly useful data store for prototyping or working with in memory datasets.

Data is manipulated in memory as basic clojure maps but stored to disk as plain JSON making it easy to transfer
your data between applications.

## Quickstart

Clojars

```
[rage-db "0.1.0-SNAPSHOT"]
```

Maven

```
<dependency>
  <groupId>rage-db</groupId>
  <artifactId>rage-db</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

Create a new database

```clojure

(ns foo
  (require [rage-db.core :as rdb]))

;; Create a new in memory database
(def db (rdb/create "demo"))

;; #<Atom@7079cda4: #rage_db.core.RDB{:db-name "demo", :store {}}>

;; Insert some data

(rdb/insert db :users
  {:first "owain"
   :last  "lewis"
   :email "owain@owainlewis.com"})

;; Now let's query for a user

(rdb/where db :users :email "owain@owainlewis.com")

;; [{:email "owain@owainlewis.com", :first "owain", :last "lewis"}]

;; Once we are done we can flush our data to disk

(rdb/dump db)

;; "data/users-1408216769512"

```

## Querying the database

You can use the ? function to find data. By default the underlying store in Rage is a simple Clojure map.

```clojure

;; Find a user in the users keyspace with a given email

(rdb/? db :users (fn [row] (= (:email row) "jack@twitter.com")))

```

## Save your data

Data can be flushed to disk as JSON like this

```clojure
(rdb/dump db)
```

This command returns a file path to your database. Notice how the files are saved with a timestamp
representing the database creation time. All data is stored as pretty printed json.

```json
{
  "users" : [ {
    "email" : "owain@owainlewis.com",
    "first" : "owain",
    "last" : "lewis"
  }, {
    "email" : "jack@twitter.com",
    "first" : "jack",
    "last" : "dorsey"
  }, {
    "email" : "bill@microsoft.com",
    "first" : "bill",
    "last" : "gates"
  } ]
}
```

## Loading data

Loading data is equally easy

```clojure

(def db (rdb/load-db "users-1408216769512"))

```

## License

Copyright © 2014 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
