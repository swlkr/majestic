(ns {{name}}.http
  (:require [clojure.string :as s]
            [{{name}}.utils :as utils]))

; responses
(defonce default-headers {"Content-Type" "application/json"})

(defn response [status body & {:as headers}]
  {:status status
   :body body
   :headers (merge default-headers headers)})

(def ok (partial response 200))
(def bad-request (partial response 400))
(def unauthorized (partial response 401))
(def not-found (partial response 400))
(def server-error (partial response 500))
