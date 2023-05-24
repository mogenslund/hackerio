(in-package :hackerio)

(defvar *post-office* (make-hash-table :test 'equal))

(defun generate-uid ()
  (uuid:make-v4-uuid))

(defun generate-response (to msg)
  (cond ((string= to "test") (format nil "My response is no to your message:~%~A" msg))
        ((and (string= to "hq") (string= msg "1966-12-11")) "SUCCESS\nYou have completed Mission 1")
        ((string= to "hq") "Not the answer we are looking for.")
        (t "Empty response")))

(defun send-message (&key to msg)
  (let* ((id (princ-to-string (uuid:make-v4-uuid))))
    (setf (gethash id *post-office*)
          (list :to to :msg msg :time (+ (get-universal-time) 5 (random 10))))
    id))

(defun get-response (&key id)
  (let* ((response (gethash id *post-office*)))
    (cond ((not response) "Message does not exist")
          ((> (getf response :time) (get-universal-time)) "Response not ready")
          (t (progn
               (remhash id *post-office*)
               (generate-response (getf response :to) (getf response :msg)))))))

; (setq *t1* (send-message :to "test" :msg "Msg1"))
; (get-response :id *t1*)
; (get-response :id "nan")
