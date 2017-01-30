(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
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


(defn create-page [id request]
  (s/join (list "To Do for '" id "' page"))
)

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/_create/:id" [id request] (create-page id request))
  (GET "/:id" [id] (a-page id))
)

