(ns hackerio.messages
  (:require [clojure.string :as str]
            [hackerio.cipher :as cipher]))

(def post-office (atom {}))

(defn generate-response
  [to msg]
  (let [lmsg (str/lower-case msg)]
    (cond (= to "test") (format "My response is no to your message:\n%s" msg)
          (and (= to "hq") (= lmsg "1966-12-11")) "SUCCESS\nYou have completed Mission 1"
          (and (= to "hq") (= lmsg "copenhagen")) "SUCCESS\nYou have completed Mission 2"
          (and (= to "hq") (= lmsg "1230")) "SUCCESS\nYou have completed Mission 3"
          (and (= to "hq") (= lmsg "95.85127,85.22671")) "SUCCESS\nYou have completed Mission 4"
          (and (= to "hq") (= lmsg "morales392")) "SUCCESS\nYou have completed Mission 5"
          (and (= to "hq") (= lmsg "minsk")) "SUCCESS\nYou have completed Mission 6"
          (= to "hq") "Not the answer we are looking for."
          (= to "bshadow") (cipher/decrypt msg)
          true "Empty response")))

; (println (generate-response "test" "Some message"))

(defn send-message
  [& {:keys [to msg]}]
  (let [id (.toString (java.util.UUID/randomUUID))]
    (swap! post-office assoc id
           {:to to :msg msg :time (+ (quot (System/currentTimeMillis) 1000) 5 (rand-int 10))})
    id))

(defn get-response
  [& {:keys [id]}]
  (let [response (@post-office id)]
    (cond (not response) "Message does not exist"
          (> (response :time) (quot (System/currentTimeMillis) 1000)) "Response not ready"
          true (do
                 (swap! post-office dissoc id)
                 (generate-response (response :to) (response :msg))))))

; (def t1 (send-message :to "test" :msg "Msg1"))
; (get-response :id t1)
