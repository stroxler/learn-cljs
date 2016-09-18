(set-env!
  :source-paths #{"src/cljs" "src/clj" "src/cljc"}
  :resource-paths #{"html"}
  :dependencies '[
                  ;; boot dependencies ;;
                  [org.clojure/clojure "1.7.0" :scope "test"]
                  [org.clojure/clojurescript "1.7.170" :scope "test"]
                  [adzerk/boot-cljs "1.7.170-3" :scope "test"]
                  [pandeiro/boot-http "0.7.0" :scope "test"]
                  [samestep/boot-refresh "0.1.0" :scope "test"] ; refresh clj
                  [adzerk/boot-test "1.0.7" :scope "test"]      ; test in clj
                  [adzerk/boot-reload "0.4.9" :scope "test"]    ; reload cljs
                  [adzerk/boot-cljs-repl "0.3.0" :scope "test"]
                  ;; (these 3 are dependencies that boot-cljs-repl
                  ;;    doesn't ransitively pull in for some reason)
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  ;; Client-side dependencies ;;
                  [org.clojars.magomimmo/domina "2.0.0-SNAPSHOT"]
                  [hiccups "0.3.0"]
                  [cljs-ajax "0.5.4"]                 ;; also works in jvm clj, despite the name

                  ;; Server-side dependencies ;;
                  [compojure "1.4.0"]
                  [ring/ring-json "0.4.0"]
                  [javax.servlet/servlet-api "2.5"]

                  ;; shared dependencies
                  [org.clojars.magomimmo/valip "0.4.0-SNAPSHOT"]
                  ])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(deftask with-test-code
  "Add test source code to the :source-paths"
  []
  (set-env! :source-paths #(conj % "test/cljc"))
  identity)

(deftask dev
  "Launch a dev environment: watch and compile, serve files, set up
  browser reloading, and connect an nrepl server to the browser"
  []
  (comp
   ;; serve up static files only with ring... (serve :dir "build")
   (serve :handler 'modern-cljs.server/app
          :resource-root "build"
          :reload true)
   (watch)
   (reload)
   (cljs-repl)
   (cljs)
   (target :dir #{"build"})))
