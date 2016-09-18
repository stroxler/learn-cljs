(ns modern-cljs.shopping-validation
  (:require [valip.core :refer [validate]]
            [valip.predicates :refer [present?
                                       integer-string?
                                       decimal-string?
                                       gt]]))


(defn validate-shopping-form [quantity price tax-percent discount]
  (validate {:quantity quantity :price price
             :tax-percent tax-percent :discount discount}
            [:quantity present? "quantity cannot be empty"]
            [:quantity integer-string? "quantity should be an integer"]
            [:quantity (gt 0) "quantity cannot be negative"]
            [:price present? "price cannot be empty"]
            [:price decimal-string? "price should be a number"]
            [:tax-percent present? "tax-percent cannot be empty"]
            [:tax-percent decimal-string? "tax-percent should be a number"]
            [:discount present? "discount cannot be empty"]
            [:discount decimal-string? "discount should be a number"]
            ))
