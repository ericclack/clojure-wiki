(ns couchdb-test.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[couchdb-test started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[couchdb-test has shut down successfully]=-"))
   :middleware identity})
