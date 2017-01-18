(ns user
  (:require [mount.core :as mount]
            couchdb-test.core))

(defn start []
  (mount/start-without #'couchdb-test.core/http-server
                       #'couchdb-test.core/repl-server))

(defn stop []
  (mount/stop-except #'couchdb-test.core/http-server
                     #'couchdb-test.core/repl-server))

(defn restart []
  (stop)
  (start))


