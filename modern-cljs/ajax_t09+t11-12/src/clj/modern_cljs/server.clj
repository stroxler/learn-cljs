(ns modern-cljs.server
  (:require [compojure.core :refer [defroutes GET POST routes]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))

(defn calculate-total [quantity price tax-percent discount]
  (let [tax-mult (+ 1.0 (/ tax-percent 100))]
     (-> (* quantity price)
         (* tax-mult)
         (- discount))))

(defroutes site-routes
  (GET "/" [] "Hello from Compujure!")
  (route/files "/" {:root "build"})
  (route/resources "/" {:root "build"})
  (route/not-found "Page Not Found (compojure)"))


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


(defn shopping-price-handler [request]
  (println (str "request: " request))
  (let [quantity (-> (get-from-req :quantity request) -> (BigDecimal.))
        price (-> (get-from-req :price request) -> (BigDecimal.))
        tax-percent (-> (get-from-req :tax-percent request 0) -> (BigDecimal.))
        discount (-> (get-from-req :discount request 0) -> (BigDecimal.))]
    {:status 200
     :body {:total (calculate-total quantity price tax-percent discount)}}))


(defroutes api-routes
  (POST "/shopping-price" request (shopping-price-handler request)))


(def all-routes (routes
                 (handler/api api-routes)
                 (handler/site site-routes)))

(def app
  (-> all-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
