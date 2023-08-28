(ns rinha.components.env
  (:require [mount.core :refer [defstate]]
            [clojure.string :as str])
  (:gen-class))

(defn transform-key [k]
  (-> (str/replace k #"_" "-")
      str/lower-case
      keyword))

(defstate env
  :start (do
           (println "[env] loading env")
           (-> (System/getenv)
               (update-keys transform-key)))
  :stop {})

(mount.core/start #'rinha.components.env/env)