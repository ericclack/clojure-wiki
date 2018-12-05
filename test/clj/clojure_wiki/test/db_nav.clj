(ns clojure-wiki.test.db-nav
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
  (utils/delete-if-exists! db/nav-bar-id)
  (db/create-wiki-page! db/nav-bar-id '(welcome))
  ;; The test
  (f)
  ;; Tear down code
)

(use-fixtures :once utils/use-test-database)
(use-fixtures :each doc-prep-fixture)

;; ----------------------------------------------------

(deftest test-nav-bar
  (testing "get the nav bar"
    (let [nav-bar (db/get-nav-bar)]
      (is (seq? nav-bar))))
  (testing "set the nav bar"
    (let [items '(welcome about holidays)
          nav-bar (db/set-nav-bar! items)]
      (is (= 'welcome (first nav-bar))))))
