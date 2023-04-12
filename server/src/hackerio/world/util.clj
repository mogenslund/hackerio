(ns hackerio.world.util)

(defn sq
  [x]
  (* x x))

(defn sqrt
  [x]
  (Math/sqrt x))

(defn coord-distance
  [pos1 pos2]
  (let [dx (- (:x pos2) (:x pos1))
        dy (- (:y pos2) (:y pos1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

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

(defn coord-move
  [origin target speed tics]
  (let [total-distance (coord-distance origin target)
        total-tics (if (zero? speed) 0 (/ total-distance speed))
        ratio (if (zero? total-tics) 0 (min 1 (/ tics total-tics)))
        x (+ (origin :x) (* (- (target :x) (origin :x)) ratio))
        y (+ (origin :y) (* (- (target :y) (origin :y)) ratio))]
    {:x x :y y}))

(comment (coord-move {:x 0 :y 0} {:x 0 :y 0} 100 1))
(comment (coord-move {:x 0 :y 0} {:x 5 :y 10} 100 1))
(comment (coord-move {:x 0 :y 0} {:x 5 :y 10} 1 2))
(comment (coord-move {:x 0 :y 0} {:x 5 :y 0} 1 2))
(comment (coord-move {:x 0 :y 0} {:x 0 :y -5} 1 2))

(defn dist
  [obj1 obj2]
  (sqrt (+ (sq (- (obj1 :x) (obj2 :x))) (sq (- (obj1 :y) (obj2 :y))))))

(comment (dist (get-object default-world "abc-123") (get-object default-world "abc-124")))

(defn move-object
  "One tick"
  [o]
  (let [new-pos (coord-move o {:x (o :target-x) :y (o :target-y)} (o :speed) 1)]
    (-> o
        (assoc :x (new-pos :x))
        (assoc :y (new-pos :y)))))

(comment
  (move-object {:x 0 :y 0 :speed 5 :target-x 0 :target-y 0})
  (move-object {:x 0 :y 0 :speed 5 :target-x 1 :target-y 0})
  (move-object {:x 0 :y 0 :speed 10 :target-x 20 :target-y 40})
  (move-object {:x 0 :y 0 :speed 5 :target-x 10 :target-y 0}))

