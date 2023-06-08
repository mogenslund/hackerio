(ns hackerio.cipher
  (:require [clojure.string :as str]))

(def alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
(def rotation [4 8 102 34 18 83 199 34 10 24 52 32 12 98 23 45 12 34 54 99 87 28 29 93 28 182 32 19 1 23 45])

(defn rotate
  [c n]
  (if (<= 0 (.indexOf alphabet (str c)))
    (str (nth alphabet (mod (+ (.indexOf alphabet (str c)) n) (count alphabet))))
    (str c)))

; (rotate "B" 2)

(defn encrypt
  [text]
  (apply str (map rotate (seq text) rotation)))

; (encrypt "Abc")

(defn decrypt
  [text]
  (apply str (map #(rotate %1 (- (count alphabet) %2)) (seq text) rotation)))

; (decrypt "EjG")
; (decrypt "oqRBzznP32TcGb")
