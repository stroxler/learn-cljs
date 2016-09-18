(ns modern-cljs.login
  (:require [domina.core :refer [by-id by-class value prepend! append! destroy!]]
            [domina.events :refer [listen! prevent-default]]
            [ajax.core :refer [POST]]
            [hiccups.runtime]
            [modern-cljs.login-validation :refer [email-valid? password-valid?]])
  (:require-macros [hiccups.core :refer [html]]))




(defn validate-form-field [field-name field-value validator]
  (let [general-class "error" ; this is here for css
        specific-class (str field-name "-error") ; this is here for by-class
        class-specifier (str general-class " " specific-class)
        err-msg (str "Invalid " field-name)]
    (destroy! (by-class specific-class))
    (if (validator field-value)
      true
      (do
        (prepend! (by-id "loginForm")
                  (html [:div {:class class-specifier} err-msg]))
        false))))

(defn validate-email [email]
  (validate-form-field "email" email email-valid?))

(defn validate-password [password]
  (validate-form-field "password" password password-valid?))


(defn validate-form [email password]
  (let [loginForm (by-id "loginForm")
        email (-> "email" (by-id) (value))
        password (-> "password" (by-id) (value))
        incomplete (or (empty? email)
                       (empty? password))]
    (if (and (not incomplete)
             (validate-email email)
             (validate-password password))
      true
      false)))


(defn do-login-form-error []
  (append! (by-id "loginForm")
           (html [:div.error.form "Please Complete the Form"])))


(defn do-login [email password]
  (let [data {:email email :password password}
        loginForm (by-id "loginForm")
        callback (fn [response]
                   (if (response "success")
                     (append! loginForm
                               (html [:div "Successfully logged in!"]))
                     (append! loginForm
                               (html [:div.error "Login unsuccessful"]))))]
    (POST "/login"
      {:response-format :json
       :format :json
       :params data
       :handler callback})))

(defn login-handler [e]
  (let [loginForm (by-id "loginForm")
        email (-> "email" (by-id) (value))
        password (-> "password" (by-id) (value))
        incomplete (or (empty? email)
                       (empty? password))]
    (prevent-default e)
    (destroy! (by-class "error"))
    (if (validate-form email password)
      (do-login email password)
      (do-login-form-error))))


(defn init []
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
             :click login-handler)))
