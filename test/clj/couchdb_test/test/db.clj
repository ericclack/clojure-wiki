(ns couchdb-test.test.db
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [couchdb-test.models.db :as db]))

;; To Do:
;; - Add new test database instance so we don't accidentally
;;   clobber live data!

(def test-page-id "test-wiki-page-123")

(defn doc-prep-fixture [f]
  ;; Set up code
  (let [test-page (db/wiki-page test-page-id)]
    (when (some? test-page)
      (println "page exists, deleting it")
      (db/remove-wiki-page test-page-id (:_rev test-page))))
  ;; The test
  (f)
  ;; Tear down code
)

(use-fixtures :each doc-prep-fixture)

(deftest test-db
  (testing "get a wiki page"
    (let [result (db/wiki-page "welcome")]
      (is (s/includes? (:content result) "Congratulations"))))

  (testing "add and remove a wiki page"
    (let [page-id "test-wiki-page-999"
          test-content "One two three four"
          created-page (db/create-wiki-page page-id test-content)
          deleted-page (db/remove-wiki-page page-id (:_rev created-page))]
      (is (s/includes? (:content created-page) "two three"))
      (is (= (:ok deleted-page) true))
      (is (= (:id deleted-page) page-id))))
  
  (testing "create a wiki page"
    (let [content "A very short page"
          result (db/create-wiki-page test-page-id content)]
      (is (= (:_id result) test-page-id)))))
