(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [com.ashafa.clutch :as clutch]))

(defn wiki-pages []
  (clutch/with-db "wiki" 
    (clutch/get-view "pages" "by_title")))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)
                 :pages (wiki-pages)
                 }))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

