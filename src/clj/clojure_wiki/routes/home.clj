(ns clojure-wiki.routes.home
  (:require [clojure-wiki.layout :as layout]
            [clojure.tools.logging :as log]            
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            [clojure-wiki.models.db :as db]
            [clojure.string :as s]))

;; ----------------------------------------------------------

(defn split-tags [tag-string]
  (if (= tag-string "")
    '()
    (s/split tag-string #"[\s,]+")))

;; ----------------------------------------------------------

(defn create-page-form [id]
   (layout/render
       "edit.html" {:id id}))

(defn a-page [id]
  ;; Render a page, or show the create page form
  (let [page (db/wiki-page id) 
        page-exists (not (nil? page))
        nav-bar (db/get-nav-bar)]
    (if (true? page-exists) 
      (layout/render
       "page.html" {:doc page :nav nav-bar})
      (do
        (log/info "page" id "doesn't exist, creating it")
        (create-page-form id)))))

(defn home-page []
  (a-page "home-page"))


(defn create-page [id content tags]
  (db/create-wiki-page! id content (split-tags tags))
  (redirect (str "/" id)))

(defn edit-page [id]
  (let [page (db/wiki-page id)]
    (layout/render
       "edit.html" {:doc page})))  

(defn update-page [id rev content tags]
  (db/update-wiki-page! id rev content (split-tags tags))
  (redirect (str "/" id)))

;; ------------------------------------------------

(defn add-nav [id]
  (db/set-nav-bar! (set (conj (db/get-nav-bar) id)))
  (redirect (str "/" id)))

;; ------------------------------------------------

(defn tag-search [tag]
  (layout/render
   "search.html" {:tag tag :results (db/pages-with-tag tag)}))

(defn word-search [word]
  (layout/render
   "search.html" {:word word :results (db/pages-with-word word)}))

;; ------------------------------------------------

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/_create/:id" [id content tags] (create-page id content tags))
  (GET "/_edit/:id" [id] (edit-page id))
  (POST "/_edit/:id/:rev" [id rev content tags] (update-page id rev content tags))
  (POST "/_addnav/:id" [id] (add-nav id))
  (GET "/_tagsearch/:tag" [tag] (tag-search tag))
  (GET "/_search" [word] (word-search word))  
  (GET "/:id" [id] (a-page id))
)

