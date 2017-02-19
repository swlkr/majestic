(ns {{name}}.db-test
  (:require [clojure.test :refer :all]
            [{{name}}.db :refer :all]
            [{{name}}.utils :refer [now]])
  (:import (java.util Date)
           (java.text SimpleDateFormat)))

(let [date-str "20170101000000"
      parser (SimpleDateFormat. "yyyyMMddHHmmss")
      date (.parse parser date-str)]

  (deftest test-fmt-date
    (testing "with nil"
      (is (= nil (fmt-date nil))))

    (testing "with a string"
      (is (= nil (fmt-date "hello"))))

    (testing "with a date"
        (is (= date-str (fmt-date date)))))

  (deftest test-ragtime-conn
    (testing "with nil"
      (with-redefs [conn {:connection-uri nil}]
        (let [rag-conn (ragtime-conn)]
          (is (= (every? rag-conn [:datastore :migrations]) true))))))

  (deftest test-migration-file-path
    (testing "with nil"
      (is (= nil (migration-file-path nil))))

    (testing "with a string"
      (with-redefs [now (fn [] date)]
        (is (= (str "resources/migrations/" date-str "_create_users_table.edn")
               (migration-file-path "create-users-table")))))))
