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

(defn create-wiki-page! [id content]
  (with-db (couch/put-document {:_id id :content content})))

(defn remove-wiki-page! [id rev]
  (with-db (couch/delete-document {:_id id :_rev rev})))

(defn update-wiki-page! [id rev content]
  (with-db (couch/put-document
            {:_id id :_rev rev :content content})))  

;; --------------------------------------------------

(def nav-bar-id "-nav-bar")

(defn get-nav-bar []
  (with-db
    (list* (:content (couch/get-document nav-bar-id)))))

(defn set-nav-bar! [items]
  (with-db
    (let [current-bar (couch/get-document nav-bar-id)
          new-bar (couch/put-document (merge current-bar {:content items}))]
      (:content new-bar))))
