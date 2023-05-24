(in-package :hackerio)

;; (ql:quickload "cl-csv")

(defun read-csv-to-list (file-path)
  "Reads a CSV file and returns its contents as a list of lists."
  (with-open-file (stream file-path)
    (let ((csv-contents (cl-csv:read-csv stream)))
      csv-contents)))

(defvar *contacts* (rest (read-csv-to-list (concatenate 'string *projectpath* "/resources/contacts.csv"))))

(defun contacts (&key id)
  (let* ((hit (find-if (lambda (entry) (string= (first entry) (format nil "~A" id))) *contacts*)))
    (if hit
      (format nil "~%ID: ~A~%FIRSTNAME: ~A~%LASTNAME: ~A~%BIRTHDATE: ~A~%CITY: ~A~%"
              (nth 0 hit) (nth 1 hit) (nth 2 hit) (nth 3 hit) (nth 4 hit))
      (format nil "~%No hits for id: ~A" id))))

(defun contacts-list ()
  (reduce (lambda (all entry) (format nil "~A~A ~A ~A~%" all (nth 0 entry) (nth 1 entry) (nth 2 entry))) *contacts* :initial-value ""))

; (contact :id "2046")
; (contact :id 2046)
; (contact :id "8046")
; (contact-list)
