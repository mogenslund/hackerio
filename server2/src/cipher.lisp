(in-package :hackerio)

(defun rotate (c n)
  ;Rotate *alphabet* others should be identity
  (if (position c *alphabet*)
    (nth (mod (+ (position c *alphabet*) n) (length *alphabet*))
         *alphabet*)
    c))

(defun encrypt (text)
  (concatenate
    'string
    (mapcar
      (lambda (x y) (rotate x y))
      (coerce text 'list)
      '(4 8 102 34 18 83 199 34 10 24 52 32 12 98 23 45 12 34 54 99 87 28 29 93 28 182 32 19 1 23 45))))

; (encrypt "Abc")

(defun decrypt (text)
  (concatenate
    'string
    (mapcar
      (lambda (x y) (rotate x (- (length *alphabet*) y)))
      (coerce text 'list)
      '(4 8 102 34 18 83 199 34 10 24 52 32 12 98 23 45 12 34 54 99 87 28 29 93 28 182 32 19 1 23 45))))

; (decrypt "EjG")
