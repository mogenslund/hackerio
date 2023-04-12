(ns hackerio.world.app
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hackerio.world.data :as data]
            [hackerio.world.model :as model]
            ))

(def running (atom false))

(def world (atom {}))

(defn action
  [args]
  (let [id (nth args 0)]
    (cond (nil? (model/get-object @world id)) "NaN" 
          (= (count args) 1) (model/info @world id) 
          (= (nth args 1) "target") (swap! world model/change-target id {:x (Double/parseDouble (nth args 2)) :y (Double/parseDouble (nth args 3))})
          (= (nth args 1) "speed") (swap! world model/change-speed id (Double/parseDouble (nth args 2)))
          true "NaN")))
(comment (println (get-object "a")))
(comment (println (action ["a"])))
(comment (println (action ["abc-123"])))
(comment (println (action ["abc-123" "speed" "20"])))

(comment (sensor-radiation default-world "abc-123"))
(comment (sensor-radiation default-world "abc-124"))
(comment (sensor-radiation default-world "abc-125"))

(defn start
  []
  (reset! running true)
  (reset! world data/default-world)
  (future 
    (while @running
      (let [t0 (System/currentTimeMillis)]
        (swap! world model/tick)
        ;; Sleep until 1 sec after last iteration
        (Thread/sleep (max 0 (- (System/currentTimeMillis) t0 -1000)))))))

(defn stop
  []
  (reset! running false))

(defn debug
  []
  @world)

(defn api
  [& args]
  (cond (nil? (first args)) (model/index)
        (= (first args) "????") (second args)
        (= (first args) "start") (start)
        (= (first args) "stop") (stop)
        (= (first args) "debug") (debug)
        (re-find #"-" (first args)) (action args)
        ;(= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))

(comment
  (do
    (defn p [m] (println "\n") (clojure.pprint/pprint m) (println ""))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/util.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/data.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/model.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/server/src/hackerio/world/app.clj"))
    (ns user (:require [hackerio.world.data :as util] [hackerio.world.data :as data] [hackerio.world.model :as model] [hackerio.world.app :as app])))

  (app/api "start")
  (p (app/api "debug"))
  (p (app/api "abc-123"))
  (p (app/api "abc-123" "speed" "2"))
  (p (app/api "abc-123" "speed" "0"))
  (p (app/api "abc-123" "direction" -2.34 45))
  (p (app/api "abc-123")) ; Info. Pos and sensor data
  (app/api "stop")

  (p (slurp "http://localhost/contacts"))
  (p (slurp "http://localhost/world/start"))
  (p (slurp "http://localhost/world/debug"))
  (p (slurp "http://localhost/world/abc-123"))
  (p (slurp "http://localhost/world/abc-123/target/100/120"))
  (p (slurp "http://localhost/world/abc-123/target/-0.12/-43"))
  (p (slurp "http://localhost/world/abc-123/speed/1.0"))
  (p (slurp "http://localhost/world/stop")))




