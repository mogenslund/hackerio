(ns hackerio.util
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def logfile (str (System/getProperty "java.io.tmpdir") "hackerio.log"))

(defn log
  [s]
  (let [calendar (java.util.Calendar/getInstance (java.util.TimeZone/getTimeZone "Europe/Copenhagen"))
        t (format "%1$tY-%1$tm-%1$td %1$tT" calendar)
        entry (str t " " s)]
    (println entry)
    (spit logfile (str entry "\n") :append true)))

(defn get-log
  [n]
  (let [entries (str/split-lines (slurp logfile))]
    (str/join "\n" (drop (- (count entries) n) entries))))

(def resource-path 
  (when (.exists (io/file "/Users/mogensbrodsgaardlund/proj/hackerio/world/resources/"))
    "/Users/mogensbrodsgaardlund/proj/hackerio/world/resources/"))

(defn resource
  [path]
  (if resource-path
    (str resource-path path)
    (io/resource path)))

(defn read-csv-to-list
  "Takes file name and reads data."
  [path]
  (with-open [reader (io/reader path)]
    (doall
      (csv/read-csv reader))))

(defn map-val
  [fun m]
  (into {} (map (fn [[k v]] [k (fun v)]) m)))

(defn coord-distance
  [pos1 pos2]
  (let [dx (- (:x pos2) (:x pos1))
        dy (- (:y pos2) (:y pos1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn coord-move
  [origin target speed tics]
  (let [total-distance (coord-distance origin target)
        total-tics (if (zero? speed) 0 (/ total-distance speed))
        ratio (min 1 (/ tics total-tics))
        x (+ (:x origin) (* (- (:x target) (:x origin)) ratio))
        y (+ (:y origin) (* (- (:y target) (:y origin)) ratio))]
    {:x x :y y}))

(comment (coord-move {:x 0 :y 0} {:x 5 :y 10} 100 1))
(comment (coord-move {:x 0 :y 0} {:x 5 :y 10} 1 2))
(comment (coord-move {:x 0 :y 0} {:x 5 :y 0} 1 2))
(comment (coord-move {:x 0 :y 0} {:x 0 :y -5} 1 2))
