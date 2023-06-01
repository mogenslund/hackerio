(in-package :hackerio)

(defun mogens-test ()
  (write-line "Mogens Test loaded")
  (write-line (get-resource "mission1.txt"))
  (write-line *projectpath*))

(defun get-resource (filename)
  (format nil "~A" (uiop:read-file-string (format nil "~A/resources/~A" *projectpath* filename))))

(defun replace-host (request token content)
  (let* ((protocol (if (string= (subseq (format nil "~A" (hunchentoot:server-protocol request)) 0 5) "HTTPS") "https" "http"))
         (host (format nil "~A://~A" protocol (hunchentoot:host request))))
    (format nil "~A" (cl-ppcre:regex-replace-all "<token>" (cl-ppcre:regex-replace-all "<host>" content host) (or token "...")))))

(defun is-admin (admin)
  (and admin (string= admin "mogens8391")))

; (type-of (cl-ppcre:regex-replace-all "<host>" "abc <host>" "http://localhost"))
; (format nil "~A" (cl-ppcre:regex-replace-all "<token>" (cl-ppcre:regex-replace-all "<host>" "<host>asbc<token>" "localhost") (or "abc-123" "...")))
; (format nil "~A" (cl-ppcre:regex-replace-all "<token>" (cl-ppcre:regex-replace-all "<host>" "<host>asbc<token>" "localhost") (or nil "...")))

(defun access-level1 (token)  9)

; (access-level1 "abc-123")
; (hackerio:access-level1 "abc-123")
; (>= (access-level1 "abc-123") 0)

(defmacro file-handler (handler-name)
  `(hunchentoot:define-easy-handler (,(intern (concatenate 'string (string handler-name) "-handler")) :uri ,(concatenate 'string "/" (string handler-name))) (admin)
     (when (not (is-admin admin))
       (log1 (format nil "FILE ~A" ,(string handler-name))))
     (setf (hunchentoot:content-type*) "text/plain")
     (replace-host *request* nil (get-resource ,(concatenate 'string (string handler-name) ".txt")))))

(defun start-server (&rest args)
  (declare (ignore args))
  (write-line "Starting server")

  (defvar *server* (make-instance 'hunchentoot:easy-acceptor :port 80))

  (hunchentoot:define-easy-handler (test0-handler :uri "/test0") (param)
    (setf (hunchentoot:content-type*) "text/plain")
    (format nil "Test working~@[ ~A~]!" param))

  (hunchentoot:define-easy-handler (log-handler :uri "/log") (admin n)
    (setf (hunchentoot:content-type*) "text/plain")
    (if (is-admin admin)
      (get-log (or n 200))
      "Not allowed"))

  ; http://localhost/test0?token=abc-123&param=abc

  (hunchentoot:define-easy-handler (msg-handler :uri "/msg") (to id msg admin)
    (when (not (is-admin admin)) (log1 (format nil "MSG ~A ~A ~A" id to msg)))
    (setf (hunchentoot:content-type*) "text/plain")
    (cond (id (get-response :id id))
          ((and to msg) (send-message :to to :msg msg))
          (t (replace-host *request* nil (get-resource "msg.txt")))))

  (hunchentoot:define-easy-handler (tracker-handler :uri "/tracker") (id)
    (setf (hunchentoot:content-type*) "text/plain")
    (cond ((string= id "guard") (guard))
          (t (replace-host *request* nil (get-resource "tracker.txt")))))
  ; http://localhost/tracker
  ; http://localhost/tracker?id=guard

  (hunchentoot:define-easy-handler (sat1-handler :uri "/sat1/data") ()
    (setf (hunchentoot:content-type*) "text/plain")
    (sat1-data))
  (file-handler "sat1")
  ; http://localhost/sat1
  ; http://localhost/sat1/data

  (hunchentoot:define-easy-handler (index-handler :uri "/") (token)
    (setf (hunchentoot:content-type*) "text/plain")
    (replace-host *request* token (get-resource "index.txt")))

  (file-handler "mission1")
  (file-handler "mission2")
  (file-handler "mission3")
  (file-handler "mission4")
  (file-handler "mission5")
  (file-handler "mission6")
  (file-handler "mission7")
  (file-handler "file1459382")
  (file-handler "file3423121")
  ; http://localhost/mission1
  ; http://localhost/mission2
  ; http://localhost/mission3
  ; http://localhost/mission4
  ; http://localhost/mission5
  ; http://localhost/mission6
  ; http://localhost/mission7

  (hunchentoot:define-easy-handler (contacts-handler :uri "/contacts") (id)
    (setf (hunchentoot:content-type*) "text/plain")
    (contacts :id id))

  ; http://localhost/contacts?id=8068
  ; http://localhost/contacts?id=2068

  (hunchentoot:define-easy-handler (contacts-list-handler :uri "/contacts-list") ()
    (setf (hunchentoot:content-type*) "text/plain")
    (contacts-list))
  ; http://localhost/contacts-list

  (file-handler "mission1solution")
  ; http://localhost/mission1solution

  (hunchentoot:define-easy-handler (test1-handler :uri "/test1") (a)
    (setf (hunchentoot:content-type*) "text/plain")
    (format nil "Request: ~A~%Method: ~A~%A: ~A~%Token: ~A~%Access level: ~A~%Host: ~A://~A~%Message: ~A"
                *request*
                (hunchentoot:request-method *request*)
                a
                (hunchentoot:header-in "token" *request*)
                (access-level1 (hunchentoot:header-in "token" *request*))
                (hunchentoot:server-protocol *request*)
                (hunchentoot:host *request*)
                (hunchentoot:raw-post-data :force-text t)))
  ; http://localhost/test1?token=abc-123&a=1000

  (hunchentoot:start *server*))

(defun stop-server ()
  (write-line "Stopping server")
  (hunchentoot:stop *server*))
