(ns hackerio.util
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def resource-path nil)
; (def resource-path "/Users/mogensbrodsgaardlund/proj/hackerio/server3/resources/")

(defn resource
  [path]
  (if resource-path
    (str resource-path path)
    (io/resource path)))

(defn read-csv-to-list
  "Takes file name and reads data."
  [path]
  (with-open [reader (io/reader path)]
    (doall
      (csv/read-csv reader))))

