(ns hackerio.util
  (:require [clojure.string :as str]
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

(defn my-hash
  [s]
  (Long/toHexString (+ 9000000000 (hash s))))

(def solution-hashes ["1bad726a1" "209ad6376" "294da68db" "1e094cf4b" "1bfd60208" "1bcd9b155"])

(def mission-hashes ["277f53b43" "1fcbaaf06" "215b3d3ca" "27cc45453" "1e8015feb" "20d21bbb4"])

(def admin-hash "2780514ad")

(defn next-mission
  [solution]
  (let [mission (or (re-find #"mission\d+" solution) "mission0")
        missionnr (Integer/parseInt (re-find #"\d+" mission))]
    (println mission missionnr)
    (when (and (<= 1 missionnr (count solution-hashes)) (= (solution-hashes (dec missionnr)) (my-hash solution)))
      (str "<host>/mission" (inc missionnr) "-" (my-hash (str "next-" solution))))))
