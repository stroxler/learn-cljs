(ns modern-cljs.shopping
  (:require [domina.core :refer [by-id value by-class
                                 set-value! append! destroy!]]
            [domina.events :refer [listen!]]
            [hiccups.runtime])
  (:require-macros [hiccups.core :refer [html]]))


(defn calculate-total []
  (let [value-at-id #(-> % (by-id) (value))
        quantity (value-at-id "quantity")
        price (js/Number (value-at-id "price"))
        tax-percent (js/Number (value-at-id "taxrate"))
        discount (js/Number (value-at-id "discount"))
        tax-mult (+ 1.0 (/ tax-percent 100))
        total (-> (* quantity price)
                  (* tax-mult)
                  (- discount))]
    (set-value! (by-id "total") total)))


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
