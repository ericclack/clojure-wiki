(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            [couchdb-test.models.db :as db]
            [clojure.string :as s]))

(defn create-page-form [id]
   (layout/render
       "edit.html" {:id id}))

(defn a-page [id]
  (let [page (db/wiki-page id) 
        page-exists (not (nil? page))]
    (if (true? page-exists) 
      (layout/render
       "page.html" {:doc page })
      (create-page-form id))))

(defn home-page []
  (a-page "home-page"))

(defn create-page [id content]
  (db/create-wiki-page id content)
  (redirect (str "/" id)))

(defn edit-page [id]
  (let [page (db/wiki-page id)]
    (layout/render
       "edit.html" {:doc page})))  

(defn update-page [id rev content]
  (db/update-wiki-page id rev content)
  (redirect (str "/" id)))


(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/_create/:id" [id content] (create-page id content))
  (GET "/_edit/:id" [id] (edit-page id))
  (POST "/_edit/:id/:rev" [id rev content] (update-page id rev content))
  (GET "/:id" [id] (a-page id))
)

