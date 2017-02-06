(ns couchdb-test.test.markdown
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [couchdb-test.filters :refer [my-markdown]]))

;; To Do:
;; 

(deftest test-simple-markdown
  (testing "bold"
    (let [markdown-text "Hello **bold** text"
          transformed (my-markdown markdown-text)]
      (is (s/includes? transformed "<strong>bold</strong>")))))

(deftest test-wiki-links
  (testing "wiki-link"
    (let [markdown-text "Hello [[Wiki Link]] text"
          transformed (my-markdown markdown-text)]
      (is (s/includes? transformed "<a href=\"wiki-link\"")))))
