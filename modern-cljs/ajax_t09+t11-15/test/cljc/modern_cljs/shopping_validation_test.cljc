(ns modern-cljs.shopping-validation-test
  (:require [modern-cljs.shopping-validation :refer [validate-shopping-form]]
            #?(:clj  [clojure.test :refer [deftest is are testing]]
               :cljs [cljs.test :refer-macros [deftest is are testing]])))


(deftest validate-shopping-form-test
  (testing "Shoppig Form Validation"

    (testing "/ valid data"
      (are [expected actual] (= expected actual)
           nil (validate-shopping-form "1" "0" "0" "0")
           nil (validate-shopping-form "1" "0.0" "0.0" "0.0")
           nil (validate-shopping-form "100" "10.25" "0.3" "0.1")))

    (testing "/ missing values"
      (are [expected actual] (= expected actual)
        "quantity cannot be empty"
        (-> (validate-shopping-form "" "0" "0" "0") (:quantity) (first))
        "price cannot be empty"
        (-> (validate-shopping-form "1" "" "0" "0") (:price) (first))
        "tax-percent cannot be empty"
        (-> (validate-shopping-form "1" "0" "" "0") (:tax-percent) (first))
        "discount cannot be empty"
        (-> (validate-shopping-form "1" "0" "0" "") (:discount) (first))))

    (testing "/ bad types"
      (are [expected actual] (= expected actual)
        "quantity should be an integer"
        (-> (validate-shopping-form "a" "0" "0" "0") (:quantity) (first))
        "quantity should be an integer"
        (-> (validate-shopping-form "1.5" "0" "0" "0") (:quantity) (first))
        "price should be a number"
        (-> (validate-shopping-form "1" "a" "0" "0") (:price) (first))
        "tax-percent should be a number"
        (-> (validate-shopping-form "1" "0" "a" "0") (:tax-percent) (first))
        "discount should be a number"
        (-> (validate-shopping-form "1" "0" "0" "a") (:discount) (first))))


    (testing "/ negative quantity"
      (is (= "quantity cannot be negative"
             (-> (validate-shopping-form "-1" "0" "0" "0") (:quantity) (first)))))

    ))
