(ns dr.util
  (:require [clojure.string :as str]))

(defn dist
  "Distance from one node to another.
  The st is the state."
  [game start end]
  (loop [visited #{}
         queue [[start]]
         distance 0]
    (when-let [path (first queue)]
      (let [current (last path)]
        (cond
          (= current end) (dec (count path)) ; Just use the length of the path minus 1 for distance
          (visited current) (recur visited (rest queue) distance)
          :else
          (recur (conj visited current)
                 (concat (rest queue)
                         (map #(conj path %)
                              (remove visited (get (game :paths) current))))
                 distance)))))) ; No increment here

(comment (dist {:paths {:A [:C :D] :B [:D] :C [:A :D] :D [:A :B :C :D :E] :E [:D :G :F] :F [:E :J :H] :G [:E :I] :H [:F :J] :I [:G :J] :J [:F :H :I]}} :A :I))

;; position like {"z" "A" "e" "B"}
(defn generate-map
  [m positions]
  (-> m
      (str/replace (re-pattern (str (or (get positions "z") "-") "X----")) (str (get positions "z") "X---z"))
      (str/replace (re-pattern (str (or (get positions "s") "-") "X---")) (str (get positions "s") "X--s"))
      (str/replace (re-pattern (str (or (get positions "e") "-") "X--")) (str (get positions "e") "X-e"))
      (str/replace (re-pattern (str (or (get positions "g") "-") "X-")) (str (get positions "g") "Xg"))
      (str/replace #"[A-Z]X-----" "       ")
      (str/replace #"[A-Z]X" " -")))



