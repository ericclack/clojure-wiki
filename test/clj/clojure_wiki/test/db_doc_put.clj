(ns clojure-wiki.test.db-doc-put
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))

(defn setup-docs [f]
  (utils/delete-if-exists! "a-new-page")
  ;; The test
  (f)
  ;; Tear down code
  )

(use-fixtures :once utils/use-test-database)
(use-fixtures :each setup-docs)

(deftest test-put
  (testing "putting a new doc"
    (is (some?
         (db/with-db (couch/put-document {:_id "a-new-page"
                                          :content "lorem ipsum"
                                          :tags (list "tag1" "tag2")})))))
  (testing "updating an existing doc"
    (let [doc (db/with-db (couch/get-document "a-new-page"))]
      (is (some?
           (db/with-db (couch/put-document {:_id "a-new-page"
                                            :_rev (:_rev doc)
                                            :content "Some new content"
                                            :tags (list "tag1" "tag2" "tag3")})))))))
