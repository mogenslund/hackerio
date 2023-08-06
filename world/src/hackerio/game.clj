(ns hackerio.game
  (:require [clojure.string :as str]))


;; Expected to have id
(defn add-unit
  "Inserts a new unit into game"
  [g unit]
  (assoc g (unit :id) unit))

(defn update-unit
  "Given right user (owner) applies function to unit"
  [g id user fun]
  (if (= (get-in g [id :owner]) user)
    (update g id fun)
    g))

(comment
  (def g0 {})
  (def g1 (add-unit g0 {:id "abc-123" :user "abc" :pos {:x 3 :y 4}}))
  (update-unit g1 "abc-123" "abc" (fn [u] (assoc-in u [:pos :x] 5)))
  (update-unit g1 "abc-123" "non" (fn [u] (assoc-in u [:pos :x] 6)))
  (println g2)
)
