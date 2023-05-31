(in-package :hackerio)

(defun rand-coord ()
  (format nil "~A,~A: ~A"
          (/ (random 10000000) 100000.0)
          (/ (random 10000000) 100000.0)
          (random 500)))

(defun sat1-data ()
  (format nil "~{~A~%~}"
          (alexandria:shuffle
            (append 
                    (loop for i from 1 to 1200 collect (rand-coord))
                    (list "55.69599,12.56716: 456")))))

; (sat1-data)


