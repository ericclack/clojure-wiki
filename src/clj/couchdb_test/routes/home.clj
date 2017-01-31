(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            [couchdb-test.models.db :as db]
            [clojure.string :as s]))

(defn create-page-form [id]
   (layout/render
       "create.html" {:id id}))

(defn a-page [id]
  (let [page (db/wiki-page id) 
        page-exists (not (nil? page))]
    (if (true? page-exists) 
      (layout/render
       "page.html" {:doc (:content page)})
      (create-page-form id))))

(defn home-page []
  (a-page "home-page"))

(defn create-page [id content]
  (db/create-wiki-page id content)
  (redirect "/"))


(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/_create/:id" [id content] (create-page id content))
  (GET "/:id" [id] (a-page id))
)

