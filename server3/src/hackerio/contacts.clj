(ns hackerio.contacts
  (:require [clojure.string :as str]
            [hackerio.util :as util]))

(def data (rest (util/read-csv-to-list (util/resource "/contacts.csv"))))

(defn contact
  [& {:keys [id]}]
  (let [hit (first (filter #(= (first %) (str id)) data))]
    (if hit
      (apply format (concat ["\nID: %s\nFIRSTNAME: %s\nLASTNAME: %s\nBIRTHDATE: %s\nCITY: %s\n"] hit))
      (format "\nNot hits for id: %s" id))))

(defn contact-list
  []
  (str "\n" (str/join "\n" (map #(format "%s %s %s" (nth % 0) (nth % 1) (nth % 2)) data)) "\n"))

; (println (contact :id "2046"))
; (println (contact :id 2046))
; (println (contact :id "8046"))
; (println (contact-list))
