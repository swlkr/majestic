(ns {{name}}.routes
  (:require [compojure.core :refer [defroutes GET POST wrap-routes]]
            [compojure.route :as route]
            [{{name}}.http :refer [ok unauthorized not-found]]
            [{{name}}.logic.users :as users]
            [{{name}}.logic.tokens :as tokens]
            [{{name}}.utils :refer [throw+]]
            [{{name}}.env :as env]))

(defn authorization->token [req]
  (some-> req
          (get-in [:headers "authorization"])
          (clojure.string/split #" ")
          second))

(defn wrap-jwt [handler]
  (fn [request]
    (let [user (some-> request
                       authorization->token
                       (tokens/decode! env/secret))]
      (handler (assoc request :user user)))))

(defn wrap-auth [handler]
  (fn [request]
    (let [{:keys [user]} request]
      (if (not (nil? user))
        (handler request)
        (unauthorized {:message "You don't have permission to do that"})))))

(defroutes protected-routes
  (POST "/" {body :body} (ok body)))

(defroutes routes
  (GET "/" [] (ok {:message "hello, world!"}))
  users/routes
  tokens/routes
  (-> protected-routes
      (wrap-routes wrap-jwt)
      (wrap-routes wrap-auth))
  (route/not-found (not-found {:message "Hello?! Is it me you're looking for?!"})))
