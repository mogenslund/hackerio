(ns hackerio.tracker
  (:require [clojure.string :as str]))

(def guard-path
  (concat
    (range 500 700  10)  ; 20
    (range 700 600 -10)  ; 10
    (range 600 800  10)  ; 20
    (range 800 500 -10)  ; 30
    (range 500 660 -10)  ;
    (range 660 500 -10)))

(defn guard
  []
  (let [tick (mod (quot (System/currentTimeMillis) 1000) 96)
        t (format "%02d%02d" (quot tick 4) (* (mod tick 4) 15))
        dist (nth guard-path tick)]
    (str "Time: " t " Distance: " dist "m")))

(defn rand-coord
  []
  (str (/ (rand-int 10000000) 100000.0) "," (/ (rand-int 10000000) 100000.0) ": " (rand-int 500)))

(defn sat1
  []
  (str/join "\n" (shuffle (conj (repeatedly 1200 rand-coord) "55.69599,12.56716: 456"))))

