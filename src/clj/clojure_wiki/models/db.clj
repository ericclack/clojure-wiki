(ns clojure-wiki.models.db
  (:require [com.ashafa.clutch :as couch]))

(defn wiki-page [id]
  (couch/with-db "wiki" (couch/get-document id)))

(defn create-wiki-page [id content]
  (couch/with-db "wiki"
    (couch/put-document {:_id id :content content})))

(defn remove-wiki-page [id rev]
  (couch/with-db "wiki"
    (couch/delete-document {:_id id :_rev rev})))

(defn update-wiki-page [id rev content]
  (couch/with-db "wiki"
    (couch/put-document {:_id id :_rev rev :content content})))  
