(ns dr.game
  (:require [clojure.string :as str]
            [dr.util :as util]))

;; actions should be a list. One is executed and removed in each tick.

(defn move-action
  [game agt loc]
  (assoc-in game [:agents agt :loc] loc)) 

(defn execute-action
  [game agt]
  (let [cmd (get-in game [:agents agt :command])]
    (assoc-in
      (cond (nil? cmd) game 
            (= (count cmd) 1) (move-action game agt cmd)
            true game)
      [:agents agt :command]
      nil)))

(defn game-tick
  [game]
  (-> game 
      ;; 1. Handle commands
      (execute-action "s")
      ;;(execute-action :engineer)
      ;; 2. Handle dead
      ;; 3. Dead discovered => Alarm count down
      ;; 4. Any revealed? => (stop "MISSION FAILED: One of your men was captured!")
      ;; Remember to eval to state
      (update :tics inc)))

(defn move-command
  [game agt loc]
  (if (get-in game [:agents agt])
    (assoc (assoc-in game [:agents agt :command] (str/upper-case loc)) :output "")
    (assoc game :output "")))

(defn game-input
  [game command]
  (cond (= command "test") (assoc game :output (str "TICS: " (game :tics)))
        (= command "debug") (assoc game :output (assoc game :map nil :output "")) 
        (= command "map") (assoc game :output (util/generate-map (game :map) (reduce (fn [acc [k v]] (assoc acc k (:loc v))) {} (game :agents))))
        (re-matches #"\w \w" command) (move-command game (subs command 0 1) (subs command 2 3))
        true game))

(defn create-game
  [initial battlemap]
  (assoc initial
         :tics 0
         :output "Game Created"
         :map battlemap))

