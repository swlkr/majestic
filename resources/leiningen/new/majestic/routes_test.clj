(ns {{name}}.routes-test
  (:require [clojure.test :refer :all]
            [{{name}}.routes :refer :all]
            [{{name}}.logic.tokens :as tokens]
            [{{name}}.env :as env]))

(deftest test-authorization->token
  (testing "without auth header"
    (is (= nil (authorization->token nil))))

  (testing "with an invalid auth header"
    (is (= nil (authorization->token {:headers {"authorization" "no-space"}}))))

  (testing "with an auth header"
    (is (= "hello" (authorization->token {:headers {"authorization" "bearer hello"}})))))

(deftest wrap-jwt-test
  (let [fn (wrap-jwt (fn [req] req))]
    (testing "without an auth header"
      (is (= {:user nil} (fn {}))))

    (testing "with an invalid encoded auth header"
      (let [token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJlbWFpbCIsImlzcyI6InNlbGYiLCJleHAiOjE0Njc0Mzc0MDcsImlhdCI6MTQ2NjgzMjYwN30.o7t4U5_wQAbBPMt5wyvBpOJZVx7PRrohkirSzli6DsU"]
        (is (= nil (-> (fn {:headers {"authorization" (str "bearer " token)}}) :user :id)))))

    (testing "with a valid encoded auth header"
      (let [token (tokens/gen {:id 123} env/secret)]
        (is (= "123" (-> (fn {:headers {"authorization" (str "bearer " token)}}) :user :id)))))))

(deftest wrap-auth-test
  (let [fn (wrap-auth (fn [req] req))]
    (testing "without a user"
      (is (= "You don't have permission to do that" (-> (fn {:user nil}) :body :message))))

    (testing "with a user"
      (is (= {:user "user"} (fn {:user "user"}))))))
