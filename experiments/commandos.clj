(ns commandos
  (:require [clojure.string :as str]
            [clojure.pprint :as pprint]
            [ring.adapter.jetty :as jetty]
            [ring.util.codec :as codec]))

;; clojure commandos.clj
;; clojure -M -m commandos
;; http://localhost/log
;; http://localhost/map
;; http://localhost/units
;; http://localhost/move?par1=s1&par2=s51
;; http://localhost/scan?par1=s1
;; (future (doseq [x (range 12)] (slurp "http://localhost/scan?par1=s1") (Thread/sleep 8000)))
;; http://localhost/debug
;; http://localhost/quit
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

(defn neighbours
  [loc]
  (let [[r c] (map #(Integer/parseInt %) (rest (re-find #"S(\d)(\d)" loc)))
        odd (== (mod c 2) 1)]
    (into [] (filter identity
      (if odd
        (vector
          (when (> r 1) (str "S" (- r 1) c))
          (str "S" r (+ c 1))
          (str "S" (+ r 1) (+ c 1))
          (when (< r 6) (str "S" (+ r 1) c))
          (str "S" (+ r 1) (- c 1))
          (str "S" r (- c 1)))
        (vector
          (when (> r 1) (str "S" (- r 1) c))
          (when (and (> r 1) (< c 8)) (str "S" (- r 1) (+ c 1)))
          (when (and (< r 7) (< c 8)) (str "S" r (+ c 1)))
          (when (< r 7) (str "S" (+ r 1) c))
          (when (and (< r 7) (> c 1)) (str "S" r (- c 1)))
          (when (and (> r 1) (> c 1)) (str "S" (- r 1) (- c 1)))))))))

(comment
  (neighbours "S10")
  (neighbours "S18")
  (neighbours "S78")
  (neighbours "S70")
  (neighbours "S62")
  (neighbours "S55")
  (neighbours "S63")
  (neighbours "S61")
  (neighbours "S11")
  (neighbours "S17")
)

(defn hex-distance
  [loc1 loc2]
  (let [[r1 c1] (map #(Integer/parseInt %) (rest (re-find #"S(\d)(\d)" loc1)))
        [r2 c2] (map #(Integer/parseInt %) (rest (re-find #"S(\d)(\d)" loc2)))
        offset-to-cube (fn [r c] (let [x c z (- r (quot (- c (mod c 2)) 2)) y (- (- x) z)] [x y z]))
        [x1 y1 z1] (offset-to-cube r1 c1)
        [x2 y2 z2] (offset-to-cube r2 c2)]
    (max (Math/abs (- x2 x1)) (Math/abs (- y2 y1)) (Math/abs (- z2 z1)))))

(comment
  (hex-distance "S63" "S64")
  (hex-distance "S62" "S66")
  (hex-distance "S62" "S62")
)

;action:
; - move {:location S70 :sec-left 34}
;(Only 1 action at a time. Penelty before moving)

(defn pretty-print-str
  [value]
  (let [writer (java.io.StringWriter.)]
    (pprint/write value :stream writer)
    (.toString writer)))

(def tick0 (quot (System/currentTimeMillis) 1000))
(defn ticks [] (- (quot (System/currentTimeMillis) 1000) tick0))

(def log (atom ["00000000 init"]))

(def units (atom [
  {:id "spy1" :type "spy" :loc "S62" :action {:type :move :loc "S53" :tick 45}}
  {:id "marine1" :type "marine" :loc "S65" :action nil}
  {:id "hacker1" :type "hacker" :loc "S65" :action nil}
  {:id "sniper1" :type "sniper" :loc "S65" :action nil}
  {:id "driver1" :type "driver" :loc "S23" :action nil}
  {:id "tc1" :type "coil" :active true :loc "S34" :hidden false :action nil}
  {:id "tc2" :type "coil" :active true :loc "S23" :hidden true :action nil}
  {:id "pc1" :type "server" :loc "S54" :connection "tc2" :hidden false}
  {:id "guard1" :type "guard" :loc "S65" :route ["S64" "S65" "S55"] :action nil}
  {:id "guard2" :type "guard" :loc "S66" :route ["S66"] :action nil}
  {:id "guard3" :type "guard" :loc "S45" :route ["S45" "S46"]:action nil}
  ]))

(defn update-unit!
  [id fun]
  (swap! units #(map (fn [unit] (if (= (unit :id) id) (fun unit) unit)) %)))

(defn unit-field
  [id field]
  (get (first (filter #(= (% :id) id) @units)) field))

(defn log-entry
  [s]
  (swap! log conj (format "%08d %s" (ticks) s)))

(defn show-log
  []
  (str/join "\n" @log))

(defn scan-request
  "Id is id of unit to do the scan in its own area"
  [id]
  (update-unit! id #(assoc % :action {:type :scan :tick (+ (ticks) 6)})))

(defn scan-action
  [id]
  (log-entry (format "DEBUG: scan-action %s" id))
  (let [loc (unit-field id :loc)
        owner (unit-field id :owner)
        sight (unit-field id :sight)]
    (doseq [u (filter #(and (= (% :loc) loc) (not= (% :owner) owner)) @units)]
      (when (< (rand-int 10000) (* sight (u :visibility)))
        (log-entry (format "SCAN %s: %s %s" loc (u :id) (u :type)))))))

(defn move-request
  "Id is id of unit to do the scan in its own area"
  [id loc]
  (update-unit! id #(assoc % :action {:type :move :loc (str/upper-case loc) :tick (+ (ticks) 6)})))

(defn move-action
  [id loc]
  (update-unit! id #(assoc % :loc loc))
  (log-entry (format "MOVE: %s %s" id loc)))

(defn wrap-tokens
  [tokens size]
  (loop [left size line [] lines []
         words tokens]
    (if-let [word (first words)]
      (let [wlen (count word)
            spacing (if (== left size) "" " ")
            alen (+ (count spacing) wlen)]
        (if (<= alen left)
          (recur (- left alen) (conj line spacing word) lines (next words))
          (recur (- size wlen) [word] (conj lines (apply str line)) (next words))))
      (when (seq line)
        (conj lines (apply str line))))))

(comment (wrap-tokens ["snipter1" "guard1" "tc1" "driver1" "baret3" "hacker1" "sniper2"] 12))

(comment
  (def cells (group-by :loc (filter #(not (:hidden %)) @units)))
  (cells "S62")
  (get ["1"] 1)
)

(defn board
  []
  (let [cells (group-by :loc (filter #(not (:hidden %)) @units))]
    (-> (reduce (fn [b k]
                  (let [lines (wrap-tokens (map :id (cells k)) 9)]
                    (-> b
                        (str/replace (str k "-Line1---") (format (str "%-12s") (or (get lines 0) "")))
                        (str/replace (str k "-Line2-----") (format (str "%-14s") (or (get lines 1) "")))
                        (str/replace (str k "-Line3-----") (format (str "%-14s") (or (get lines 2) "")))
                        (str/replace (str k "-Line4---") (format (str "%-12s") (or (get lines 3) "")))
                        (str/replace (str k "-Line5-") (format (str "%-10s") (or (get lines 4) ""))))))
                board-template (keys cells))
        (str/replace #"S\d\d-Line1---" "            ")
        (str/replace #"S\d\d-Line2-----" "              ")
        (str/replace #"S\d\d-Line3-----" "              ")
        (str/replace #"S\d\d-Line4---" "            ")
        (str/replace #"S\d\d-Line5-" "          "))))

(defn execute-actions
  [units]
  (doseq [u units]
    (cond (= (-> u :action :type) :scan) (scan-action (u :id))
          (= (-> u :action :type) :move) (move-action (u :id) (-> u :action :loc))
          true (println "Execution " u))))

(defn extract-due-units!
  []
  (let [t (ticks)
        result (atom nil)]  ; Holds the list of due actions
    (swap! units
           (fn [us]
             (let [due-actions (filter (fn [u] (and (:action u) (<= (:tick (:action u)) t))) us)  ; Filter units with due actions
                   updated-us (map (fn [u] (if (and (:action u) (<= (:tick (:action u)) t))
                                             (assoc u :action nil) 
                                             u))
                                   us)]  
               (reset! result due-actions)  ; Store due actions in the result atom
               updated-us)))  ; Return updated units to be stored in units atom
    @result))  ; Return the due actions

(defn unit-list
  [player]
  (str/join "\n"
    (map #(format "%s %s %s" (% :id) (% :type) (% :loc))
         (filter #(= (:owner %) player) @units))))

(defn command
  [s]
  (when (not= s "log") (log-entry (str "COMMAND: " s)))
  (try
    (let [args (str/split s #" ")
          cmd (first args)]
      (cond (= cmd "map") (println (board))
            (= cmd "units") (println (unit-list "p1"))
            (= cmd "log") (println (show-log))
            (= cmd "scan") (apply scan-request (rest args))
            (= cmd "move") (apply move-request (rest args))
            (= cmd "debug") (do (println (ticks)) (println @units))
            true (println "OUT:" s)))
    (catch Exception e (println e))))

(defn remove-dead!
  []
  ; For each health, if (<= :health 0) then dissoc. Otherwise do nothing.
  )

(defn tc-kill
  []
  ;; Unique list of active tc fields and neighbours
  ;; Traverse units. If any on list, print GAME OVER
  )
  
(defn tick!
  []
  (execute-actions (extract-due-units!)) ;; kill-guard
  ;; tc-kill
  ;; move-guards
  ;; guards-kill
  )

;; PC ACTIONS
;; ==========
;; - cmd pc23 ls (list files: guard movements, tc interfaces, encryption keys)
;; - cmd pc23 cat file (print file) Might be guard movements

(def running (atom false))
(defn game-loop!
  []
  (when (not @running)
    (reset! running true)
    (future 
      (while @running
        (tick!)
        (Thread/sleep (* 1 1000))))))

(defn handler
  [request]
  (let [components-unmodified (rest (str/split (request :uri) #"/"))
        components (map str/lower-case components-unmodified)
        port (request :server-port)
        app (first components)
        params (codec/form-decode (str (request :query-string)))
        par1 (get params "par1")
        par2 (get params "par2")
        result (cond (= app "log") (show-log)
                     (= app "map") (board)
                     (= app "units") (unit-list "p1")
                     (= app "scan") (scan-request par1)
                     (= app "move") (move-request par1 par2)
                     (= app "debug") (str (ticks) "\n" (pretty-print-str @units))
                     true (str "\nNothing\n" request))]
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (str/replace result #"<host>" (str "http://" (request :server-name) (when (not= port 80) (str ":" port))))}))



(defn -main
  [& args]
  (game-loop!)
  (loop []
    (let [input (read-line)]
      (if (= input "quit")
        (do
          (println "Quitting")
          (reset! running false)
          (shutdown-agents))
        (do
          (command input)
          (recur))))))

(defonce server (jetty/run-jetty handler {:port 80 :join? false}))
;(-main)
