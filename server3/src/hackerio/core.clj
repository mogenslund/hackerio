(ns hackerio.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.codec :as codec]
            [hackerio.util :as util]
            [hackerio.messages]
            [hackerio.contacts]
            [hackerio.tracker]
            [hackerio.sat1]
            [hackerio.drone]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn is-admin
  [admin]
  (and admin (= admin "mogens8391")))
     
(defn handler
  [request]
  ;(when (not (re-find #"/admin|favicon" (request :uri))) (util/log (request :uri)))
  (let [components-unmodified (rest (str/split (request :uri) #"/"))
        components (map str/lower-case components-unmodified)
        port (request :server-port)
        app (first components)
        params (codec/form-decode (str (request :query-string)))
        admin (is-admin (get params "admin"))
        id (get params "id")
        to (get params "to")
        msg (get params "msg")
        type (get params "type")
        speed (get params "speed")
        direction (get params "direction")
        result (cond (= app "test") (str "Admin: " admin "\nApp: " app)
                     (or (= app "") (nil? app)) (slurp (util/resource "index.txt"))
                     (and (= app "log") admin) (util/get-log 200)
                     (= app "mission1") (slurp (util/resource "mission1.txt"))
                     (= app "mission1solution") (slurp (util/resource "mission1solution.txt"))
                     (= app "mission2") (slurp (util/resource "mission2.txt"))
                     (= app "mission3") (slurp (util/resource "mission3.txt"))
                     (= app "mission4") (slurp (util/resource "mission4.txt"))
                     (= app "mission5") (slurp (util/resource "mission5.txt"))
                     (= app "mission6") (slurp (util/resource "mission6.txt"))
                     (= app "mission7") (slurp (util/resource "mission7.txt"))
                     (= app "file1459382") (slurp (util/resource "file1459382.txt"))
                     (= app "file3423121") (slurp (util/resource "file3423121.txt"))
                     (and (= app "msg") to msg) (hackerio.messages/send-message :to to :msg msg)
                     (and (= app "msg") id) (hackerio.messages/get-response :id id)
                     (= app "msg") (slurp (util/resource "msg.txt"))
                     (and (= app "contacts") id) (hackerio.contacts/contact :id id)
                     (= app "contacts") (slurp (util/resource "contacts.txt"))
                     (= app "contacts-list") (hackerio.contacts/contact-list)
                     (and (= app "tracker") (= id "guard")) (hackerio.tracker/guard)
                     (= app "tracker") (slurp (util/resource "tracker.txt"))
                     (and (= app "sat1") (= id "data")) (hackerio.tracker/sat1)
                     (= app "sat1") (slurp (util/resource "sat1.txt"))
                     (and (= app "drone") (= type "moscow")) (hackerio.drone/drone :type "moscow")
                     (and (= app "drone") id) (hackerio.drone/drone :id id :speed speed :direction direction)
                     (= app "drone") (slurp (util/resource "drone.txt"))
                     true (str "\nNothing\n" id "\n" to "\n" msg "\n" request))]
      (when (and (not admin) (re-find #"mission|msg" (str app)))
        (util/log (str (request :uri) (when (request :query-string) (str "?" (request :query-string))))))
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (str/replace result #"<host>" (str "http://" (request :server-name) (when (not= port 80) (str ":" port))))}))

; (defn handler [request] {:status 200 :headers {"Content-Type" "text/plain"} :body (str request)})

(defn -main
  [& args])

(defonce server (jetty/run-jetty handler {:port 80 :join? false}))
; (do (.stop server) (def server (jetty/run-jetty handler {:port 80 :join? false})) (.start server))

  ; http://localhost/
  ; http://localhost/test
  ; http://localhost/test?admin=mogens8391
  ; http://localhost/log
  ; http://localhost/log?admin=mogens8391
  ; http://localhost/mission1
  ; http://localhost/mission1solution
  ; http://localhost/mission2
  ; http://localhost/mission3
  ; http://localhost/mission4
  ; http://localhost/mission5
  ; http://localhost/mission6
  ; http://localhost/mission7
  ; http://localhost/contacts?id=2046
  ; http://localhost/contacts-list
  ; (def ans (slurp "http://localhost/msg?to=test&msg=Msg1"))
  ; (println ans)
  ; (slurp (str "http://localhost/msg?id=" ans))
