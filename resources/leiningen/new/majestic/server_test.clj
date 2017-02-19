(ns {{name}}.server-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [{{name}}.server :refer :all]
            [cheshire.core :as json]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) (json/generate-string {:message "hello, world!"})))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
