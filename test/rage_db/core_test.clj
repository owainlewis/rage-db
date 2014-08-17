(ns rage-db.core-test
  (:require [midje.sweet :refer :all]
            [rage-db.core :refer :all]))


(facts "creating a database"
  (let [db (create "users")]
    (class db)     => clojure.lang.Atom
    (:store @db)   => {}
    (:db-name @db) => "users"))

(facts "inserting rows into a keyspace"
  (let [db (create "foo")]
    (insert db :users {:a 1 :b 2})
    (size db :users) => 1))
