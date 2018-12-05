(ns clojure-wiki.test.utils
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [environ.core :refer [env]]
            [clojure-wiki.models.db :as db]))


(defn use-test-database
  "Use test database instance"
  [f]
  (with-redefs [db/database-url (env :database-test-url)]
    (f)
    ))

(defn delete-if-exists!
  [id]
  (let [page (db/wiki-page id)]
    (when (some? page)
      (db/remove-wiki-page! id (:_rev page)))))

