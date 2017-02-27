(ns clojure-wiki.test.handler
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [ring.mock.request :refer :all]
            [clojure-wiki.handler :refer :all]
            [clojure-wiki.models.db :as db]
            [clojure-wiki.test.utils :as utils]))


(defn handler-fixture [f]
  ;; Set up code
  (utils/delete-if-exists "-test-123")
  (db/create-wiki-page! "-test-123" "some content")
  ;; The test
  (f)
  ;; Tear down code
  )

(use-fixtures :each handler-fixture)

;; -----------------------------------------------------------

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

(deftest test-wiki-actions
  (testing "history simple view"
    (let [response ((app) (request :get "/_history/-test-123"))]
      (is (= 200 (:status response)))))

  (testing "history rev list"
    (let [v1 (db/wiki-page "-test-123")
          v2 (db/update-wiki-page! "-test-123" (:_rev v1) "some new content")
          response ((app) (request :get "/_history/-test-123"))]
      (is (s/includes? (:body response) (:_rev v1)))
      (is (s/includes? (:body response) (:_rev v2)))))

  (testing "history page view"
    (let [v1 (db/wiki-page "-test-123")
          v2 (db/update-wiki-page! "-test-123" (:_rev v1) "some new content")
          history (db/wiki-page-history "-test-123")
          url (str "/-test-123/" (:rev (second history)))
          response ((app) (request :get url))]
      (is (= 200 (:status response))))))
