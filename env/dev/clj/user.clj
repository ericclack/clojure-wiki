(ns user
  (:require [mount.core :as mount]
            clojure-wiki.core))

(defn start []
  (mount/start-without #'clojure-wiki.core/http-server
                       #'clojure-wiki.core/repl-server))

(defn stop []
  (mount/stop-except #'clojure-wiki.core/http-server
                     #'clojure-wiki.core/repl-server))

(defn restart []
  (stop)
  (start))


