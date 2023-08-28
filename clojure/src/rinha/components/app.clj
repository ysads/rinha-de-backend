(ns rinha.components.app
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [compojure.handler :refer [api]]
            [mount.core :refer [defstate]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.logger.timbre :refer [wrap-with-logger make-timbre-logger]]
            [ring.logger :as logger]
            [ring.middleware.reload :refer [wrap-reload]]
            [rinha.models.person :as person]
            [rinha.components.db :as db :refer [*db*]]
            [taoensso.timbre :as timbre])
  (:gen-class))

(defn response
  ([status]
   (response status "" {}))
  ([status body]
   (response status body {}))
  ([status body headers]
   {:status status
    :body body
    :headers headers}))

(defn create-person [req]
  (if (person/valid? (:body req))
    (try
      (let [result (db/create-person! (:body req))]
        (response 201 "" {"Location" (str "/pessoas/" (:id result))}))
      (catch Exception ex
        (response 422)))
    (response 400)))

(defn search-people [t]
  (if-not (empty? t)
   (let [people (db/get-all-people *db*)]
     (response 200 people))
    (response 400)))

(defn single-person [id]
  (if-let [person (db/get-person-by-id *db* {:id id})]
    (response 200 person)
    (response 404)))

(defn count-people [_]
  (-> (db/count-all-people *db*) :count str))

(defroutes app-routes
  (POST "/pessoas" [] create-person)
  (GET "/pessoas" [t] (search-people t))
  (GET "/pessoas/:id" [id] (single-person id))
  (GET "/contagem-pessoas" [] count-people)
  (route/not-found "Not Found"))

(defn redacted-key? [k]
  (contains? #{:authorization :password :token :secret :secret-key :secret-token :pwd :pass} k))

(defn redact-keys [body]
  "Removes sensitive information from a request body by checking against a pre-defined set of keys."
  (reduce (fn [acc [k v]]
            (if (redacted-key? k)
                acc
                (assoc acc k v)))
          {}
          body))

(defn wrap-with-request-body [handler]
  (fn [request]
    (let [body (:body request)]
      (taoensso.timbre/info "Request body: " (if (map? body)
                                               (redact-keys body)
                                               body)))
    (handler request)))

(defstate app
  :start (-> app-routes
             api
             wrap-with-request-body ;; INFO: needs to come after wrap-json-body since it expects body to already be a map
             (wrap-json-body {:keywords? true :bigdecimals? true})
             wrap-with-logger
             wrap-keyword-params
             wrap-json-response)
  :stop (println "[app] stopping. noop"))