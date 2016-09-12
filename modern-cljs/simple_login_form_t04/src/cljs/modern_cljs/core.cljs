(ns modern-cljs.core
  (:require [modern-cljs.login :refer [validate-form]]))

(defn init []
  (let [login-form (.getElementById js/document "loginForm")]
    (set! (.-onsubmit login-form) validate-form)))

(set! (.-onload js/window) init)
