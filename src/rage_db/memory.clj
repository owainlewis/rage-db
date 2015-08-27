(ns rage-db.memory
    (:require [rage-db.protocol :refer :all]))

;; An in memory implementation using a Clojure atom

(extend-protocol Rage

  clojure.lang.Atom

  (insert [this ks row]
    (swap! this assoc-in [:store ks]
      (conj
        (get-in @this [:store ks] []) row)))

  (keyspace [this ks]
    (let [result (get-in @this [:store ks])]
      (if (nil? result) {} result)))

  (keyspaces [this]
    (keys (:store @this)))

  (? [this ks f]
    (into []
      (filter f (keyspace this ks))))

  (where [this ks k v]
    (? this ks (fn [row]
                 (= (get row k) v))))

  (one [this ks k v]
    ((comp first where) this ks k v))

  (drop-where
    [this ks k v]
      (swap! this assoc-in [:store ks]
        (filter
          (complement
            #(= (get % k) v))
              (get-in @this [:store ks] []))))

  (drop-db [this]
    (swap! this assoc-in [:store] {})))

