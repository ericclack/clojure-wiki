(ns couchdb-test.models.db
  (:require [com.ashafa.clutch :as clutch]))

(defn wiki-page [id]
  (clutch/with-db "wiki" (clutch/get-document id)))

(defn create-wiki-page [id content]
  (clutch/with-db "wiki"
    (clutch/put-document {:_id id :content content})))

(defn remove-wiki-page [id]
  (clutch/with-db "wiki"
    (clutch/delete-document id)))
