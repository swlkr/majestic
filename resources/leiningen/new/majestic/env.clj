(ns {{name}}.env
  (:require [environ.core :as environ]
            [{{name}}.utils :refer [str->int]]))

(def port (str->int (environ/env :port)))
(def database-url (environ/env :database-url))
(def secret (environ/env :secret))
