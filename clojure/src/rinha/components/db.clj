(ns rinha.components.db
  (:require [conman.core :as conman]
            [mount.core :refer [defstate]]
            [next.jdbc.result-set :refer [ReadableColumn]]
            [next.jdbc.prepare :refer [SettableParameter]])
  (:gen-class))

;; Need to use env vars to work with production
(def pool-spec {:adapter "postgresql"
                :username "postgres"
                :password "postgres"
                :database-name "db_clj"
                :server-name "localhost"
                :port-number 5432})

(defstate ^:dynamic *db* 
  :start (conman/connect! pool-spec)
  :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

;; By default, when the result set includes pg arrays, they are instances of org.postgresql.jdbc.PgArray,
;; which are not the same as clojure's collections.
;; This extends next.jdbc types by implementing a manual conversion as follows:
;;    insertion: <whichever clojure collection> -> PgArray
;;    reading:   PgArray -> clojure vectors
(defn- coll->pg-array [coll ^java.sql.PreparedStatement stmt ^long i]
  (let [conn (.getConnection stmt)
        meta (.getParameterMetaData stmt)
        type-name (.getParameterTypeName meta i)]
    (if-let [elem-type (when (= (first type-name) \_) (apply str (rest type-name)))]
      (.setObject stmt i (.createArrayOf conn elem-type (to-array coll)))
      (.setObject stmt i coll))))

(extend-protocol SettableParameter
  clojure.lang.IPersistentCollection
  (set-parameter [v stmt i]
    (coll->pg-array v stmt i)))

(extend-protocol ReadableColumn
  java.sql.Array
  (read-column-by-label [val _]
    (into [] (.getArray val)))
  (read-column-by-index [val _ _]
    (into [] (.getArray val))))

