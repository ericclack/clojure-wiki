(ns clojure-wiki.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [clojure-wiki.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[clojure-wiki started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[clojure-wiki has shut down successfully]=-"))
   :middleware wrap-dev})
