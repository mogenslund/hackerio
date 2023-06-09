(defsystem hackerio
  :author "Mogens Lund <salza@salza.dk>"
  :maintainer "Mogens Lund <salza@salza.dk>"
  :license "MIT"
  :homepage "https://github.com/mogenslund/hackerio"
  :version "0.1"
  :depends-on (:hunchentoot :uuid :cl-csv :cl-ppcre :sha1 :dexador :alexandria :uiop)
  :components ((:module "src"
                :serial t
                :components
                ((:file "config")
                 (:file "logging")
                 (:file "messages")
                 (:file "contacts")
                 (:file "tracker")
                 (:file "sat1")
                 (:file "drone")
                 (:file "cipher")
                 (:file "token-handler")
                 (:file "main"))))
  :description "API Based Hacker Game"
  :long-description
  #.(uiop:read-file-string
     (uiop:subpathname *load-pathname* "README.md"))
  :in-order-to ((test-op (test-op hackerio-test))))

;; SLIME:
;; (ql:quickload :hackerio)
;; (hackerio:main)
