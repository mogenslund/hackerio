; https://github.com/borodust/notalone

(in-package :hackerio)

(defun test-token-handler ()
  (format t "Connection to token handler works"))

(defun access-level (token)
  (cond ((string= token "abc-123") 1)
         (t 0)))
