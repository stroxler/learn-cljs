(ns modern-cljs.login-validation)

;; note: the ^:dynamic tells clojure that this is a
;; dynamically-scoped binding, which means you can re-bind it
;; in a function and have everything further down the call stack
;; see the re-binding, without affecting code outside the call stack


;; match 4 to 8 characters, at least one decimal. Note that we use a
;; "look-ahead" regexp [the "(?=blah)" bit] to find the decimal - this is a
;; handy trick for advanced regexp validation
(def ^:dynamic *password-rx* #"^(?=[^\d]*\d).{4,8}")

;; match an email. Note that the domain has to be 2-4 lowercase letters
(def ^:dynamic *email-rx* #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z]{2,4})$")

(defn email-valid? [email]
  (re-matches *email-rx* email))


(defn password-valid? [password]
  (re-matches *password-rx* password))
