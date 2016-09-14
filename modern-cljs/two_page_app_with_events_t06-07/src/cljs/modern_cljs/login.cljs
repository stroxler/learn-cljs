(ns modern-cljs.login
  (:require [domina.core :refer [by-id value set-value!]]))


(defn validate-form []
  (let [email (-> "email" (by-id) (value))
        password (-> "password" (by-id) (value))
        valid (and (> (count email) 0)
                   (> (count password) 0))]
    (if valid
      true
      (do (js/alert "Please complete the form!")
          false))))


(defn ^:export init []
  (let [price-form (by-id "loginForm")]
    (set! (.-onsubmit price-form) validate-form)))
