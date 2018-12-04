(ns clojure-wiki.test.db-doc-connection
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]))

(defmacro with-db
  [& body]
  `(couch/with-db (env :database-url)
    ~@body))

(defn doc-revisions
  [id]
  (:ids (:_revisions (with-db (couch/get-document id :revs true)))))

(deftest test-revision
  (testing "getting doc revisions"
    (is (some? (with-db (couch/get-document "ideal-wiki" :revs true)))))
  (testing "getting an older revision"
    (is (some? (with-db (couch/get-document "ideal-wiki" :rev "6-fcb4d3cc5a8e683cc35eb64d8d1f8ba2"))))))

