(ns rinha.core
  (:require [mount.core :as mount :refer [defstate]]
            [rinha.components.app :refer [app]]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defstate http-server
  :start (let [port (if (System/getenv "PORT")
                      (Integer/parseInt (System/getenv "PORT"))
                      9999)]
           (println "[http] started on port " port)
           (jetty/run-jetty app
                            {:port port
                             :join? true}))
  :stop (do
          (println "[http] stopping")
          (.stop http-server)
          (.join http-server)))

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (println "[component] " component "stopped"))
  (shutdown-agents))

(defn start-app []
  (println "[app] starting")
  (doseq [component (:started (mount/start))]
    (println "[component]" component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main []
  (start-app))