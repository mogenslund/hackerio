(in-package :hackerio)

(defun get-temp-directory ()
  (or (uiop:getenv "TMPDIR")   ; Unix-like OSes
      (uiop:getenv "TEMP")     ; Windows
      (uiop:getenv "TMP")      ; Windows
      "/tmp"))                 ; Fallback on Unix-like OSes

(defvar logfile (merge-pathnames (make-pathname :name "hackerio" :type "log") 
                   (make-pathname :directory (list :absolute (get-temp-directory)))))

(defun log1 (s)
  (with-open-file (stream logfile :direction :output :if-exists :append :if-does-not-exist :create)
    (format stream "~A ~A~%" (subseq (format nil "~A" (local-time:universal-to-timestamp (get-universal-time))) 0 19) s)))

(defun get-log (n)
  (with-open-file (stream logfile)
    (let ((lines (loop for line = (read-line stream nil)
                       while line collect line)))
      (format nil "~{~A~%~}" (subseq lines (max 0 (- (length lines) n)))))))

; (log1 "My entry 1")
; (log1 "My entry 2")
; (log1 "My entry 3")
; (get-log 100)
