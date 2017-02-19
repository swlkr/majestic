(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :min-lein-version "2.6.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.2.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ragtime/ragtime.jdbc "0.6.3"]
                 [ring/ring-mock "0.3.0"]
                 [compojure "1.5.1"]
                 [http-kit "2.2.0"]
                 [yesql "0.5.3"]
                 [environ "1.1.0"]
                 [cheshire "5.7.0"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.13.0"]
                 [clojurewerkz/scrypt "1.2.0"]]
  :plugins [[lein-environ "1.0.3"]]
  :main {{name}}.core
  :source-paths ["src"]
  :test-paths ["test"]
  :aliases {"migrate"          ["run" "-m" "{{name}}.db/migrate"]
            "rollback"         ["run" "-m" "{{name}}.db/rollback"]
            "create-migration" ["run" "-m" "{{name}}.db/create-migration"]}
  :profiles {:uberjar {:aot :all
                       :uberjar-name "{{name}}.jar"}})
