(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [com.ashafa.clutch :as clutch]))

(defn wiki-page [id]
  (:content (clutch/with-db "wiki" 
    (clutch/get-document id))))

(defn home-page []
  (layout/render
    "page.html" {:doc (wiki-page "home-page")} ))

(defn a-page [id]
  (layout/render
   "page.html" {:doc (wiki-page id)}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/page/:id" [id] (a-page id))
)

