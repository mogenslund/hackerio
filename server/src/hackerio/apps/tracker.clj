(ns hackerio.apps.tracker
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hackerio.util :as util]))

(defn index
  []
  (str "
                                  Tracker

--------------------------------------------------------------------------------

To get distance to guard:

 * <host>/tracker/guard

  "))

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

(defn debug
  []
  "(pr-str @progress)")

;; Simple tracker just shows distance between subject and royal castle
;; We need the difference in time between two measurements of within 10 meters.

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "guard") (guard)
        ;(= (first args) "crack") (crack (second args))
        ;(= (first args) "crack-result") (crack-result (second args))
        (= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))


