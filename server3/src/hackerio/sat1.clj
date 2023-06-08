(ns hackerio.sat1
  (:require [clojure.string :as str]))

(defn rand-coord
  []
  (str (/ (rand-int 10000000) 100000.0) "," (/ (rand-int 10000000) 100000.0) ": " (rand-int 500)))

(defn data
  []
  (str/join "\n" (shuffle (conj (repeatedly 1200 rand-coord) "55.69599,12.56716: 456"))))

