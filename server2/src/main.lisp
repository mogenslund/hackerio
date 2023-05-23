(in-package :hackerio)

(defun mogens-test ()
  (write-line "Mogens Test loaded")
  (write-line (get-resource "mission1.txt"))
  (write-line *projectpath*))

(defun get-resource (filename)
  (format nil "~A" (uiop:read-file-string (format nil "~A/resources/~A" *projectpath* filename))))

(defun replace-host (request content)
  (let* ((protocol (if (string= (subseq (format nil "~A" (hunchentoot:server-protocol request)) 0 5) "HTTPS") "https" "http"))
         (host (format nil "~A://~A" protocol (hunchentoot:host request))))
    (format nil "~A" (cl-ppcre:regex-replace-all "<host>" content host))))

; (type-of (cl-ppcre:regex-replace-all "<host>" "abc <host>" "http://localhost:4242"))

(defun access-level1 (token)
  9)

; (access-level1 "abc-123")
; (hackerio:access-level1 "abc-123")
; (>= (access-level1 "abc-123") 0)

(defun start-server (&rest args)
  (declare (ignore args))
  (write-line "Starting server")

  (defvar *server* (make-instance 'hunchentoot:easy-acceptor :port 4242))

  (hunchentoot:define-easy-handler (test0-handler :uri "/test0") (param)
    (setf (hunchentoot:content-type*) "text/plain")
    (format nil "Test working~@[ ~A~]!" param))

  ; http://localhost:4242/test0?token=abc-123&param=abc

  (hunchentoot:define-easy-handler (msg-handler :uri "/msg") (to uid content)
    (setf (hunchentoot:content-type*) "text/plain")
    (cond (uid (get-response uid))
          (t (send-message to content))))

  ; (setq *t1* (dex:get "http://localhost:4242/msg?token=abc-123&to=test&content=abcdef123"))
  ; (dex:get (format nil "http://localhost:4242/msg?token=abc-123&uid=~A" *t1*))

  (hunchentoot:define-easy-handler (mission-handler :uri "/mission") (token id)
    (setf (hunchentoot:content-type*) "text/plain")
    (let* ((level (access-level1 token)))
       (cond ((string= id "tmp") (get-resource "mission1.txt"))
             ((and (>= level 0) (string= id "mission1")) (replace-host *request* (get-resource "mission1.txt")))
             ((and (>= level 1) (string= id "mission2")) (replace-host *request* (get-resource "mission2.txt")))
             ((and (>= level 1) (string= id "mission3")) (replace-host *request* (get-resource "mission3.txt")))
             ((and (>= level 1) (string= id "mission4")) (replace-host *request* (get-resource "mission4.txt")))
             ((and (>= level 1) (string= id "mission5")) (replace-host *request* (get-resource "mission5.txt")))
             ((and (>= level 1) (string= id "mission6")) (replace-host *request* (get-resource "mission6.txt")))
             (t (format nil "Mission ~A does not exist or too low access-level ~A" id level)))))

  ; http://localhost:4242/mission?token=abc-123&id=tmp
  ; http://localhost:4242/mission?token=abc-123&id=mission1
  ; http://localhost:4242/mission?token=abc-123&id=mission2

  (hunchentoot:define-easy-handler (contacts-handler :uri "/contacts") (id)
    (setf (hunchentoot:content-type*) "text/plain")
    (contacts :id id))

  ; http://localhost:4242/contacts?token=abc-123&id=2068
  ; http://localhost:4242/contacts?token=abc-123&id=2046


  (hunchentoot:define-easy-handler (contacts-list-handler :uri "/contacts-list") ()
    (setf (hunchentoot:content-type*) "text/plain")
    (contacts-list))

  ; http://localhost:4242/contacts-list

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

  (hunchentoot:start *server*))

(defun stop-server ()
  (write-line "Stopping server")
  (hunchentoot:stop *server*))
