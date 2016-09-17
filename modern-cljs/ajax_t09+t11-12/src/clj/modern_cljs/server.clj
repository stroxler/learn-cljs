(ns modern-cljs.server
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found files resources]]))

(defroutes handler
  (GET "/" [] "Hello from Compujure!")
  (files "/" {:root "build"})
  (resources "/" {:root "build"})
  (not-found "Page Not Found (compojure)"))
