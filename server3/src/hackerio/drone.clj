(ns hackerio.core
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(def drones (atom {}))
; (.toString (java.util.UUID/randomUUID))

; Local geometry km/degree
(def geometry {:lat 67.84 :long 111.21})

; Drone in Warehouse near kreml
; n=0 ne=45 e=90 se=135 s=180 sw=225 w=270 nw=315

(defn move
  [pos direction dist]
  (let [converted-direction (if (number? direction) direction ({"n" 0 "ne" 45 "e" 90 "se" 135 "s" 180 "sw" 225 "w" 270 "nw" 315} direction))
        radians (Math/toRadians converted-direction)
        dlat (* dist (Math/cos radians) (/ 1 (geometry :lat)))
        dlong (* dist (Math/sin radians) (/ 1 (geometry :long)))]
    (assoc pos
      :lat (+ (pos :lat) dlat) :long (+ (pos :long) dlong))))

; (move {:lat 52.33 :long 21.10} 0 67)
; (move {:lat 52.33 :long 21.10} "n" 67)
; (move {:lat 52.33 :long 21.10} 90 111)

(defn dist
  [pos1 pos2]
  (let [dx (* (- (pos2 :lat) (pos1 :lat)) (geometry :lat))
        dy (* (- (pos2 :long) (pos1 :long)) (geometry :long))]
    (math/sqrt (+ (* dx dx) (* dy dy)))))

(defn new-moscow-drone
  []
  (let [id (.toString (java.util.UUID/randomUUID))
        timestamp (System/currentTimeMillis)]
    (swap! drones assoc id {:id id :type :moscow
                            :base-lat 55.7623067807219 :base-long 37.56398181116498
                            :lat 55.7623067807219 :long 37.56398181116498
                            :range 15 :battery (* 10 60 1000) :direction "e" :speed 0
                            :sensor "" :base-timestamp timestamp :timestamp timestamp})
    id))

(defn update-drone-info
  [drone] ; To be used with swap
  (let [new-timestamp (System/currentTimeMillis)
        elapsed (- new-timestamp (drone :timestamp))
        total-elapsed (- new-timestamp (drone :base-timestamp))
        delta-distance (/ (* (drone :speed) elapsed) (* 60 60 1000))
        new-pos (move drone (drone :direction) delta-distance)]
    (assoc drone :lat (new-pos :lat)
                 :long (new-pos :long)
                 :timestamp new-timestamp)))

(defn drone-speed
  [id speed]
  (when (@drones id) (swap! drones update id update-drone-info))
  (when (@drones id) (swap! drones assoc :speed speed)))

(defn drone-str
  [id]
  (if-let [drone (@drones id)]
    (format (str "\nID: %s\n"
                 "LATITUDE: %s\n"
                 "LONGITUDE: %s\n"
                 "SPEED: %s\n"
                 "DIRECTION: %s\n"
                 "BATTERY: %s\n"
                 "DISTANCE FROM BASE: %s\n")
            (drone :id)
            (drone :lat)
            (drone :long)
            (drone :speed)
            (drone :direction)
            (/ (- (drone :battery) (- (drone :timestamp) (drone :base-timestamp))) (* 60.0 1000))
            (str (dist drone {:lat (drone :base-lat) :long (drone :base-long)})))
    "Drone lost" ))

(defn handle-drone
  [id]
  (when (@drones id)
    (let [drone ((swap! drones update id update-drone-info) id)]
      (cond (> (- (drone :timestamp) (drone :base-timestamp)) (drone :battery)) (swap! drones dissoc id)
            (> (dist drone {:lat (drone :base-lat) :long (drone :base-long)}) (drone :range)) (swap! drones dissoc id)
            true drone)))
  (drone-str id))

(defn new-drone
  [& {:keys [type]}]
  (cond (= type "moscow") (new-moscow-drone)))

(defn drone
  [& {:keys [type id speed direction]}]
  (if (= type "moscow")
    (new-moscow-drone)
    (when id
      (handle-drone id)
      (when (@drones id)
        (when speed (swap! drones assoc-in [id :speed] (max (min (Integer/parseInt speed) 100) 0)))
        (when direction (swap! drones assoc-in [id :direction] direction)))
      (drone-str id))))

(drone :id "a")


(comment
  (def id1 (drone :type "moscow"))
  (println (drone :id id1))
  (println (drone :id id1 :speed "140" :direction "n"))
  (println (handle-drone id2))
)
                 

; SOURCE: 55.7623067807219, 37.56398181116498
; TARGET: 55.7527493805092, 37.61740444728526
