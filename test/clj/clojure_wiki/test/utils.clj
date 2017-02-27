(ns clojure-wiki.test.utils
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure-wiki.models.db :as db]))


(defn delete-if-exists
  [id]
  (let [page (db/wiki-page id)]
    (when (some? page)
      (db/remove-wiki-page! id (:_rev page)))))

