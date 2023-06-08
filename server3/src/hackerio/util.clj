(ns hackerio.util
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def logfile (str (System/getProperty "java.io.tmpdir") "hackerio.log"))

(defn log
  [s]
  (let [calendar (java.util.Calendar/getInstance (java.util.TimeZone/getTimeZone "Europe/Copenhagen"))
        t (format "%1$tY-%1$tm-%1$td %1$tT" calendar)
        entry (str t " " s)]
    (println entry)
    (spit logfile (str entry "\n") :append true)))

(defn get-log
  [n]
  (let [entries (str/split-lines (slurp logfile))]
    (str/join "\n" (drop (- (count entries) n) entries))))

(def resource-path 
  (when (.exists (io/file "/Users/mogensbrodsgaardlund/proj/hackerio/server3/resources/"))
    "/Users/mogensbrodsgaardlund/proj/hackerio/server3/resources/"))

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

