(ns couchdb-test.models.db
  (:require [com.ashafa.clutch :as clutch]))

(defn wiki-page [id]
  (clutch/with-db "wiki" (clutch/get-document id)))
