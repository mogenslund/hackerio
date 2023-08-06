(ns hackerio.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.codec :as codec]
            [hackerio.util :as util]
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
        speed (get params "speed")
        direction (get params "direction")
        result (cond (= app "test") (str "Admin: " admin "\nApp: " app)
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
