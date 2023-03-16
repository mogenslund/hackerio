(ns hackerio.apps.darkweb
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hackerio.util :as util]))

(def progress (atom {"uid" "Not ready"}))

(def dictionary
  {"A" "E" "B" "i" "C" "o" "D" "H" "E" "z" "F" "V" "G" "Y" "H" "t" "I" "J" "J" "M" "K" "w"
   "L" "v" "M" "K" "N" "y" "O" "F" "P" "x" "Q" "P" "R" "d" "S" "S" "T" "h" "U" "s" "V" "U"
   "W" "X" "X" "O" "Y" "g" "Z" "C" "a" "T" "b" "r" "c" "W" "d" "f" "e" "a" "f" "G" "g" "Z"
   "h" "R" "i" "A" "j" "e" "k" "p" "l" "D" "m" "c" "n" "m" "o" "L" "p" "q" "q" "Q" "r" "l"
   "s" "B" "t" "u" "u" "k" "v" "n" "w" "I" "x" "N" "y" "j" "z" "b"})

(defn base64encode
  [s]
  (.encodeToString (java.util.Base64/getEncoder) (.getBytes s)))

(defn base64decode
  [s]
  (apply str (map char (.decode (java.util.Base64/getDecoder) s))))

(defn remove-eq
  [s]
  (-> s
      (str/replace #"(?<!=)$" "0")
      (str/replace #"==$" "2")
      (str/replace #"=$" "1")))

(defn append-eq
  [s]
  (-> s
      (str/replace #"0$" "")
      (str/replace #"1$" "=")
      (str/replace #"2$" "==")))

(defn encrypt
  [s]
  (->> s
       (map #(or (dictionary (str %1)) (str %1)))
       reverse
       (apply str)
       base64encode
       remove-eq))

(defn decrypt
  [s]
  (->> s
       append-eq
       base64decode
       reverse
       (map #(or ((clojure.set/map-invert dictionary) (str %1)) (str %1)))
       (apply str)))

(comment
  (decrypt "OTYzbUlMbFc1")
  (encrypt "abcd4923asd")
  (decrypt (encrypt "abcd4923asd")))

(defn index
  []
  (str "
                                  Dark Web

--------------------------------------------------------------------------------

Request a crack of message <m> by calling:

  * <host>/darkweb/crack/<m>

It will return an <id>.

It will take some time to crack the message. To check if the message has been
decrypted call:

  * <host>/darkweb/crack/<id>
  "))

(defn crack
  [s]
  (util/log (str "Cracking " s))
  (let [id (str (java.util.UUID/randomUUID))
        d (decrypt s)]
    (swap! progress assoc id "Not ready")
    (future (Thread/sleep (+ 5000 (rand-int 8000))) (swap! progress assoc id d))
    (str "<host>/darkweb/crack-result/" id)))

(defn crack-result
  [id]
  ;; When id done, remove from progress and return result
  (get @progress id))

(defn debug
  []
  (pr-str @progress))

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "crack") (crack (second args))
        (= (first args) "crack-result") (crack-result (second args))
        (= (util/my-hash (first args)) "2727676af") (debug)
        true "Invalid"))

(comment
  (def id (crack "ZkJUMzI5NGZXclQ1"))
  (decrypt "ZkJUMzI5NGZXclQ1")
  (println id)
  (crack-result (re-find #"[^/]+$" id)))

