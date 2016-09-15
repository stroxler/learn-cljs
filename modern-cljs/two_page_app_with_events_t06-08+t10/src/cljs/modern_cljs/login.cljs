(ns modern-cljs.login

  (:require [domina.core :refer [by-id by-class value prepend! append! destroy!]]
            [domina.events :refer [listen! prevent-default]]
  [hiccups.runtime])
  (:require-macros [hiccups.core :refer [html]]))


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


(defn validate-email [email]
  (destroy! (by-class "email-error"))
  (if (re-matches *email-rx* email)
    true
    (do
      (prepend! (by-id "loginForm")
                (html [:div.error.email-error "Invalid email"]))
      false)))


(defn validate-password [password]
  (destroy! (by-class "password-error"))
  (if (re-matches *password-rx* password)
    true
    (do
      (prepend! (by-id "loginForm")
                (html [:div.error.password-error "Invalid password"]))
      false)))


(defn validate-form [e]
  (let [fail (fn [] (prevent-default e))
        loginForm (by-id "loginForm")
        email (-> "email" (by-id) (value))
        password (-> "password" (by-id) (value))
        incomplete (or (empty? email)
                       (empty? password))]
  (destroy! (by-class "error"))
  (if incomplete
    (do
      (append! loginForm (html [:div.error.form "Please Complete the Form"]))
      (fail))
    (if (and (validate-email email)
             (validate-password password))
      true
      (fail)))))


(defn ^:export init []
  (let [price-form (by-id "loginForm")
        validate-on-value (fn [handler id]
                            (fn [e]
                              (let [value-at-id (-> id (by-id) (value))]
                                (handler value-at-id))))]
    (listen! (by-id "email")
             :blur
             (validate-on-value validate-email "email"))
    (listen! (by-id "password")
             :blur
             (validate-on-value validate-password "password"))
    (listen! (by-id "submit")
             :click validate-form)))
