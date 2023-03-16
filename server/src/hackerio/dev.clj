(ns hackerio.dev
  (:require [ring.adapter.jetty :as jetty]
            ;[ring.util.request :refer [body-string]]
            ;[ring.middleware.params :refer [wrap-params]]
            ;[ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [hackerio.util :as util]
            [hackerio.apps.messages]
            [hackerio.apps.darkweb]
            [hackerio.apps.contacts]
            [hackerio.apps.tracker]
            [hackerio.apps.m1]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def world (atom {
  :tick 0
  :money 100
  :army-attack 10
  :army-defence 23
  :navy-attack 35 
  :navy-defence 20
  :air-attack 10
  :air-defence 5}))

(defn tick
  [w]
  (update w :tick inc))

(defn game-loop
  []
  (loop []
    (Thread/sleep 2000) ; TODO: Use diffence between elapsed time and current
    (swap! world tick)
    (recur)))

(comment (swap! world tick))


(defn -main
  [& args]
  ;; 1. start tics in thread
  (future (game-loop))
  ;; 2. Start input loop
  (loop []
    (let [input (read-line)]
      (cond (= input "info") (println @world)
            true (println "Unknown command"))
      (recur))))
