(ns hackerio.apps.m2
  (:require [clojure.string :as str]))

;    ╭──────────╮
; 10 │.....x....│ 
;  9 │..........│ 
;  8 │..........│ 
;  7 │..........│ 
;  6 │>--------<│ laser
;  5 │..........│
;  4 │..x.......│ guard1
;  3 │..........│
;  2 │.x.x......│ engineer1 scout1 
;  1 │..........│
;    ╰──────────╯
;     1234567890
;
; !!!!!!!!!! It should be much more server heavy.
; Spending time analysing a server an deducting the right
; actions and commands.


(defn index
  []
  (str "
                     Mission M2
                 Coordinated actions 

------------------------------------------------------------

Greetings Agent

You are hereby activated for Mission M2: Coordinated
actions.

References:
  * <host>/m1/info
  * <host>/m1/scout1/n
  * <host>/m1/scout1/s
  * <host>/m1/scout1/e
  * <host>/m1/scout1/w
  * <host>/m1/engineer1/n
  * <host>/m1/engineer1/s
  * <host>/m1/engineer1/e
  * <host>/m1/engineer1/w

  "))

