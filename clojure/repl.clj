(require '[mount.core :as mount])
(require '[rinha.components.app :refer [app]])
(require '[ring.mock.request :as mock])
(require '[clojure.pprint :refer [pprint]])
(require '[cheshire.core :refer [parse-string]])

(mount/start #'rinha.components.app/app)
(mount/start #'rinha.components.db/*db*)

(defmacro req!
  [& body]
  `(let [result# (app ~@body)]
     (if (clojure.string/includes? (get-in result# [:headers "Content-Type"]) "application/json")
       (pprint (assoc result# :body (parse-string (:body result#))))
       (pprint result#))))

(req! (mock/request :get "/pessoas/9"))
(req! (mock/request :get "/contagem-pessoas"))
(req! (-> (mock/request :post "/pessoas")
          (mock/json-body {:nickname "jack",
                           :name "Jackarias",
                           :birthdate "2002-01-04",
                           :stack ["Ruby" "Elixir"]})))
(req! (-> (mock/request :post "/pessoas")
          (mock/json-body {:nickname "sus",
                           :name "Susi",
                           :birthdate "2001-01-04",
                           :stack ["Ruby"]})))
(req! (-> (mock/request :post "/pessoas")
          (mock/json-body {:nickname "sus",
                           :name "Susi",
                           :birthdate "2001-01-04"
                           :stack ["C" 23]})))
(req! (mock/request :get "/pessoas/a3f1f7ac-0f69-4fa1-bb61-c6ee7cb7d250"))
(req! (mock/request :get "/pessoas?t=oi"))
(req! (mock/request :get "/pessoas?t="))
(req! (mock/request :get "/pessoas?")) 

;; ===============================================================

(require '[next.jdbc :as jdbc])
(require '[conman.core :as conman])

(conman/bind-connection rinha.components.db/*db* "sql/queries.sql")

(mount/start #'rinha.components.db/*db*)
(mount/stop)

(get-person-by-id {:id "1d301dbd-78bb-4594-84f3-5db30097e50d"})

(def user-data
  [{:nickname "jack",
    :name "Jackarias",
    :birthdate "2002-01-04",
    :stack ["Ruby" "Elixir"]}
   {:nickname "pitu",
    :name "Capitu",
    :birthdate "2010-02-29",
    :stack ["Clojure" "Ruby" "Elixir"]}
   {:nickname "josé",
    :name "José Roberto",
    :birthdate "1985-09-23",
    :stack nil}
   {:nickname "ana",
    :name "Ana Barbosa",
    :birthdate "2000-05-11",
    :stack ["Ruby" "C#" "Node"]}
   {:nickname "beto",
    :name "Carlos Roberto",
    :birthdate "2009-03-16",
    :stack ["C#"]}])

(create-person! {:nickname "john",
                 :name "John",
                 :birthdate "2012-03-16",
                 :stack ["Ruby" "C#"]})

(map create-person! user-data)

(-> (get-all-people *db*) clojure.pprint/pprint)
(count-all-people *db*)

(-> (search-people) clojure.pprint/pprint)

(require '[next.jdbc :as nbc])
(-> (nbc/execute! *db* ["DROP TABLE people"])
    (clojure.pprint/pprint))

;; ===============================================================