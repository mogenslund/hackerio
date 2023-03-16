(ns hackerio.apps.sat1
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hackerio.util :as util]))

(defn index
  []
  (str "
                                  Sat 1
                            Radiation monitor

--------------------------------------------------------------------------------

This is the interface for the Satellite Sat 1.

The primary task of the satellite is to measure the amount of radiation at
different locations.

The latest data can be accessed through this endpoint:

 * <host>/sat1/data

The endpoint only returns the latests data and not the whole history. 

  "))

(defn rand-coord
  []
  (str (/ (rand-int 10000000) 100000.0) "," (/ (rand-int 10000000) 100000.0) ": " (rand-int 500)))

(defn data
  []
  (str/join "\n" (shuffle (conj (repeatedly 1200 rand-coord) "55.69599,12.56716: 456"))))

(defn debug
  []
  "(pr-str @progress)")

;; Simple tracker just shows distance between subject and royal castle
;; We need the difference in time between two measurements of within 10 meters.

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "data") (data)
        ;(= (first args) "crack") (crack (second args))
        ;(= (first args) "crack-result") (crack-result (second args))
        ;(= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))


