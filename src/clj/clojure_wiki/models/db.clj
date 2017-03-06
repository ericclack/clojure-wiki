(ns clojure-wiki.models.db
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]))

(defmacro with-db
  [& body]
  `(couch/with-db (env :database-url)
    ~@body))

(defn- make-history-ids [id revisions]
  "A list of dics representing this page's history: {id, rev}"
  (let [latest-rev (:start revisions)]
    (map-indexed #(hash-map :id id :rev (str (- latest-rev %1) "-" %2))
                 (:ids revisions))))

(defn- time-now []
  "Generate the time now as a string like 2012-04-23T18:25:43.511Z"
  (.toString (java.time.LocalDateTime/now)))

(defn- add-in-timestamps [docs]
  (map #(assoc % :timestamp
               (:timestamp (with-db (couch/get-document (:id %) :rev (:rev %)))))
       docs))

;; --------------------------------------------------

(defn wiki-page
  "Return the wiki page with this id, and optionally a revision.
  The return is a hash-map with keys :_id :_rev :content :tags :timestamp."
  ([id] (with-db (couch/get-document id)))
  ([id rev] (with-db (couch/get-document id :rev rev))))

(defn wiki-page-history [id]
  (let [histdoc (with-db (couch/get-document id :revs true))]
    (add-in-timestamps (make-history-ids id (:_revisions histdoc)))))

(defn create-wiki-page!
  ([id content] (create-wiki-page! id content nil))
  ([id content tags]
   (with-db (couch/put-document {:_id id
                                 :content content
                                 :tags tags
                                 :timestamp (time-now)}))))

(defn remove-wiki-page! [id rev]
  (with-db (couch/delete-document {:_id id :_rev rev})))

(defn update-wiki-page!
  ([id rev content] (update-wiki-page! id rev content nil))
  ([id rev content tags]
   (with-db (couch/put-document {:_id id
                                 :_rev rev
                                 :content content
                                 :tags tags
                                 :timestamp (time-now)}))))  

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
