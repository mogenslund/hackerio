;; sbcl
;; (load "core.lisp")
;;
;; (sb-posix:chdir (format nil "~Aproj/hackerio/server2" (user-homedir-pathname)))
;; (load (format nil "~A/core.lisp" (sb-posix:getcwd)))

;; (load (merge-pathnames "quicklisp/setup.lisp" (user-homedir-pathname)))

(defpackage hackerio
  (:use :cl :hunchentoot)
  (:export :main))

(in-package :hackerio)

(defun get-resource (filename)
  (format nil "~A" (uiop:read-file-string (format nil "~A/resources/~A" (sb-posix:getcwd) filename))))

(defun main (&rest args)
  (declare (ignore args))

  (defvar *server* (make-instance 'hunchentoot:easy-acceptor :port 4242))

  (hunchentoot:define-easy-handler (test0 :uri "/test0") (param)
    (setf (hunchentoot:content-type*) "text/plain")
    (format nil "Test working~@[ ~A~]!" param))

  (hunchentoot:define-easy-handler (msg :uri "/msg") (to uid)
    (cond ((eq (hunchentoot:request-method *request*) :post)
             (let* ((post-body (raw-post-data :force-text t)))
               (setf (hunchentoot:content-type*) "text/plain")
               (send-message to post-body)))
          ((eq (hunchentoot:request-method *request*) :get)
               (setf (hunchentoot:content-type*) "text/plain")
               (get-response uid))))
; (defparameter *t1* (dex:post "http://localhost:4242/msg?to=test" :headers `(("Content-Type" . "text/plain") ("token" . "abc-123")) :content "My message"))
; (dex:get (format nil "http://localhost:4242/msg?uid=~A" *t1*) :headers `(("Content-Type" . "text/plain") ("token" . "abc-123")) :content "My message")

  (hunchentoot:define-easy-handler (mission :uri "/mission") (id)
    (setf (hunchentoot:content-type*) "text/plain")
    (let* ((level (access-level (hunchentoot:header-in "token" *request*))))
       (cond ((and (>= level 0) (string= id "mission1")) (get-resource "mission1.txt"))
             ((and (>= level 1) (string= id "mission2")) (get-resource "mission2.txt"))
             ((and (>= level 1) (string= id "mission3")) (get-resource "mission3.txt"))
             (t (format nil "Mission ~A does not exist or too low access-level ~A" id level)))))

  (hunchentoot:define-easy-handler (contacts :uri "/contacts") (id)
    (setf (hunchentoot:content-type*) "text/plain")
    (if id
      (contact id)
      (contact-list)))

;(dex:get "http://localhost:4242/mission?id=mission1" :headers `(("content-type" . "text/plain") ("token" . "abc-123")))
;(dex:get "http://localhost:4242/mission?id=mission2" :headers `(("content-type" . "text/plain") ("token" . "abc-123")))
;(dex:get "http://localhost:4242/mission?id=mission2" :headers `(("content-type" . "text/plain") ("token" . "nan")))

  (hunchentoot:define-easy-handler (test1 :uri "/test1") (a)
    (setf (hunchentoot:content-type*) "text/plain")
    (format nil "Request: ~A~%Method: ~A~%A: ~A~%Token: ~A~%Access level: ~A~%Host: ~A://~A~%Message: ~A"
                *request*
                (hunchentoot:request-method *request*)
                a
                (hunchentoot:header-in "token" *request*)
                (access-level (hunchentoot:header-in "token" *request*))
                (hunchentoot:server-protocol *request*)
                (hunchentoot:host *request*)
                (hunchentoot:raw-post-data :force-text t)))
;(dex:post "http://localhost:4242/test1?a=1" :headers `(("content-type" . "text/plain") ("token" . "abc-123")) :content "My message")

  (hunchentoot:start *server*))


;(ql:quickload '("hunchentoot"))

;(defun hello ()
;   (format nil "Hello, it works!"))
;
;(push
;  (hunchentoot:create-prefix-dispatcher "/hello" #'hello)
;  hunchentoot:*dispatch-table*)
;
;(defvar *server* (make-instance 'hunchentoot:easy-acceptor :port 4242))
;(hunchentoot:start *server*)
;
;(hunchentoot:define-easy-handler (say-yo :uri "/yo") (name)
;  (setf (hunchentoot:content-type*) "text/plain")
;  (format nil "Hey~@[ ~A~]!" name))
;
;(hunchentoot:define-easy-handler (mission1 :uri "/mission1") ()
;  (setf (hunchentoot:content-type*) "text/plain")
;  (format nil "~A" (uiop:read-file-string (format nil "~A/mission1.txt" (sb-posix:getcwd)))))
;
;(hunchentoot:define-easy-handler (test1 :uri "/test1") ()
;  (setf (hunchentoot:content-type*) "text/plain")
;  (format nil "~A/mission1" (sb-posix:getcwd)))

; (ql:quickload "dexador")
; (dex:get "http://localhost:4242/yo?name=Alice")
; (dex:get "http://localhost:4242/mission1")
; (dex:post "http://localhost:4242/test1" :headers `(("Content-Type" . "text/plain")) :content "My message")
; (dex:get "http://localhost:4242/test0?param=123")
; (dex:post "http://localhost:4242/msg?to=test" :headers `(("Content-Type" . "text/plain")) :content "My message")
; (dex:get "http://localhost:4242/contacts?id=2068")
; (dex:get "http://localhost:4242/contacts")
; (hunchentoot:stop *server*)

