(ns clojure-wiki.test.db-wiki
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))

;; Run this from lein test so that it uses the test environment
;; and database-url.

;; To Do:
;; - 

(defn doc-prep-fixture [f]
  ;; Set up code
  (utils/delete-if-exists "-test-123")
  (utils/delete-if-exists "-test-124")
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

;; ----------------------------------------------------

(deftest word-search
  (testing "find no docs"
    (let [results (db/pages-with-word "definitelydoesntexist")]
      (is (empty? results))))

  (testing "find one doc"
    (let [test-doc (db/create-wiki-page! "-test-123"
                                         "some content with an orangeorangeorange in it")
          results (db/pages-with-word "orangeorangeorange")]
      (is (= 1 (count results)))
      (is (= "-test-123" (:id (first results)))))))

;; -----------------------------------------------------

(deftest who-links-to
  (testing "simple link"
    (let [page1 (db/create-wiki-page! "-test-123"
                                      "some content with a [[-test-124]] link")
          page2 (db/create-wiki-page! "-test-124"
                                      "some more content")
          results (db/who-links-to "-test-124")]
      (is (= 1 (count results)))
      (is (= "-test-123" (:id (first results)))))))

(deftest who-links-to-2
  (testing "lower-case link"
    (let [page1 (db/create-wiki-page! "-test-123"
                                      "some content with a [[-Test-124]] link")
          page2 (db/create-wiki-page! "-test-124"
                                      "some more content")
          results (db/who-links-to "-test-124")]
      (is (= "-test-123" (:id (first results)))))))

(deftest who-links-to-3
  (testing "links with spaces"
    (let [page1 (db/create-wiki-page! "-test-123"
                                      "some content with a [[-Test 124]] link")
          page2 (db/create-wiki-page! "-test-124"
                                      "some more content")
          results (db/who-links-to "-test-124")]
      (is (= "-test-123" (:id (first results)))))))

;; -----------------------------------------------------

(deftest simple-history
  (testing "wiki-page-history"
    (let [v1 (db/create-wiki-page! "-test-123" "some content")
          v2 (db/update-wiki-page! "-test-123" (:_rev v1) "some new content")
          history (db/wiki-page-history "-test-123")]
      (is (<= 2 (count history)))
      (is (= (:_rev v2) (:rev (first history))))
      (is (= (:_rev v1) (:rev (second history)))))))

;; -----------------------------------------------------

(deftest doc-dates
  (testing "wiki-page-timestamp"
    (let [doc (db/create-wiki-page! "-test-123" "a doc")]
      (is (not (nil? (:timestamp doc)))))))

(deftest history-dates
  (testing "history with dates"
    (let [v1 (db/create-wiki-page! "-test-123" "some content")
          v2 (db/update-wiki-page! "-test-123" (:_rev v1) "some new content")
          history (db/wiki-page-history "-test-123")]
      (is (= (:timestamp v2) (:timestamp (first history))))
      (is (= (:timestamp v1) (:timestamp (second history)))))))
