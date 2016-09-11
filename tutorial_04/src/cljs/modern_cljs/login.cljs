(ns modern-cljs.login)

;; called when the form is submitted.
;; validates the form data
;; returns a Boolean indicated whether the form was valid
;;   (in an event handler, returning false prevents the usual
;;    action, so here we don't submit the POST request if
;;    validation fails)
(defn validate-form []
  (let [email (.getElementById js/document "email")
        password (.getElementById js/document "password")
        valid (and (> (count (.-value email)) 0)
                   (> (count (.-value password)) 0))]
    (if valid
      true
      (do (js/alert "Please complete the form!")
          false))))
