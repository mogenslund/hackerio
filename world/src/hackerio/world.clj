(ns hackerio.world
  (:require [clojure.string :as str]
            [hackerio.units :as units]
            [hackerio.game :as game]
            [hackerio.util :as util]))

(def tick-sec 2)
(def state (atom {:ticks 0}))

(comment
  (do
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/world/src/hackerio/units.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/world/src/hackerio/game.clj"))
    (load-file (str (System/getProperty "user.home") "/proj/hackerio/world/src/hackerio/util.clj"))))

;(defn mapify
;  "Takes a list of maps and makes it into a map
;  with id as key."
;  [l]
;  (reduce #(assoc %1 (%2 :id) %2) {} l))

;(comment (mapify [{:id "a" :speed 20} {:id "b" :speed 40}]))

(defn unit-ids
  [user]
  (map :id (filter #(and (map? %) (= (% :owner) user)) (vals @state))))

(defn status
  [user]
  (str/join "\n"
    (map units/summary (filter #(and (map? %) (= (% :owner) user)) (vals @state)))))

(defn pstatus [user] (println (str "\n" (status user))))

(defn set-target!
  [id user target]
  (swap! state game/update-unit id user #(assoc % :target target)))
  ;; !!!!!!!!! Units have to associated with a user, otherwise other players can just brute-force password by setting target.

(defn set-speed!
  [id user speed]
  ;; !!!!! Handle :max-speed
  (swap! state game/update-unit id user #(assoc % :speed speed)))

(defn hack!
  [id user password]
  ;; Set cooldown in relation to user
  ;; If password correct, update user
  )

(defn tick-move-units
  [g]
  (util/map-val #(units/move % tick-sec) g))

(defn tick
  [g]
  (-> g
     (update :ticks inc)
  ; Move units (First without restrictions)
     tick-move-units
  ; Gather actions
  ; Apply Actions (Gather first in case two units shoot each other)
  ; Remove dead
  ))

(defn tick!
  []
  (swap! state tick))

(defn new-game!
  []
  (reset! state {:ticks 0})
  (swap! state #(game/add-unit % (units/hq "mogens" {:x 100 :y 200})))
  (swap! state #(game/add-unit % (units/scout "mogens" {:x 100 :y 210}))))

(def running (atom false))

(defn run-game!
  []
  (when (not @running)
    (reset! running true)
    (future 
      (while @running
        (tick!)
        (Thread/sleep (* tick-sec 1000))))))


(defn stop-game!
  []
  (reset! running false))

(comment
  (new-game!)
  (println @state)
  (run-game!)
  (stop-game!)
  (tick!)
  (unit-ids "mogens")
  (set-target! (last (unit-ids "mogens")) "mogens" {:x 2222 :y 3333})
  (set-speed! (last (unit-ids "mogens")) "mogens" 10)
  (units/move (@state (last (unit-ids "mogens"))) tick-sec)
  (tick-move-units @state)
  (pstatus "mogens"))

