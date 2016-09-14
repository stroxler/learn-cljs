(ns modern-cljs.shopping
  (:require [domina.core :refer [by-id value set-value!]]
            [domina.events :refer [listen!]]))


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


(defn ^:export init []
  (let [price-form (by-id "priceForm")]
    (listen! (by-id "calc") :click calculate-total)))
