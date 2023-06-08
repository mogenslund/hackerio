(in-package :hackerio)

(defvar *drones* (make-hash-table :test 'equal))

(defun new-pos (pos direction dist)
  (cond ((string= direction "e") '(:lat (assoc
  )

; (new-pos '(:lat 52.3 :long 21.5 "e" 1000)) km
; (setf (getf '(:lat 1 :long 2) :long) 4)

(defun new-drone (city)
  (if (string= (string-downcase city) "warsaw")
    (let* ((id (princ-to-string (uuid:make-v4-uuid)))
           (data '(:id id :lat 52.21417 :long 21.04475 :speed 0 :direction "e" :signal "---" :timestamp (get-universal-time))))
      (setf (gethash id *drones*) data)
      id)
    "N/A"))

(defun update-drone-position (id)
  (let* ((drone (getf (gethash id *drones*))))
    (when drone
      (let* ((new-time (get-universal-time))
             (elapsed (- new-time (getf drone :timestamp)))
             (speed (getf drone :speed))
             (direction (getf drone :direction)))
        (cond ((string= direction "e") (
        
      ))))
; Max = 10 km/sec

; (setq d (new-drone "warsaw"))
; (new-drone "berlin")
; (getf (gethash d *drones*) :lat)
; (assoc (gethash d *drones*) :lat 4.5)


????
Drone

(setq d (list :id "abc-123" :lat 52.21417 :long 21.04475 :speed 0 :direction "e" :signal "---" :timestamp (get-universal-time)))

<host>/drone?deploy=warsaw -> abc-123
<host>/drone?id=abc-123&direction=n&speed=3

New drone will begin at coordinates to 52.21417, 21.04475 (Warsaw) with speed=0
When close enough to 55.75110, 37.61647 <host>/drone?id=abc-123 will return: Signal: a
Before:
Id: abc-123
Pos: 53.75323, 31.61453
Direction: e
Speed: 100 (km/h)
Signal: ---

When outside range: Delete



(/ (- 38.333 21.278) 1157)
East: 1 km = 0.014740709
(/ (- 54.988 52.367) 291.47)
North: 1 km = 0.008992342
Adjust speed to fit 1 minute on topspeed 100.

