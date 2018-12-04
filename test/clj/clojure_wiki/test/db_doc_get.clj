(ns clojure-wiki.test.db-doc-get
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]))

(defmacro with-db
  [& body]
  `(couch/with-db (env :database-url)
    ~@body))

(deftest test-get
  (testing "getting doc"
    (is (some? (with-db (couch/get-document "ideal-wiki")))))
  (testing "getting all docs"
    (is (seq? (with-db (couch/all-documents))))))


