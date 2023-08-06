(ns hackerio.units
  (:require [clojure.string :as str]
            [hackerio.util :as util]))


(defn generate-uuid
  []
  (str (java.util.UUID/randomUUID)))

(defn hq
  [owner pos]
  {:id (generate-uuid)
   :password (rand-int 10000)
   :owner owner
   :type :hq
   :pos pos
   :target pos
   :speed 0
   :max-speed 1
   })

(defn scout
  [owner pos]
  {:id (generate-uuid)
   :password (rand-int 100)
   :owner owner
   :type :scout
   :pos pos
   :target pos
   :speed 0
   :max-speed 10
   :view-range 20
   :visibility 50 ; percent
   :precision 70 ; percent
   ; Discoverability is visibility of enemy times precision of own.
   })

(defn summary
  [unit]
  (str (format "ID: %s\n" (unit :id))
       (format "TYPE: %s\n" (name (unit :type)))
       (format "X: %.0f\n" (double ((unit :pos) :x)))
       (format "Y: %.0f\n" (double ((unit :pos) :y)))
       (when (unit :target) (format "TARGET-X: %.0f\n" (double ((unit :target) :x))))
       (when (unit :target) (format "TARGET-Y: %.0f\n" (double ((unit :target) :y))))))

(defn move
  [unit tick-sec]
  (if (and (map? unit) (unit :target) (not= (unit :pos) (unit :target)) (> (unit :speed) 0))
    (update unit :pos util/coord-move (unit :target) (unit :speed) tick-sec)
    unit))

(comment
  (move (assoc (scout "mogens" {:x 1000 :y 2000}) :target {:x 3000 :y 2000} :speed 10) 2)
  (move nil 2))
