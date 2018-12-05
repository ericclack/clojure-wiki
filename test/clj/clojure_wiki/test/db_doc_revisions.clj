(ns clojure-wiki.test.db-doc-connection
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))

(defn setup-docs [f]
  (utils/delete-if-exists! "ideal-wiki")
  (let* [doc1 (db/create-wiki-page! "ideal-wiki" "Some content")
         doc2 (db/update-wiki-page! "ideal-wiki" (:_rev doc1)
                                    "Some new content")
         doc3 (db/update-wiki-page! "ideal-wiki" (:_rev doc2)
                                    "Some newer content")]
    ;; The test
    (f)
    ;; Tear down code
    ))

(use-fixtures :once utils/use-test-database)
(use-fixtures :each setup-docs)

(defn doc-revisions
  [id]
  (:ids (:_revisions (db/with-db (couch/get-document id :revs true)))))

(deftest test-revision
  (testing "getting doc revisions"
    (is (some? (doc-revisions "ideal-wiki"))))
  (testing "getting an older revision"
    (let [revisions (doc-revisions "ideal-wiki")
          choice (rand-int (count revisions))
          rev_counter (- (count revisions) choice)
          rev_id (str rev_counter "-" (nth revisions choice))]
      (print rev_id)
      (is (some? (db/with-db (couch/get-document
                              "ideal-wiki"
                              :rev rev_id)))))))
                              

