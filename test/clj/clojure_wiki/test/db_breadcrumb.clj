(ns clojure-wiki.test.db-wiki
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))

(defn setup-docs [f]
  (utils/delete-if-exists! "page-one")
  (utils/delete-if-exists! "page-two")
  ;; The test
  (f)
  ;; Tear down code
  )

(use-fixtures :once utils/use-test-database)
(use-fixtures :each setup-docs)

;; ----------------------------------------------------

(deftest creating-pages-with-links
  (testing "create a wiki page"
    (let [content "A very short page with a link to [[page-two]]"
          result (db/create-wiki-page! "page-one" content)]
      (is (= "page-one" (:_id result)))))
  (testing "create second wiki page"
    (let [content "Another page called page-two"
          result (db/create-wiki-page! "page-two" content)]
      (is (= "page-two" (:_id result)))))
  (testing "who-links-to has correct info"
    (let [content (db/wiki-page "page-two")
          links (db/who-links-to "page-two")]
      (is (= "page-one" (:id (first links))))))
  (testing "second page has breadcrumb including page that links"
    ;; hmmm does this test belong here?
    ))


