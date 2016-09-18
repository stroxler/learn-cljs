(ns modern-cljs.shopping
  (:require [domina.core :refer [by-id value by-class
                                 set-value! append! destroy!]]
            [domina.events :refer [listen!]]
            [hiccups.runtime]
            [ajax.core :refer [GET POST]]
            [modern-cljs.shopping-validation :refer [validate-shopping-form]])
  (:require-macros [hiccups.core :refer [html]]))


(defn calculate-on-server [quantity price tax-percent discount]
  (POST "/shopping-price"
      {:response-format :json
       :format :json
       :params {:quantity (js/Number quantity)
                :price (js/Number price)
                :tax-percent (js/Number tax-percent)
                :discount (js/Number discount)}
       :handler (fn [response]
                  (set-value! (by-id "total") (response "total")))}))


(defn calculate-total []
  (destroy! (by-class "error"))
  (let [value-at-id #(-> % (by-id) (value))
        quantity (value-at-id "quantity")
        price (value-at-id "price")
        tax-percent (value-at-id "tax-percent")
        discount (value-at-id "discount")]
    (if-let [errors (validate-shopping-form quantity price tax-percent discount)]
      (doseq [[key message] errors]
        (js/console.log message)
        (append! (by-id (str (name key) "-div"))
                 (html [:div.error (apply str message)])))
      (calculate-on-server quantity price tax-percent discount))))


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
