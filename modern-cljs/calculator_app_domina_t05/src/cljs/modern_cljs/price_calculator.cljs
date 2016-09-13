(ns modern-cljs.price-calculator
  (:require [domina.core :refer [by-id value set-value!]]))


(defn calculate-total []
  "called when the form is submitted.
  validates the form data
  returns a Boolean indicated whether the form was valid
   (in an event handler, returning false prevents the usual
    action, so here we don't submit the POST request if
    validation fails)"
  (let [value-at-id #(-> % (by-id) (value))
        quantity (value-at-id "quantity")
        price (js/Number (value-at-id "price"))
        tax-percent (js/Number (value-at-id "taxrate"))
        discount (js/Number (value-at-id "discount"))
        tax-mult (+ 1.0 (/ tax-percent 100))
        total (-> (* quantity price)
                  (* tax-mult)
                  (- discount))]
    (set-value! (by-id "total") total)
    false))
