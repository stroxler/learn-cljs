(ns modern-cljs.core
  (:require [modern-cljs.login]
            [modern-cljs.shopping]))

(defn ^:export init_login []
  (modern-cljs.login/init))


(defn ^:export init_shopping []
  (modern-cljs.shopping/init))
