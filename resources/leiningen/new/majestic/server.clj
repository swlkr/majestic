(ns {{name}}.server
  (:require [org.httpkit.server :as http]
            [clojure.tools.namespace.repl :as tn]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [{{name}}.env :as env]
            [{{name}}.routes :refer [routes]]
            [{{name}}.http :refer [response]]
            [{{name}}.utils :refer [throw+]]
            [{{name}}.logic.tokens :as tokens]))

(defn wrap-exceptions [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (let [{:keys [status]} (ex-data e)
              message (.getMessage e)]
          (response (or status 500) {:message message}))))))

(def app
  (-> routes
      (wrap-exceptions)
      (wrap-defaults api-defaults)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

(defonce server (atom nil))

(defn start []
  (let [opts {:port env/port}]
    (reset! server (http/run-server #'app opts))))

(defn stop []
  (when @server
    (@server :timeout 100)
    (reset! server nil)))

(defn restart []
  (stop)
  (tn/refresh :after '{{name}}.core/start))
