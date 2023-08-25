(ns rinha.components.app
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [compojure.handler :refer [api]]
            [mount.core :refer [defstate]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [rinha.models.person :as person]
            [rinha.components.db :as db :refer [*db*]])
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

(defstate app
  :start (-> app-routes
             api
            ;;  wrap-reload
             (wrap-json-body {:keywords? true :bigdecimals? true})
             wrap-keyword-params
             wrap-json-response)
  :stop (println "[app] stopping. noop"))