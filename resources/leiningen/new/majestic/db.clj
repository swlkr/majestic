(ns {{name}}.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [{{name}}.env :as env]
            [{{name}}.utils :refer [now flip]])
  (:import (java.text SimpleDateFormat)
           (java.util Date)))

(defn get-user-and-pass [str]
  (let [val (-> str
                (s/split #"//")
                second
                (s/split #"@")
                first
                (s/split #":"))]
    [(first val) (second val)]))

(defn fmt-jdbc [conn-str]
  (if (and
        conn-str
        (= 3 (->> conn-str (re-seq #":") count)))
    (let [[user password] (get-user-and-pass conn-str)
          p-conn-str (-> conn-str
                         (s/split #"@")
                         second)]
      (str "jdbc:postgresql://" p-conn-str "?user=" user "&password=" password))
    (str "jdbc:" conn-str)))

(defonce conn {:connection-uri (fmt-jdbc env/database-url)})

(defn fmt-date [date]
  (when (instance? Date date)
    (.format (SimpleDateFormat. "yyyyMMddHHmmss") date)))

(def migrations-dir
  "Default migrations directory"
  "resources/migrations/")

(def ragtime-format-edn
  "EDN template for SQL migrations"
  "{:up [\"\"]\n :down [\"\"]}")

(defn ragtime-conn []
  {:datastore  (jdbc/sql-database conn)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (ragtime-conn)))

(defn rollback []
  (repl/rollback (ragtime-conn)))

(defn migrations-dir-exist? []
  (.isDirectory (io/file migrations-dir)))

(defn migration-file-path [name]
  (when (and
          ((comp not nil?) name)
          ((comp not empty?) name)
          (string? name))
    (str migrations-dir (-> (now) fmt-date) "_" (s/replace name #"\s+|-+|_+" "_") ".edn")))

(defn create-migration [name]
  (let [migration-file (migration-file-path name)]
    (if-not (migrations-dir-exist?)
      (io/make-parents migration-file))
    (spit migration-file ragtime-format-edn)))

(defn yesql-conn []
  {:connection conn})
