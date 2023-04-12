(ns hackerio.world.model
  (:require [clojure.string :as str]
            [hackerio.world.util :as util]))

;;
;; !!!!!!!!!!!!!!!!!!!!! Model should be "immuteable"
;; Se no state, but world in world out.

(defn get-object
  [w id]
  (first (filter #(= (% :id) id) (w :objects))))

(defn index
  []
  (str "
                                   World 

--------------------------------------------------------------------------------


  " ))


(defn sensor-radiation
  [w o]
  ;; Sum of distance to radiation-object formula
  ;; Intensity / dist^2
  (let [radioactive (filter :radioactive (w :objects))]
    (if (o :sensor-radiation)
      (assoc o :sensor-radiation (apply + (map #(/ (% :radioactive) (util/sq (util/dist o %))) radioactive)))
      o)))

(defn info
  [w id]
  (let [o (get-object w id)]
    (str "
                                   Info

--------------------------------------------------------------------------------

ID: " (o :id) "
X: " (o :x) "
Y: " (o :y) "
TARGET-X: " (o :target-x) "
TARGET-Y: " (o :target-y) "
SPEED: " (o :speed) "
RADIATION: " (or (o :sensor-radiation) "NaN") "

  " )))

(defn change-direction
  [w id direction]
  (let [idx (.indexOf (mapv :id (w :objects)) id)]
    (assoc-in w [:objects idx :direction] direction)))

(defn change-target
  [w id target]
  (let [idx (.indexOf (mapv :id (w :objects)) id)]
    (-> w
        (assoc-in [:objects idx :target-x] (:x target))
        (assoc-in [:objects idx :target-y] (:y target)))))

(defn change-speed
  [w id speed]
  (let [idx (.indexOf (mapv :id (w :objects)) id)]
    (assoc-in w [:objects idx :speed] speed)))

(defn tick
  [w]
  (-> w
      (update :tick inc)
      (update :objects #(mapv util/move-object %))
      (update :objects #(mapv (fn [x] (sensor-radiation w x)) %))))



(comment
  (do
    (defn p [m] (println "\n") (clojure.pprint/pprint m) (println ""))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/util.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/data.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/model.clj"))
    (ns user (:require [hackerio.world.data :as util] [hackerio.world.data :as data] [hackerio.world.model :as model])))
  (model/get-object data/default-world "abc-123")
  (model/get-object data/default-world "nan-000")

  (p (model/sensor-radiation data/default-world "abc-123"))
  (pprint (model/tick data/default-world))
  (p {:a 1})
  (p (model/tick data/default-world))
  )

(comment )

(comment (.indexOf (mapv :id (default-world :objects)) "abc-123"))
(comment (.indexOf (mapv :id (default-world :objects)) "abc-123"))

(comment (re-find #"-?\d+\.?\d*" "test -0.43"))

