(ns hackerio.drone
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(def drones (atom {}))
; (.toString (java.util.UUID/randomUUID))

; Local geometry km/degree
(def geometry {:lat 67.84 :long 111.21})

(def locale (java.util.Locale. "en" "US"))

; Drone in Warehouse near kreml
; n=0 ne=45 e=90 se=135 s=180 sw=225 w=270 nw=315

(defn move
  [pos direction dist]
  (let [converted-direction (if (re-matches #"-?\d+" direction)
                              (mod (Integer/parseInt direction) 360)
                              ({"n" 0 "ne" 45 "e" 90 "se" 135 "s" 180 "sw" 225 "w" 270 "nw" 315} direction))
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
                            :base-pos {:lat 55.7623067807219 :long 37.56398181116498}
                            :pos {:lat 55.7623067807219 :long 37.56398181116498}
                            :target-pos {:lat 55.7527493805092 :long 37.61740444728526}
                            :range 15 :battery (* 10 60 1000) :direction "e" :speed 0
                            :sensor "" :base-timestamp timestamp :timestamp timestamp})
    id))

(defn moscow-sensor
  [timestamp]
  (str (nth "attack2045" (mod (quot timestamp 1000) 10))))

(defn update-drone-info
  [drone] ; To be used with swap
  (let [new-timestamp (System/currentTimeMillis)
        elapsed (- new-timestamp (drone :timestamp))
        total-elapsed (- new-timestamp (drone :base-timestamp))
        delta-distance (/ (* (drone :speed) elapsed) (* 60 60 1000))
        new-pos (move (drone :pos) (drone :direction) delta-distance)
        target-distance (dist new-pos (drone :target-pos))
        sensor (if (<= target-distance 1) (moscow-sensor new-timestamp) "N/A")]
    (assoc drone :pos new-pos
                 :timestamp new-timestamp
                 :sensor sensor)))

(defn drone-speed
  [id speed]
  (when (@drones id) (swap! drones update id update-drone-info))
  (when (@drones id) (swap! drones assoc :speed speed)))

(defn drone-str
  [id]
  (if-let [drone (@drones id)]
    (java.lang.String/format locale
               (str "\nID: %s\n"
                    "LATITUDE: %.6f\n"
                    "LONGITUDE: %.6f\n"
                    "SPEED: %s\n"
                    "DIRECTION: %s\n"
                    "BATTERY: %.2f\n"
                    "SENSOR: %s\n"
                    "DISTANCE FROM BASE: %.3f\n"
                    "DISTANCE FROM TARGET: %.3f\n")
            (into-array Object [
            (drone :id)
            (-> drone :pos :lat)
            (-> drone :pos :long)
            (drone :speed)
            (drone :direction)
            (/ (- (drone :battery) (- (drone :timestamp) (drone :base-timestamp))) (* 60.0 1000))
            (drone :sensor)
            (dist (drone :pos) (drone :base-pos))
            (dist (drone :pos) (drone :target-pos))]))
    "Drone lost" ))

(defn handle-drone
  [id]
  (when (@drones id)
    (let [drone ((swap! drones update id update-drone-info) id)]
      (cond (> (- (drone :timestamp) (drone :base-timestamp)) (drone :battery)) (swap! drones dissoc id)
            (> (dist (drone :pos) (drone :base-pos)) (drone :range)) (swap! drones dissoc id)
            true drone)))
  (drone-str id))

(defn drone
  [& {:keys [type id speed direction]}]
  (if (= type "moscow")
    (new-moscow-drone)
    (when id
      (handle-drone id)
      (when (@drones id)
        (when speed (swap! drones assoc-in [id :speed] (max (min (Integer/parseInt speed) 200) 0)))
        (when direction (swap! drones assoc-in [id :direction] direction)))
      (drone-str id))))

(comment
  (def id1 (drone :type "moscow"))
  (println (drone :id id1))
  (println (drone :id id1 :speed "200" :direction "e"))
  (println (drone :id id1 :speed "0" :direction "e"))
  (println (handle-drone id1))
  (@drones id1)
)
                 

; SOURCE: 55.7623067807219, 37.56398181116498
; TARGET: 55.7527493805092, 37.61740444728526
