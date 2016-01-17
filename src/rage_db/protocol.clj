(ns rage-db.protocol)

;; Generic protocol/abstraction to be implemented by various back end stores

(defprotocol Rage

  (insert [this ks row]
    "insert an item in to a given keyspace")

  (keyspace [this ks]
    "Select all items in the keyspace")

  (keyspaces [this]
    "Return all available keyspaces")

  (? [this ks f]
    "Query the database using a simple function")

  (where [this ks k v]
    "Select an item in the database where k = v")

  (one [this ks k v]
    "Return the first matching item or nil")

  (drop-where [this ks k v]
    "Drop every occurence in a keyspace where k = v")

  (drop-db [this]
     "Drop everything from the database"))
