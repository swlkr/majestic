(ns {{name}}.core
  (:require [{{name}}.server :as server]
            [{{name}}.env :as env]
            [{{name}}.utils :as utils])
  (:gen-class))

(def start server/start)
(def stop server/stop)
(def restart server/restart)

(utils/add-json-to-yesql)

(defn -main [& [port]]
  (start)
  (println (str "Listening on " (or port env/port))))
