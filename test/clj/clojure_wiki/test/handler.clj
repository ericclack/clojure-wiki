(ns clojure-wiki.test.handler
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [ring.mock.request :refer :all]
            [clojure-wiki.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "no existing page, show creation form"
    (let [response ((app) (request :get "/new-page"))]
      (is (= 200 (:status response)))
      (is (s/includes? (:body response) "Create"))))

  (testing "test we can println"
    ;; But where does it go?
    (println "hello"))
  
  (testing "create a new page - post request not authorised"
    (let [response ((app) (request :post "/_create/a-new-page" {}))]
      (is (= 403 (:status response))))))

