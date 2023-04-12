(ns hackerio.core
  (:require [ring.adapter.jetty :as jetty]
            ;[ring.util.request :refer [body-string]]
            ;[ring.middleware.params :refer [wrap-params]]
            ;[ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [hackerio.util :as util]
            [hackerio.world.app]
            [hackerio.apps.messages]
            [hackerio.apps.darkweb]
            [hackerio.apps.contacts]
            [hackerio.apps.tracker]
            [hackerio.apps.sat1]
            [hackerio.apps.m1]
            [clojure.string :as str]
            [clojure.java.io :as io]))


;; /app/function/param1/param2
;; /contacts/list
;; /contacts/entry/10
;; /resource/mission1
;; /resource/mission2

;; messages app
;; messages/send/mission1=budapest -> abc-123-def
;; messages/read/abc-123-def


;; App sat-tracker
;; Position depends on time mod x.
;; Send a time to darkweb with a time where the satelite is a certain pos.
;; Real positions of route in London

;; App laser locator
;; Request a key. Takes some minutes.
;; Key can be used for single point at given target.
;; Use together with sat-tracker to point at right time

(defn resource
  [path]
  (let [res (io/resource path)]
    (if res
      (slurp res)
      (str "Invalid resource: " path))))

(defn admin
  [args]
  (cond (= (first args) "log") (util/get-log 1000)
        true "Nothing"))
     
(defn handler
  [request]
  (when (not (re-find #"/admin|favicon" (request :uri))) (util/log (request :uri)))
  (let [components-unmodified (rest (str/split (request :uri) #"/"))
        components (map str/lower-case components-unmodified)
        port (request :server-port)
        app (first components)
        hashed (util/my-hash app)
        result (cond (or (= app "") (nil? app)) (resource "index.txt")
                     (= app "world") (apply hackerio.world.app/api (rest components))
                     (= app "messages") (apply hackerio.apps.messages/api (rest components))
                     (= app "contacts") (apply hackerio.apps.contacts/api (rest components))
                     (= app "darkweb") (apply hackerio.apps.darkweb/api (rest components-unmodified))
                     (= app "tracker") (apply hackerio.apps.tracker/api (rest components))
                     (= app "sat1") (apply hackerio.apps.sat1/api (rest components))
                     (= app "m1") (apply hackerio.apps.m1/api (rest components))
                     (= app "file1459382") (resource "file1459382.txt")
                     (= app "file3423121") (resource "file3423121.txt")
                     (= app "mission1") (resource "mission1.txt")
                     (= app "mission1solution") (resource "mission1solution.txt")
                     (= app "mission2") (resource "fakemission2.txt")
                     (= hashed (util/mission-hashes 0)) (resource "mission2.txt")
                     (= hashed (util/mission-hashes 1)) (resource "mission3.txt")
                     (= hashed (util/mission-hashes 2)) (resource "mission4.txt")
                     (= hashed (util/mission-hashes 3)) (resource "mission5.txt")
                     (= hashed (util/mission-hashes 4)) (resource "mission6.txt")
                     (= hashed (util/mission-hashes 5)) (resource "mission7.txt")
                     (= hashed util/admin-hash) (admin (rest components))
                     true "Nothing")]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (str/replace result #"<host>" (str "http://" (request :server-name) (when (not= port 80) (str ":" port))))}))

(defn -main
  [& args]
  (println "Starting HackerIO")
  (jetty/run-jetty handler {:port 80 :async? false}))

(comment (println (slurp "http://localhost/mission1")))
(comment (println (slurp "http://localhost/contacts")))
(comment
  (println (slurp "http://localhost/"))
  (println (slurp "http://localhost/contacts"))
  (println (slurp "http://localhost/contacts/list"))
  (println (slurp "http://localhost/contacts/3"))
  (spit "/tmp/id1.txt" (slurp "http://localhost/darkweb/crack/ZkJUMzI5NGZXclQ="))
  (println (slurp (slurp "/tmp/id1.txt")))
  (spit "/tmp/id2.txt" (slurp "http://localhost/darkweb/crack/ZkJUMzI5NGZXclQ="))
  (println (slurp (slurp "/tmp/id2.txt")))
  (println (slurp (str "http://localhost/darkweb")))
  (println (slurp (str "http://localhost/darkweb/debug")))
  (println (slurp "http://localhost/mission1"))
  (spit "/tmp/id3.txt" (slurp "http://localhost/messages/send/mission1=Copenhagen"))
  (println (slurp (slurp "/tmp/id3.txt")))
  (spit "/tmp/id4.txt" (slurp "http://localhost/messages/send/mission1=Amsterdam"))
  (println (slurp (slurp "/tmp/id4.txt")))
  (spit "/tmp/id5.txt" (slurp "http://localhost/messages/send/mission10000=Copenhagen"))
  (println (slurp (slurp "/tmp/id5.txt"))))
