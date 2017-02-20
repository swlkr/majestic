(ns leiningen.new.majestic
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main])
  (:import (java.text SimpleDateFormat)
           (java.util Date UUID)))

(def render (renderer "majestic"))

(def date-format "yyyyMMddHHmmss")

(defn new-uuid []
  (UUID/randomUUID))

(defn now []
  (.format (SimpleDateFormat. date-format) (new Date)))

(defn fmt-migration-name [name]
  (str (now) "_" (clojure.string/replace name #"\s+|-+|_+" "_") ".edn"))

(defn majestic
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)
              :hstore-mig (fmt-migration-name "add-hstore-ext")
              :users-mig (fmt-migration-name "create-users-table")
              :test-secret (new-uuid)
              :dev-secret (new-uuid)}]
    (main/info "Generating fresh 'lein new' majestic project.")
    (->files data
             [".gitignore" (render ".gitignore" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/{{sanitized}}/db.clj" (render "db.clj" data)]
             ["test/{{sanitized}}/db_test.clj" (render "db_test.clj" data)]
             ["src/{{sanitized}}/env.clj" (render "env.clj" data)]
             ["resources/migrations/{{hstore-mig}}" (render "hstore-mig.edn" data)]
             ["src/{{sanitized}}/http.clj" (render "http.clj" data)]
             ["Procfile" (render "Procfile" data)]
             ["profiles.clj" (render "profiles.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["test/{{sanitized}}/routes_test.clj" (render "routes_test.clj" data)]
             ["src/{{sanitized}}/routes.clj" (render "routes.clj" data)]
             ["test/{{sanitized}}/server_test.clj" (render "server_test.clj" data)]
             ["src/{{sanitized}}/server.clj" (render "server.clj" data)]
             ["test/{{sanitized}}/logic/tokens_test.clj" (render "tokens_test.clj" data)]
             ["src/{{sanitized}}/logic/tokens.clj" (render "tokens.clj" data)]
             ["test/{{sanitized}}/logic/users_test.clj" (render "users_test.clj" data)]
             ["resources/migrations/{{users-mig}}" (render "users-mig.edn" data)]
             ["src/{{sanitized}}/logic/users.clj" (render "users.clj" data)]
             ["resources/sql/users.sql" (render "users.sql" data)]
             ["src/{{sanitized}}/utils.clj" (render "utils.clj" data)])))
