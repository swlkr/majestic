(ns {{name}}.logic.tokens
  (:require [clj-jwt.core :refer [verify jwt to-str sign str->jwt]]
            [clj-time.core :refer [now plus days]]
            [clojurewerkz.scrypt.core :as sc]
            [{{name}}.utils :refer [flip]]
            [{{name}}.http :refer [ok unauthorized]]
            [{{name}}.env :as env]
            [{{name}}.logic.users :as users]
            [compojure.core :refer [defroutes POST]]))

(defn id->str [m]
  (assoc m :id (.toString (:id m))))

(defn gen [payload secret]
  (let [claim {:iss "self"
               :exp (plus (now) (days 1))
               :iat (now)}]
    (-> (id->str payload)
        (merge claim)
        (jwt)
        (sign :HS256 secret)
        (to-str))))

(defn valid? [token secret]
  (and
    (every? (comp not nil?) [token secret])
    (-> token str->jwt (verify secret))))

(defn decode! [token secret]
  (when (valid? token secret)
    (-> token str->jwt :claims)))

(defn correct-password? [req-pass db-pass]
  (sc/verify req-pass db-pass))

(defn valid-user? [user req-user]
  (and
    ((comp not nil?) user)
    (correct-password? (:password req-user) (:password user))))

(defn make [user secret]
  (when (and
          (not-any? nil? [user secret])
          (map? user)
          (string? secret))
    (-> user
        (select-keys [:id :email])
        (gen secret)
        ((flip hash-map) :access-token))))

(defn create! [{:keys [body]}]
  (let [{:keys [email password]} body
        user (some-> {:email email} users/get-users-by-email first)]
    (if (valid-user? user body)
      (ok (make user env/secret))
      (unauthorized {:message "Invalid email or password"}))))

(defroutes routes
  (POST "/tokens" [] create!))
