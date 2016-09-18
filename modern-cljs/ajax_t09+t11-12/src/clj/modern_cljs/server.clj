(ns modern-cljs.server
  (:require [compojure.core :refer [defroutes GET POST routes]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))

;; general ring json utils

(defn -parse-req [key request default]
  (or (get-in request [:params key])
      (get-in request [:body key])
      default))

(defn get-from-req
  ([key request]
   (let [value (-parse-req key request :missing_value___)]
     (if (= value :missing_value___)
       (throw (Exception. (str "missing parameter " key)))
       value)))
  ([key request default] (-parse-req key request default)))

;; shopping form logic and request handler

(defn calculate-total [quantity price tax-percent discount]
  (let [tax-mult (+ 1.0 (/ tax-percent 100))]
     (-> (* quantity price)
         (* tax-mult)
         (- discount))))

(defn shopping-price-handler [request]
  (println (str "request: " request))
  (let [quantity (-> (get-from-req :quantity request) -> (BigDecimal.))
        price (-> (get-from-req :price request) -> (BigDecimal.))
        tax-percent (-> (get-from-req :tax-percent request 0) -> (BigDecimal.))
        discount (-> (get-from-req :discount request 0) -> (BigDecimal.))]
    {:status 200
     :body {:total (calculate-total quantity price tax-percent discount)}}))

;; login form logic and request handler (TODO remove duplicated code)


(def ^:dynamic *password-rx* #"^(?=[^\d]*\d).{4,8}")

;; match an email. Note that the domain has to be 2-4 lowercase letters
(def ^:dynamic *email-rx* #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z]{2,4})$")


(defn email-valid? [email]
  (re-matches *email-rx* email))

(defn password-valid? [password]
  (re-matches *password-rx* password))

(defn login-handler [request]
  (println (str "request: " request))
  (let [email (get-from-req :email request)
        password (get-from-req :password request)]
    (if (and (email-valid? email)
             (password-valid? password))
      {:status 200
       :body {:success true}}
      {:status 400
       :body {:success false}})))

;; api routes

(defroutes api-routes
  (GET "/" [] "Hello from Compujure!")
  (POST "/shopping-price" request (shopping-price-handler request))
  (POST "/login" request (login-handler request)))

;; other routes

(defroutes site-routes
  (route/files "/" {:root "build"})
  (route/resources "/" {:root "build"})
  (route/not-found "Page Not Found (compojure)"))

;; put it all together

(def all-routes (routes
                 (handler/api api-routes)
                 (handler/site site-routes)))

(def app
  (-> all-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
