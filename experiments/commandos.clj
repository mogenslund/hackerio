(ns user
  (:require [clojure.string :as str]))

;; clojure commandos.clj
;; quit
;; Iteration1:
;; - Exploratory. No constrains. Limitations in my head.
;;
;;
;;     ODD           EVEN
;;      __            __
;;   __/-0\__      __/-0\__ 
;;  /0-\__/0+\    /--\__/-+\
;;  \__/00\__/    \__/00\__/
;;  /+-\__/++\    /0-\__/0+\
;;  \__/+0\__/    \__/+0\__/
;;     \__/          \__/
;;

(def board-template "
    __________                  __________                  __________                  __________                  __________
   /   S10    \\                /   S12    \\                /   S14    \\                /   S16    \\                /   S18    \\
  /            \\              /            \\              /            \\              /            \\              /            \\
 / S10-Line1--- \\            / S12-Line1--- \\            / S14-Line1--- \\            / S16-Line1--- \\            / S18-Line1--- \\            
/ S10-Line2----- \\__________/ S12-Line2----- \\__________/ S14-Line2----- \\__________/ S16-Line2----- \\__________/ S18-Line2----- \\
\\ S10-Line3----- /   S11    \\ S12-Line3----- /   S13    \\ S14-Line3----- /   S15    \\ S16-Line3----- /   S17    \\ S18-Line3----- /
 \\ S10-Line4--- /            \\ S12-Line4--- /            \\ S14-Line4--- /            \\ S16-Line4--- /            \\ S18-Line4--- /
  \\ S10-Line5- / S11-Line1--- \\ S12-Line5- / S13-Line1--- \\ S14-Line5- / S15-Line1--- \\ S16-Line5- / S17-Line1--- \\ S18-Line5- /
   \\__________/ S11-Line2----- \\__________/ S13-Line2----- \\__________/ S15-Line2----- \\__________/ S17-Line2----- \\__________/
   /   S20    \\ S11-Line3----- /   S22    \\ S13-Line3----- /   S24    \\ S15-Line3----- /   S26    \\ S17-Line3----- /   S28    \\
  /            \\ S11-Line4--- /            \\ S13-Line4--- /            \\ S15-Line4--- /            \\ S17-Line4--- /            \\
 / S20-Line1--- \\ S11-Line5- / S22-Line1--- \\ S13-Line5- / S24-Line1--- \\ S15-Line5- / S26-Line1--- \\ S17-Line5- / S28-Line1--- \\            
/ S20-Line2----- \\__________/ S22-Line2----- \\__________/ S24-Line2----- \\__________/ S26-Line2----- \\__________/ S28-Line2----- \\
\\ S20-Line3----- /   S21    \\ S22-Line3----- /   S23    \\ S24-Line3----- /   S25    \\ S26-Line3----- /   S27    \\ S28-Line3----- /
 \\ S20-Line4--- /            \\ S22-Line4--- /            \\ S24-Line4--- /            \\ S26-Line4--- /            \\ S28-Line4--- /
  \\ S20-Line5- / S21-Line1--- \\ S22-Line5- / S23-Line1--- \\ S24-Line5- / S25-Line1--- \\ S26-Line5- / S27-Line1--- \\ S28-Line5- /
   \\__________/ S21-Line2----- \\__________/ S23-Line2----- \\__________/ S25-Line2----- \\__________/ S27-Line2----- \\__________/
   /   S30    \\ S21-Line3----- /   S32    \\ S23-Line3----- /   S34    \\ S25-Line3----- /   S36    \\ S27-Line3----- /   S38    \\
  /            \\ S21-Line4--- /            \\ S23-Line4--- /            \\ S25-Line4--- /            \\ S27-Line4--- /            \\
 / S30-Line1--- \\ S21-Line5- / S32-Line1--- \\ S23-Line5- / S34-Line1--- \\ S25-Line5- / S36-Line1--- \\ S27-Line5- / S38-Line1--- \\            
/ S30-Line2----- \\__________/ S32-Line2----- \\__________/ S34-Line2----- \\__________/ S36-Line2----- \\__________/ S38-Line2----- \\
\\ S30-Line3----- /   S31    \\ S32-Line3----- /   S33    \\ S34-Line3----- /   S35    \\ S36-Line3----- /   S37    \\ S38-Line3----- /
 \\ S30-Line4--- /            \\ S32-Line4--- /            \\ S34-Line4--- /            \\ S36-Line4--- /            \\ S38-Line4--- /
  \\ S30-Line5- / S31-Line1--- \\ S32-Line5- / S33-Line1--- \\ S34-Line5- / S35-Line1--- \\ S36-Line5- / S37-Line1--- \\ S38-Line5- /
   \\__________/ S31-Line2----- \\__________/ S33-Line2----- \\__________/ S35-Line2----- \\__________/ S37-Line2----- \\__________/
   /   S40    \\ S31-Line3----- /   S42    \\ S33-Line3----- /   S44    \\ S35-Line3----- /   S46    \\ S37-Line3----- /   S48    \\
  /            \\ S31-Line4--- /            \\ S33-Line4--- /            \\ S35-Line4--- /            \\ S37-Line4--- /            \\
 / S40-Line1--- \\ S31-Line5- / S42-Line1--- \\ S33-Line5- / S44-Line1--- \\ S35-Line5- / S46-Line1--- \\ S37-Line5- / S48-Line1--- \\            
/ S40-Line2----- \\__________/ S42-Line2----- \\__________/ S44-Line2----- \\__________/ S46-Line2----- \\__________/ S48-Line2----- \\
\\ S40-Line3----- /   S41    \\ S42-Line3----- /   S43    \\ S44-Line3----- /   S45    \\ S46-Line3----- /   S47    \\ S48-Line3----- /
 \\ S40-Line4--- /            \\ S42-Line4--- /            \\ S44-Line4--- /            \\ S46-Line4--- /            \\ S48-Line4--- /
  \\ S40-Line5- / S41-Line1--- \\ S42-Line5- / S43-Line1--- \\ S44-Line5- / S45-Line1--- \\ S46-Line5- / S47-Line1--- \\ S48-Line5- /
   \\__________/ S41-Line2----- \\__________/ S43-Line2----- \\__________/ S45-Line2----- \\__________/ S47-Line2----- \\__________/
   /   S50    \\ S41-Line3----- /   S52    \\ S43-Line3----- /   S54    \\ S45-Line3----- /   S56    \\ S47-Line3----- /   S58    \\
  /            \\ S41-Line4--- /            \\ S43-Line4--- /            \\ S45-Line4--- /            \\ S47-Line4--- /            \\
 / S50-Line1--- \\ S41-Line5- / S52-Line1--- \\ S43-Line5- / S54-Line1--- \\ S45-Line5- / S56-Line1--- \\ S47-Line5- / S58-Line1--- \\            
/ S50-Line2----- \\__________/ S52-Line2----- \\__________/ S54-Line2----- \\__________/ S56-Line2----- \\__________/ S58-Line2----- \\
\\ S50-Line3----- /   S51    \\ S52-Line3----- /   S53    \\ S54-Line3----- /   S55    \\ S56-Line3----- /   S57    \\ S58-Line3----- /
 \\ S50-Line4--- /            \\ S52-Line4--- /            \\ S54-Line4--- /            \\ S56-Line4--- /            \\ S58-Line4--- /
  \\ S50-Line5- / S51-Line1--- \\ S52-Line5- / S53-Line1--- \\ S54-Line5- / S55-Line1--- \\ S56-Line5- / S57-Line1--- \\ S58-Line5- /
   \\__________/ S51-Line2----- \\__________/ S53-Line2----- \\__________/ S55-Line2----- \\__________/ S57-Line2----- \\__________/
   /   S60    \\ S51-Line3----- /   S62    \\ S53-Line3----- /   S64    \\ S55-Line3----- /   S66    \\ S57-Line3----- /   S68    \\
  /            \\ S51-Line4--- /            \\ S53-Line4--- /            \\ S55-Line4--- /            \\ S57-Line4--- /            \\
 / S60-Line1--- \\ S51-Line5- / S62-Line1--- \\ S53-Line5- / S64-Line1--- \\ S55-Line5- / S66-Line1--- \\ S57-Line5- / S68-Line1--- \\            
/ S60-Line2----- \\__________/ S62-Line2----- \\__________/ S64-Line2----- \\__________/ S66-Line2----- \\__________/ S68-Line2----- \\
\\ S60-Line3----- /   S61    \\ S62-Line3----- /   S63    \\ S64-Line3----- /   S65    \\ S66-Line3----- /   S67    \\ S68-Line3----- /
 \\ S60-Line4--- /            \\ S62-Line4--- /            \\ S64-Line4--- /            \\ S66-Line4--- /            \\ S68-Line4--- /
  \\ S60-Line5- / S61-Line1--- \\ S62-Line5- / S63-Line1--- \\ S64-Line5- / S65-Line1--- \\ S66-Line5- / S67-Line1--- \\ S68-Line5- /
   \\__________/ S61-Line2----- \\__________/ S63-Line2----- \\__________/ S65-Line2----- \\__________/ S67-Line2----- \\__________/
   /   S70    \\ S61-Line3----- /   S72    \\ S63-Line3----- /   S74    \\ S65-Line3----- /   S76    \\ S67-Line3----- /   S78    \\
  /            \\ S61-Line4--- /            \\ S63-Line4--- /            \\ S65-Line4--- /            \\ S67-Line4--- /            \\
 / S70-Line1--- \\ S61-Line5- / S72-Line1--- \\ S63-Line5- / S74-Line1--- \\ S65-Line5- / S76-Line1--- \\ S67-Line5- / S78-Line1--- \\            
/ S70-Line2----- \\__________/ S72-Line2----- \\__________/ S74-Line2----- \\__________/ S76-Line2----- \\__________/ S78-Line2----- \\
\\ S70-Line3----- /          \\ S72-Line3----- /          \\ S74-Line3----- /          \\ S76-Line3----- /          \\ S78-Line3----- /
 \\ S70-Line4--- /            \\ S72-Line4--- /            \\ S74-Line4--- /            \\ S76-Line4--- /            \\ S78-Line4--- /
  \\ S70-Line5- /              \\ S72-Line5- /              \\ S74-Line5- /              \\ S76-Line5- /              \\ S78-Line5- /
   \\__________/                \\__________/                \\__________/                \\__________/                \\__________/
")

;action:
; - move {:location S70 :sec-left 34}
;(Only 1 action at a time. Penelty before moving)

(def tick0 (quot (System/currentTimeMillis) 1000))
(defn ticks [] (- (quot (System/currentTimeMillis) 1000) tick0))

(def game (atom {
  :log ["init"]
  :player1 {:sc1 {:type "scout"
                  :loc "S62"
                  :action {:type :move :loc "S53" :tick 45}
                  :health 100}}
  :enemy {:h1 {:type "harvester"
               :loc "S73"
               :action nil
               :health 100}
          :h2 {:type "harvester"
               :loc "S78"
               :action nil
               :health 100}
          :h3 {:type "harvester"
               :loc "S78"
               :action nil
               :health 20}}
  }))

(defn log-entry
  [s]
  (swap! game update :log conj s))

(defn show-log
  []
  (str/join "\n"
    (@game :log)))

(defn board
  []
  (-> board-template
      (str/replace #"S\d\d-Line1---" "            ")
      (str/replace #"S\d\d-Line2-----" "              ")
      (str/replace #"S\d\d-Line3-----" "              ")
      (str/replace #"S\d\d-Line4---" "            ")
      (str/replace #"S\d\d-Line5-" "          ")))

(defn unit-list
  []
  (let [g @game
        ids (-> g :player1 keys)]
    (str/join "\n"
      (map #(format "%s %s %s" % (-> g :player1 % :type) (-> g :player1 % :loc)) ids))))

(defn scan
  [loc]
  (swap! game assoc-in [:player1 :sc1 :action] {:type :scan :loc loc :tick (+ (ticks) 10)}))

(defn command
  [s]
  (let [args (str/split s #" ")
        cmd (first args)]
    (cond (= cmd "map") (println (board))
          (= cmd "units") (println (unit-list))
          (= cmd "log") (println (show-log))
          (= cmd "scan") (println (apply scan (rest args)))
          true (println "OUT:" s))))

(defn execute-actions
  [g t]
  ; For each action, if (>= t :tick) execute action and set nil. Otherwise do nothing.
  g)

(defn remove-dead
  [g t]
  ; For each health, if (<= :health 0) then dissoc. Otherwise do nothing.
  g)
  
(defn tick
  [g]
  (let [t (ticks)]
    (-> g
        (execute-actions t)
        remove-dead)))

(defn tick!
  []
  (swap! game tick))

(def running (atom false))
(defn game-loop!
  []
  (when (not @running)
    (reset! running true)
    (future 
      (while @running
        (tick!)
        (Thread/sleep (* 1 1000))))))


(defn -main
  [& args]
  (loop []
    (let [input (read-line)]
      (if (= input "quit")
        (do
          (println "Quitting")
          (reset! running false))
        (do
          (command input)
          (recur))))))

(-main)
