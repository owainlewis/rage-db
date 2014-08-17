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

(facts "querying for rows in a keyspace"
  (let [db (create "foo")]
    (insert db :bar {:a 1 :b 2 :c {:d 3}})
    (let [result (? db :bar (fn [row] (= (:a row) 1)))]
      (count result) => 1
      (:a (first result)) => 1)))

(facts "about where"
  (let [db (create "users")]
    (insert db :users {:first "owain" :last "lewis" :email "owain@owainlewis.com"})
    (let [result (where db :users :first "owain")]
      (count result) => 1
      (:email (first result)) => "owain@owainlewis.com")))
