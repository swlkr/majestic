(ns {{name}}.env
  (:require [environ.core :as environ]
            [{{name}}.utils :refer [str->int]]))

(def port (str->int (environ/env :port)))
(def database-url (str "jdbc:" (environ/env :database-url)))
(def secret (environ/env :secret))
