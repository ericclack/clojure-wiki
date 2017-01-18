(ns couchdb-test.routes.home
  (:require [couchdb-test.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [com.ashafa.clutch :as clutch]))

(defn wiki-page [id]
  (clutch/with-db "wiki" (clutch/get-document id)))

(defn home-page []
  (a-page "home-page"))

(defn create-page-form [id]
   (layout/render
       "create.html" {:id id}))

(defn a-page [id]
  (let [page (clutch/with-db "wiki" (clutch/get-document id)) 
        page-exists (not (nil? page))]
    (if (true? page-exists) 
      (layout/render
       "page.html" {:doc (:content page)})
      (create-page-form id))))

(defn create-page [id]
  "Page TO DO"
  )

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/:id" [id] (a-page id))
  (POST "/_create/:id" [id] (create-page id))
)

