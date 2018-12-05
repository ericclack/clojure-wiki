(ns clojure-wiki.test.db-doc-get
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))

(defn setup-docs [f]
  (utils/delete-if-exists! "ideal-wiki")
  (db/create-wiki-page! "ideal-wiki" "Some content")
  ;; The test
  (f)
  ;; Tear down code
  )

(use-fixtures :once utils/use-test-database)
(use-fixtures :each setup-docs)

(deftest test-get
  (testing "getting doc"
    (is (some? (db/with-db (couch/get-document "ideal-wiki")))))
  (testing "getting all docs"
    (is (seq? (db/with-db (couch/all-documents))))))


