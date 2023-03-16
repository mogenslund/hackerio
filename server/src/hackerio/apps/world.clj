(ns hackerio.apps.world
  (:require [clojure.set :as set]
            [clojure.string :as str]
            ;[hackerio.util :as util]
            ))

(def running (atom false))

(def world (atom {}))

(def default-world {
  :tick 0
  :objects [
    {:id "abc-123" 
     :type :drone
     :x 44.5632
     :y 23.4565
     :direction 235.23
     :speed 4.5
     :sensor-radiation 0}
    {:id "abc-124"
     :type :drone
     :x 10
     :y 10
     :direction 0.0
     :speed 4.6
     :sensor-radiation 0}
    {:id "abc-125"
     :type :drone
     :x 34.0 
     :y -23.0
     :direction 0.0
     :speed 4.6
     :sensor-radiation 0}
    {:id "abc-346"
     :type :object
     :radioactive 192.04
     :x 33.12
     :y -21.01
     :direction 0.0
     :speed 0}
  ]
})

(defn get-object
  [w id]
  (first (filter #(= (% :id) id) (w :objects))))

(comment (get-object default-world "abc-123"))
(comment (get-object default-world "nan-000"))

(defn direction
  [x]
  (cond (= x "n") 0
        (= x "ne") 45
        (= x "e") 90
        (= x "se") 135
        (= x "s") 180
        (= x "sw") 225
        (= x "w") 270
        (= x "nw") 315
        true x))

(defn index
  []
  (str "
                                   World 

--------------------------------------------------------------------------------


  " ))

(defn sq
  [x]
  (* x x))

(defn sqrt
  [x]
  (Math/sqrt x))

(defn dist
  [obj1 obj2]
  (sqrt (+ (sq (- (obj1 :x) (obj2 :x))) (sq (- (obj1 :y) (obj2 :y))))))

(comment (dist (get-object default-world "abc-123") (get-object default-world "abc-124")))

(defn move-object
  [o]
  (-> o
      (update :x #(+ % (* (o :speed) (Math/cos (Math/toRadians (- 90 (direction (o :direction))))))))
      (update :y #(+ % (* (o :speed) (Math/sin (Math/toRadians (- 90 (direction (o :direction))))))))))

(comment
  (move-object {:x 0 :y 0 :speed 5 :direction 0})
  (move-object {:x 0 :y 0 :speed 5 :direction "n"})
  (move-object {:x 0 :y 0 :speed 5 :direction 90})
  (move-object {:x 0 :y 0 :speed 5 :direction "e"})
  (move-object {:x 0 :y 0 :speed 5 :direction 180})
  (move-object {:x 0 :y 0 :speed 5 :direction "s"})
  (move-object {:x 0 :y 0 :speed 5 :direction 270})
  (move-object {:x 0 :y 0 :speed 5 :direction "w"}))



(defn sensor-radiation
  [w o]
  ;; Sum of distance to radiation-object formula
  ;; Intensity / dist^2
  (let [radioactive (filter :radioactive (w :objects))]
    (if (o :sensor-radiation)
      (assoc o :sensor-radiation (apply + (map #(/ (% :radioactive) (sq (dist o %))) radioactive)))
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
SPEED: " (o :speed) "
DIRECTION: " (o :direction) "
RADIATION: " (or (o :sensor-radiation) "NaN") "

  " )))

(defn change-direction
  [w id direction]
  (let [idx (.indexOf (mapv :id (w :objects)) id)]
    (assoc-in w [:objects idx :direction] direction)))

(defn change-speed
  [w id speed]
  (let [idx (.indexOf (mapv :id (w :objects)) id)]
    (assoc-in w [:objects idx :speed] speed)))

(comment (.indexOf (mapv :id (default-world :objects)) "abc-123"))
(comment (.indexOf (mapv :id (default-world :objects)) "abc-123"))

(defn action
  [args]
  (let [id (nth args 0)]
    (cond (nil? (get-object @world id)) "NaN" 
          (= (count args) 1) (info @world id) 
          (= (nth args 1) "direction") (swap! world change-direction id (if (re-find #"\d" (nth args 2)) (Double/parseDouble (nth args 2)) (nth args 2)))
          (= (nth args 1) "speed") (swap! world change-speed id (Double/parseDouble (nth args 2)))
          true "NaN")))
(comment (println (get-object "a")))
(comment (println (action ["a"])))
(comment (println (action ["abc-123"])))
(comment (println (action ["abc-123" "direction" "20"])))
(comment (println (action ["abc-123" "speed" "20"])))

(comment (sensor-radiation default-world "abc-123"))
(comment (sensor-radiation default-world "abc-124"))
(comment (sensor-radiation default-world "abc-125"))

(defn tick
  [w]
  (-> w
      (update :tick inc)
      (update :objects #(mapv move-object %))
      (update :objects #(mapv (fn [x] (sensor-radiation w x)) %))))

(comment (println (pr-str (tick default-world))))

(defn start
  []
  (reset! running true)
  (reset! world default-world)
  (future 
    (while @running
      (let [t0 (System/currentTimeMillis)]
        (swap! world tick)
        ;; Sleep until 1 sec after last iteration
        (Thread/sleep (max 0 (- (System/currentTimeMillis) t0 -1000)))))))

(defn stop
  []
  (reset! running false))

(defn debug
  []
  (pr-str @world))

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "????") (second args)
        (= (first args) "start") (start)
        (= (first args) "stop") (stop)
        (= (first args) "debug") (debug)
        (re-find #"-" (first args)) (action args)
        ;(= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))

(comment
  (api "start")
  (println (api "debug"))
  (println (api "abc-123"))
  (api "abc-123" "direction" "n")
  (api "abc-123" "direction" "e")
  (api "abc-123" "direction" "s")
  (api "abc-123" "direction" "w")
  (api "abc-123" "direction" "123")
  (api "abc-123" "speed" "2")
  (api "abc-123" "speed" "0")
  (println (api "abc-123")) ; Info. Pos and sensor data
  (api "stop"))




