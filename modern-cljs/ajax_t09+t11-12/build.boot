(set-env!
  :source-paths #{"src/cljs" "src/clj" "src/cljc"}
  :resource-paths #{"html"}
  :dependencies '[
                  ;; boot dependencies ;;
                  [org.clojure/clojure "1.7.0"]
                  [org.clojure/clojurescript "1.7.170"]
                  [adzerk/boot-cljs "1.7.170-3"]
                  [pandeiro/boot-http "0.7.0"]
                  [adzerk/boot-reload "0.4.9"]
                  [adzerk/boot-cljs-repl "0.3.0"]
                  [com.cemerick/piggieback "0.2.1"]   ;; cljs-repl 0.3.0 doesn't
                  [weasel "0.7.0"]                    ;; include transitive dependencies,
                  [org.clojure/tools.nrepl "0.2.12"]  ;; which is why these are here
                  ;; Client-side dependencies ;;
                  [org.clojars.magomimmo/domina "2.0.0-SNAPSHOT"]
                  [hiccups "0.3.0"]
                  [cljs-ajax "0.5.4"]                 ;; also works in jvm clj, despite the name
                  ;; Server-side dependencies ;;
                  [compojure "1.4.0"]
                  [ring/ring-json "0.4.0"]
                  [javax.servlet/servlet-api "2.5"]
                  ])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])


(deftask dev []
  "Launch a dev environment: watch and compile, serve files, set up
  browser reloading, and connect an nrepl server to the browser"
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
