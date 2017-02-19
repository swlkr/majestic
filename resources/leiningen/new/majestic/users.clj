(ns {{name}}.logic.users
  (:require [cheshire.core :as json]
            [environ.core :refer [env]]
            [org.httpkit.client :as http]
            [clojurewerkz.scrypt.core :as sc]
            [yesql.core :as yesql]
            [compojure.core :refer [defroutes POST]]
            [{{name}}.db :as db]
            [{{name}}.utils :refer [uuid flip throw+]]
            [{{name}}.http :refer [ok bad-request]]))

(yesql/defqueries "sql/users.sql" (db/yesql-conn))

(defn email? [str]
  (and
    (string? str)
    (not (nil? (re-find #".+@.+\." str)))))

(defn decent-password? [password]
  (let [PASSWORD_LENGTH 12]
    (and
      (string? password)
      (> (count password) PASSWORD_LENGTH))))

(defn matching-passwords? [[password confirm-password]]
  (= password confirm-password))

(defn bind-error [f [val err]]
  (if (nil? err)
    (f val)
    [nil err]))

(defmacro err->> [val & fns]
  (let [fns (for [f fns] `(bind-error ~f))]
    `(->> [~val nil]
          ~@fns)))

(defn validate-email [{:keys [email] :as user}]
  (if (email? email)
    [user nil]
    [nil "That's not an email"]))

(defn validate-password [{:keys [password] :as user}]
  (if (decent-password? password)
    [user nil]
    [nil "Passwords need to be at least 12 characters"]))

(defn validate-matching-passwords [{:keys [password confirm-password] :as user}]
  (if (matching-passwords? [password confirm-password])
    [user nil]
    [nil "Passwords need to match"]))

(defn validate-user [{:keys [email password confirm-password] :as user}]
  (err->> user
          validate-email
          validate-password
          validate-matching-passwords))

(defn encrypt-password [password]
  (sc/encrypt password 16384 8 1))

(defn body->db [body]
  (let [ec (encrypt-password (:password body))]
   (assoc body :id (uuid) :password ec)))

(defn create! [{:keys [body]}]
  (let [[new-user err] (validate-user body)]
    (if (nil? err)
      (-> new-user
          body->db
          insert-user<!
          (dissoc :password)
          ok)
      (bad-request {:message err}))))

(defroutes routes
  (POST "/users" [] create!))
