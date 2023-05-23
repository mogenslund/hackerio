(in-package :hackerio)

(defvar *post-office* (make-hash-table :test 'equal))

(defun generate-uid ()
  (uuid:make-v4-uuid))

(defun generate-response (recipient message)
  (cond ((string= recipient "test") (format nil "My response is no to your message:~%~A" message))
        (t "Empty response")))

(defun send-message (recipient message)
  (let* ((uid (princ-to-string (uuid:make-v4-uuid))))
    (setf (gethash uid *post-office*)
          (list :recipient recipient :message message :time (+ (get-universal-time) 5 (random 10))))
    uid))

(defun get-response (uid)
  (let* ((response (gethash uid *post-office*)))
    (cond ((not response) "Message does not exist")
          ((> (getf response :time) (get-universal-time)) "Response not ready")
          (t (progn
               (remhash uid *post-office*)
               (generate-response (getf response :recipient) (getf response :message)))))))

; (setq *t1* (send-message "test" "Msg1"))
; (get-response *t1*)
; (get-response "nan")
