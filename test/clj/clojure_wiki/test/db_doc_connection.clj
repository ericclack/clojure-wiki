(ns clojure-wiki.test.db-doc-connection
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]))

(deftest test-connection
  (testing "connecting with a string"
    (is (some? (couch/get-document "wiki" "home-page"))))
  (testing "connecting with env"
    (is (some? (couch/get-document (env :database-url) "home-page")))))


(defmacro with-db
  [& body]
  `(couch/with-db (env :database-url)
    ~@body))

(deftest test-connection2
  (testing "connecting with macro"
    (is (some? (with-db (couch/get-document "blogs"))))))
