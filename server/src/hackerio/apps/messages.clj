(ns hackerio.apps.messages
  (:require [clojure.string :as str]
            [hackerio.util :as util]))

(def messages (atom {"uid" "Not ready"}))

(defn check-solution
  [solution]
  (let [mission (re-find #"mission\d+" solution)
        missionnr (when mission (Integer/parseInt (re-find #"\d+" mission)))
        nextmission (util/next-mission solution)]
    (if nextmission 
      (str "SUCCESS\n"
           "You have completed Mission " missionnr "\n\n"
           ""
           "Next mission:\n\n"
           " * " nextmission "\n")
      (str "FAIL:\n"
           "Wrong solution for Mission " missionnr))))


(defn handle-message
  [msg]
  (let [id (str (java.util.UUID/randomUUID))]
    (cond (re-matches #"mission\d+=.+" (str/lower-case msg)) (check-solution (str/lower-case msg))
          true "Invalid")))

(defn send-message
  [msg]
  (let [id (str (java.util.UUID/randomUUID))]
    (swap! messages assoc id "Message in progress")
    (future (Thread/sleep (+ 5000 (rand-int 6000)))
            (swap! messages assoc id (handle-message msg)))
    (str "<host>/messages/read/" id)))

(defn read-message
  [id]
  (get @messages id))

(defn index
  []
  (str "
                                  Messages

--------------------------------------------------------------------------------

Send message <msg> by calling:

  * <host>/messages/send/<msg>

This call will return a url that will provide access to the reply.

It will take some time to process the message. To check if the message has been
processed call the reply-url:

  * <host>/messages/read/<id>

  "))

(defn debug
  []
  (pr-str @messages))

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "send") (send-message (second args))
        (= (first args) "read") (read-message (second args))
        (= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))

