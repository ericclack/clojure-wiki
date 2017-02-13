(ns clojure-wiki.test.db-wiki
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure-wiki.models.db :as db]))

;; Run this from lein test so that it uses the test environment
;; and database-url.

;; To Do:
;; - 

(defn delete-if-exists
  [id]
  (let [page (db/wiki-page id)]
    (when (some? page)
      (db/remove-wiki-page! id (:_rev page)))))

(defn doc-prep-fixture [f]
  ;; Set up code
  (delete-if-exists "-test-123")
  ;; The test
  (f)
  ;; Tear down code
)

(use-fixtures :each doc-prep-fixture)

;; ----------------------------------------------------

(deftest test-getting-and-adding
  (testing "get a wiki page"
    (let [result (db/wiki-page "welcome")]
      (is (s/includes? (:content result) "Congratulations"))))

  (testing "create a wiki page"
    (let [content "A very short page"
          result (db/create-wiki-page! "-test-123" content)]
      (is (= (:_id result) "-test-123")))))


(deftest test-removing-editing
  (testing "add and remove a wiki page"
    (let [page-id "-test-wiki-page-999"
          test-content "One two three four"
          created-page (db/create-wiki-page! page-id test-content)
          deleted-page (db/remove-wiki-page! page-id (:_rev created-page))]
      (is (s/includes? (:content created-page) "two three"))
      (is (= (:ok deleted-page) true))
      (is (= (:id deleted-page) page-id))))
  
  (testing "edit a wiki page"
    (let [content1 "A very short page"
          content2 "A slightly longer page"
          result (db/create-wiki-page! "-test-123" content1)
          update (db/update-wiki-page! "-test-123" (:_rev result) content2)]
      (is (= (:_id result) "-test-123"))
      (is (s/includes? (:content update) "slightly")))))
  
;; ----------------------------------------------------

(deftest test-adding-tags
  (testing "create a wiki page with a tag"
    (let [content "some simple content"
          tags '(one two three)
          new-page (db/create-wiki-page! "-test-123" content tags)]
      (is (s/includes? (:content new-page) "simple"))
      (is (= 'one (nth (:tags new-page) 0)))))

  (testing "add some tags to a wiki page"
    (let [current-page (db/wiki-page "-test-123")
          updated-page (db/update-wiki-page! "-test-123"
                                             (:_rev current-page)
                                             "new-content"
                                             '(tag1 tag2))]
      (is (= (:content updated-page) "new-content"))
      (is (= 'tag2 (nth (:tags updated-page) 1))))))
          
(deftest getting-docs-by-tags
  (testing "emply list for unused tag"
    (let [results (db/pages-with-tag "zzzzzzzzzzzzz")]
      (is (empty? results))))

  (testing "get a list of docs by tag"
    (let [test-doc (db/create-wiki-page! "-test-123"
                                         "some content"
                                         '(testtagA testtagB))
          results (db/pages-with-tag "testtagA")]

      (is (= 1 (count results)))
      (is (= "-test-123" (:id (nth results 0)))))))
