(ns {{name}}.logic.users-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [{{name}}.logic.users :refer :all]
            [{{name}}.server :refer [app]]
            [cheshire.core :as json]))

(defn req [method url body]
  (-> (mock/request method url)
      (mock/body (json/generate-string body))
      (mock/content-type "application/json")))

(deftest users-test
  (let [new-user {:email "test@test.com" :password "password123456" :confirm-password "password123456"}
        new-user-res {:email "test@test.com" :id "uuid" :created_at ""}]
    (with-redefs [insert-user<! (fn [v] new-user-res)]
      (testing "with empty vals"
        (let [response (app (req :post "/users" {}))]
          (is (= (:body response) (json/generate-string {:message "That's not an email"})))))

      (testing "with valid email"
        (let [response (app (req :post "/users" {:email "test@example.com"}))]
          (is (= (:body response) (json/generate-string {:message "Passwords need to be at least 12 characters"})))))

      (testing "with valid everything"
        (let [response (app (req :post "/users" new-user))]
          (is (= (:body response) (json/generate-string new-user-res)))))

      (testing "body->db"
        (let [db-params (body->db {:email "hello" :password "hello"})]
          (is (= true (every? db-params [:email :password :id]))))))))
