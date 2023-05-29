(in-package :hackerio)

(defvar guard-path
  (append (loop for n from 0 below 200 by 10 collect (+ n 500))
          (loop for n from 0 below 100 by 10 collect (- 700 n))
          (loop for n from 0 below 200 by 10 collect (+ n 600))
          (loop for n from 0 below 300 by 10 collect (- 800 n))
          (loop for n from 0 below 160 by 10 collect (+ 500 n))
          (loop for n from 0 below 160 by 10 collect (- 660 n))))

(defun guard ()
  (let* ((tick (mod (get-universal-time) 96))
         (ts (format nil "~2,'0d~2,'0d" (floor tick 4) (* (mod tick 4) 15)))
         (dist (nth tick guard-path)))
    (format nil "Time: ~A Distance: ~Am" ts dist)))
