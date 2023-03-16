(ns hackerio.apps.m1
  (:require [clojure.string :as str]))

(def guard1-route [[1 4] [2 4] [3 4] [4 4] [5 4] [6 4] [5 4] [4 4] [3 4] [2 4]])
(def guard2-route [[1 5] [2 5] [3 5] [4 5] [5 5] [6 5] [5 5] [4 5] [3 5] [2 5]])

(def default-positions {
  :server1 [6 7]
  :guard1 [1 4]
  :guard2 [1 5]
  :scout1 [5 1]
  :engineer1 [4 1]})

(def world (atom {}))

(defn reset-world
  []
  (reset! world {
    :tick 1
    :status "In Progress"
    :token (str (java.util.UUID/randomUUID))
    :orders {:scout1 "n" :engineer ""}
    :guard1-offset (rand-int 6)
    :guard2-offset (rand-int 6)
    :positions default-positions}))



(comment (def positions default-positions))
(comment (def ks (into [] (distinct (vals positions)))) (println ks))
(comment (.indexOf (into [] ks) [4 1]))
(comment (map #(str (second %)) (filter #(= (first (key %)) 4) (group-by second positions))))
(comment (str/join " " (map #(name (first %)) (filter #(= (second (val %)) 1) positions))))

(defn draw-map
  [positions]
  (let [ks (into [] (distinct (vals positions)))]
    (str 
         "\n     ╭──────╮\n"
      (str/join "\n"
        (for [r (reverse (range 1 8))]
          (str "   " r  " │" (apply str
            (for [c (range 1 7)]
              (let [i (inc (.indexOf ks [c r]))]
                (if (> i 0) "x" ".")))) "│ "
                (str/join " " (map #(name (first %)) (sort-by second (filter #(= (second (val %)) r) positions))))
      )))
      "\n     ╰──────╯\n     123456\n\n")))

(comment (println (draw-map default-positions)))

;    ╭──────╮
;  7 │.....x│ server1
;  6 │......│
;  5 │......│
;  4 │..x...│ guard1
;  3 │......│
;  2 │.x.x..│ engineer1 scout1 
;  1 │......│
;    ╰──────╯
;     123456
;
; e move n -> Will add a move n command to tick-orders.
; s move n 
; info
; > ...
;

(defn change-pos
  [pos direction]
  ; (change-pos [1 3] "n")
  (let [x (first pos)
        y (second pos)]
    (cond (and (= direction "n") (< y 7)) [x (inc y)]
          (and (= direction "s") (> y 1)) [x (dec y)]
          (and (= direction "e") (< x 6)) [(inc x) y]
          (and (= direction "w") (> x 1)) [(dec x) y]
          true pos)))

(defn update-order
  [unit command]
  (swap! world update :orders assoc unit command)
  (str "Order: " unit " " command))

(comment (update-order :scout1 "n"))

(defn max-dist [pos1 pos2] (max (abs (- (pos1 0) (pos2 0))) (abs (- (pos1 1) (pos2 1)))))

(defn visible-positions
  [positions]
  (let [guard1dist (max-dist (positions :guard1) (positions :scout1))
        guard2dist (max-dist (positions :guard2) (positions :scout1))]
    (as-> positions _
          (if (> guard1dist 2) (dissoc _ :guard1) _)
          (if (> guard2dist 2) (dissoc _ :guard2) _))))

(defn is-dead
  [positions]
  (or (= (positions :guard1) (positions :scout1))
      (= (positions :guard2) (positions :scout1))
      (= (positions :guard1) (positions :engineer1))
      (= (positions :guard2) (positions :engineer1))))

(defn update-status
  [w]
  (cond (= (w :status) "DEAD") w
        (is-dead (w :positions)) (assoc w :status "DEAD")
        (= (-> w :orders :guess) "73") (assoc w :status "WIN")
        true w))

(defn tick
  "Perform all tasks/orders related to a tick"
  [w]
  (let [orders (w :orders)
        t (w :tick)]
    (-> w
        (update-in [:positions :scout1] change-pos (orders :scout1))
        (update-in [:positions :engineer1] change-pos (orders :engineer1))
        (assoc-in [:positions :guard1] (nth guard1-route (mod (+ t (w :guard1-offset)) 10)))
        (assoc-in [:positions :guard2] (nth guard2-route (mod (+ t (w :guard2-offset)) 10)))
        update-status
        (assoc :orders {})
        (update :tick inc))))


(comment
  (tick @world)
  (debug)
  (update-order :scout1 "n")
  (println (info))
  )


(defn info
  [w]
  (str "
                          Info

------------------------------------------------------------

  Tick:   " (w :tick) "
  Status: " (w :status) "
"
(draw-map (visible-positions (w :positions)))
"
"
(if (= (-> w :positions :engineer1) (-> w :positions :server1)) (str "ACCESS TO SERVER: <host>/m1/" (w :token)) "")

"

DATA:
Tick:        " (w :tick) "
Status:      " (w :status) "
Engineer1:   " (-> w :positions :engineer1) "
Scout1:      " (-> w :positions :scout1) "
Guard1:      " (-> w :positions :guard1) "
Guard2:      " (-> w :positions :guard2) "
Server1:     " (-> w :positions :server1) "
Server url:  " (when (= (-> w :positions :engineer1) (-> w :positions :server1)) (str "<host>/m1/" (w :token))) "
"
))

(defn debug
  [w]
  (pr-str w))

(def run (atom false))

(defn start
  "Internal for resetting and starting the stage"
  []
  (when (not @run)
    (reset! run true)
    (reset-world)
    (future
      (loop []
        (swap! world tick)
        (Thread/sleep 1000)
        (when @run (recur))))
  "M1 is running"))

(defn stop
  "Internal for stopping the stage"
  []
  (reset! run false))

(comment
  (start)
  (debug)
  (stop))

(defn index
  []
  (str "
                     Mission M1
                     Kill server

------------------------------------------------------------

Greetings Agent

You are hereby activated for Mission M1: Kill server

References:
  * <host>/m1/info
  * <host>/m1/scout1/n
  * <host>/m1/scout1/s
  * <host>/m1/scout1/e
  * <host>/m1/scout1/w
  * <host>/m1/engineer1/n
  * <host>/m1/engineer1/s
  * <host>/m1/engineer1/e
  * <host>/m1/engineer1/w

  "))

(defn server1
  [args]
  (let [token (@world :token)]
    (cond (= (first args) "shutdown") (update-order :guess (second args))
          true (str "
          Server 1 

    <host>/m1/" token "/shutdown/<key>

  <key> is two digit number

TMP: " (str/join ", " args) "
  "))))


(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "info") (info @world)
        (= (first args) "start") (start)
        (= (first args) "stop") (stop)
        (= (first args) "debug") (debug @world)
        (= (first args) (@world :token)) (server1 (rest args))
        (= (first args) "scout1") (update-order :scout1 (second args))
        (= (first args) "engineer1") (update-order :engineer1 (second args))
        true "Invalid"))

(comment (api (@world :token) "shutdown" "73"))
(comment (println (api "info")))
(comment (println (api "debug")))

(comment
  (load-file ":cf:")
  (println (hackerio.apps.m1/api "start"))
  (println (hackerio.apps.m1/api))
  (println (hackerio.apps.m1/api "info"))
  (println (hackerio.apps.m1/api "debug"))
  (println (hackerio.apps.m1/api (@hackerio.apps.m1/world :token) "shutdown" "73"))
  (println (hackerio.apps.m1/api "stop")))
