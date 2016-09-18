(ns modern-cljs.shopping
  (:require [domina.core :refer [by-id value by-class
                                 set-value! append! destroy!]]
            [domina.events :refer [listen!]]
            [hiccups.runtime]
            [ajax.core :refer [GET POST]])
  (:require-macros [hiccups.core :refer [html]]))


(defn calculate-total []
  (let [value-at-id #(-> % (by-id) (value))
        quantity (value-at-id "quantity")
        price (js/Number (value-at-id "price"))
        tax-percent (js/Number (value-at-id "taxrate"))
        discount (js/Number (value-at-id "discount"))]
    (POST "/shopping-price"
        {:response-format :json
         :format :json
         :params {:quantity quantity
                  :price price
                  :tax-percent tax-percent
                  :discount discount}
         :handler (fn [response]
                    (set-value! (by-id "total") (response "total")))})))


(defn init []
  (listen! (by-id "calc")
           :click
           calculate-total)
  (listen! (by-id "calc")
           :mouseover
           #(append! (by-id "shoppingForm")
                     (html [:div {:class "help"} "Click to calculate"])))
  (listen! (by-id "calc")
           :mouseout
           #(destroy! (by-class "help"))))
