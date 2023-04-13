(defsystem hackerio
  :author "Mogens Lund <salza@salza.dk>"
  :maintainer "Mogens Lund <salza@salza.dk>"
  :license "MIT"
  :homepage "https://github.com/mogenslund/hackerio"
  :version "0.1"
  :depends-on (:hunchentoot :uuid :cl-csv)
  :components ((:module "src"
                :serial t
                :components
                ((:file "main")
                 (:file "messages")
                 (:file "contacts")
                 (:file "token-handler"))))
  :description "API Based Hacker Game"
  :long-description
  #.(uiop:read-file-string
     (uiop:subpathname *load-pathname* "README.md"))
  :in-order-to ((test-op (test-op hackerio-test))))
