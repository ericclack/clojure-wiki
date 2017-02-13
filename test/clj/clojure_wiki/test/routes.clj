(ns clojure-wiki.test.routes
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [clojure-wiki.routes.home :refer :all]))

(deftest test-app-input
  (testing "test split of tag string into list"
    (let [tags "tag1, tag2, tag3 tag4"
          result (split-tags tags)]
      (is (= '["tag1" "tag2" "tag3" "tag4"] result))))

  (testing "test empty tags - spaces"
    (is (empty? (split-tags "   "))))

  (testing "test empty string"
    (is (empty? (split-tags "")))))
