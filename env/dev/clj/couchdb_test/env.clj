(ns couchdb-test.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [couchdb-test.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[couchdb-test started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[couchdb-test has shut down successfully]=-"))
   :middleware wrap-dev})
