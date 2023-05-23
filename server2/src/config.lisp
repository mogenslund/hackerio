(defpackage hackerio
  (:use :cl :hunchentoot)
  (:export :start-server :stop-server :mogens-test :*projectpath*))

(in-package :hackerio)
(defvar *projectpath* "~/proj/hackerio/server2")

;; (ql:quickload :hackerio)
;; (hackerio:start-server)
;; (hackerio:stop-server)
