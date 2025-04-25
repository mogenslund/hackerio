(ns dr.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [dr.game :as g]))

(defn -main
  []
  (println "Running Deft Recon...")
  (let [game (atom (g/create-game (edn/read-string (slurp (io/resource "map01.edn")))
                                  (slurp (io/resource "map01.txt"))))]
    (future
      (while true
        (swap! game g/game-tick)
        (Thread/sleep 1000)))
    (while true
      (let [in (read-line)
            out (swap! game #(g/game-input % in))]
        (when (not= (out :output) "") (println (out :output)))
        (when (= in "quit")
          (shutdown-agents)
          (System/exit 0))))))

