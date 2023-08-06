(ns hackerio.game
  (:require [clojure.string :as str]))

; cooldown: sec, timestamp: epoch sec
(def world (atom {
  :players {:abc-001 {:nodes {:abc-123 {:type :sat-scan :level 2 :timestamp 0 :cooldown 60}}}}
  }))

(defn use-node-sat-scan
  [player nodeid pos]
  (let [t (quot (System/currentTimeMillis) 1000)
        node (get-in @world [:players player :nodes nodeid])]
  ))

(defn use-node
  [player nodeid & {:keys [pos]}]
  (let [node-type (get-in @world [:players player :nodes nodeid :type])]
    (cond (= node-type :sat-scan) (use-node-sat-scan player nodeid pos))))

(comment (use-node :abc-001 :abc-123 nil)
  (get-in @world [:players :abc-001 :nodes :abc-123])
  )
