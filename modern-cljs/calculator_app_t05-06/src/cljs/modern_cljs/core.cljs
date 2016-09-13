(ns modern-cljs.core
  (:require [modern-cljs.price-calculator :refer [calculate-total]]
            [domina.core :refer [by-id]]))

(enable-console-print!)

(defn init []
  (let [price-form (by-id "priceForm")]
    (set! (.-onsubmit price-form) calculate-total)))

(set! (.-onload js/window) init)
