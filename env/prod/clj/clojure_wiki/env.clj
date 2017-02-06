(ns clojure-wiki.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[clojure-wiki started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[clojure-wiki has shut down successfully]=-"))
   :middleware identity})
