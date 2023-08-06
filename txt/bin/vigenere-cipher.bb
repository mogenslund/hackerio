(ns user)

(defn vigenere-cipher [text keyword encrypt]
  (let [alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"]
    (apply str
           (map (fn [ch keyword-ch]
                  (let [ch-int (.indexOf alphabet (str ch))
                        shift (.indexOf alphabet (str keyword-ch))
                        operation (if encrypt + -)
                        shifted (mod (operation ch-int shift) (count alphabet))]
                    (if (and (not= ch-int -1) (not= shift -1))
                      (nth alphabet shifted)
                      ch)))
                text
                (cycle keyword)))))

(println "....")

(comment (vigenere-cipher (vigenere-cipher "ab-,.cd00AB" "AB" true) "AB" false))