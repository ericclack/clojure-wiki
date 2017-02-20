(ns clojure-wiki.models.db
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]))

(defmacro with-db
  [& body]
  `(couch/with-db (env :database-url)
    ~@body))
  
;; --------------------------------------------------

(defn wiki-page [id]
  (with-db (couch/get-document id)))

(defn create-wiki-page!
  ([id content] (create-wiki-page! id content nil))
  ([id content tags]
   (with-db (couch/put-document {:_id id :content content :tags tags}))))

(defn remove-wiki-page! [id rev]
  (with-db (couch/delete-document {:_id id :_rev rev})))

(defn update-wiki-page!
  ([id rev content] (update-wiki-page! id rev content nil))
  ([id rev content tags]
   (with-db (couch/put-document
             {:_id id :_rev rev :content content :tags tags}))))  

;; --------------------------------------------------

(def nav-bar-id "-nav-bar")

(defn get-nav-bar []
  (with-db
    (list* (:content (couch/get-document nav-bar-id)))))

(defn set-nav-bar! [items]
  (with-db
    (def current-bar (couch/get-document nav-bar-id))
    (if (some? current-bar)
      (:content (couch/put-document (merge current-bar {:content items})))
      (:content (couch/put-document {:_id nav-bar-id :content items})))))

;; --------------------------------------------------

(defn pages-with-tag [tag]
  (with-db
    (couch/get-view "pages" "by_tag" {:key tag})))

(defn pages-with-word [word]
  (with-db
    (couch/get-view "pages" "by_word" {:key word})))

(defn who-links-to [id]
  (with-db
    (couch/get-view "page_graph" "who_links_to" {:key id})))
