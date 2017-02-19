(ns {{name}}.logic.tokens-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [{{name}}.logic.tokens :refer :all]
            [{{name}}.env :as env]
            [{{name}}.logic.users :as users]
            [{{name}}.server :refer [app]]
            [cheshire.core :as json]))

(defn req [method url body]
  (-> (mock/request method url)
      (mock/body (json/generate-string body))
      (mock/content-type "application/json")))

(deftest tokens-test
  (let [db-user {:id "uuid" :email "test@test.com" :password (users/encrypt-password "password123456")}
        req-user {:email "test@test.com" :password "password123456"}]
      (testing "with empty vals"
        (with-redefs [users/get-users-by-email (fn [v] [])]
          (let [response (app (req :post "/tokens" {}))]
            (is (= (:body response) (json/generate-string {:message "Invalid email or password"}))))))

      (testing "with valid email"
        (with-redefs [users/get-users-by-email (fn [v] [db-user])]
          (let [response (app (req :post "/tokens" req-user))]
            (is (= false (nil? (-> response :body (json/parse-string true) :access-token)))))))

      (testing "valid-user? with nils"
        (let [result (valid-user? nil nil)]
          (is (= false result))))

      (testing "valid-user? with a valid user"
        (let [result (valid-user? db-user {:email "test@test.com" :password "password123456"})]
          (is (= true result))))

      (testing "make"
        (let [result (make db-user env/secret)]
          (is (= false (-> result :access-token nil?)))))))
