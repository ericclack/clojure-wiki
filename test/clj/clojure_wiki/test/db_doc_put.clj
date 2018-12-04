(ns clojure-wiki.test.db-doc-put
  (:require [com.ashafa.clutch :as couch]
            [environ.core :refer [env]]
            [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pp]]))

(defmacro with-db
  [& body]
  `(couch/with-db (env :database-test-url)
    ~@body))

(deftest test-put
  (testing "putting a new doc"
    (is (some?
         (with-db (couch/put-document {:_id "a-new-page"
                                       :content "lorem ipsum"
                                       :tags (list "tag1" "tag2")})))))
  (testing "updating an existing doc"
    (is (some?
         (with-db (couch/put-document {:_id "a-new-page"
                                       :_rev "TBC"
                                       :content "Some new content"
                                       :tags (list "tag1" "tag2" "tag3")}))))))
