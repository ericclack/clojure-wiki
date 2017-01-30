(ns couchdb-test.test.db
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [couchdb-test.models.db :as db]))

(def test-wiki-page "test-wiki-page-123")

(defn doc-prep-fixture [f]
  ;; Set up code
  (when (some? (db/wiki-page test-wiki-page))
    (db/remove-wiki-page test-wiki-page))
  ;; The test
  (f)
  ;; Tear down code
)

(use-fixtures :each doc-prep-fixture)

(deftest test-db
  (testing "get a wiki page"
    (let [result (db/wiki-page "welcome")]
      (is (s/includes? (:content result) "Congratulations"))))

  (testing "create a wiki page"
    (let [page-id test-wiki-page
          content "A very short page"
          result (db/create-wiki-page page-id content)]
      (is (= (:_id result) page-id)))))
