(defproject rinha "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :main rinha.core
  :dependencies [[compojure "1.6.1"]
                 [conman "0.9.6"]
                 [mount "0.1.17"] 
                 [org.clojure/clojure "1.11.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.6.0"] 
                 [ring "1.9.0"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler rinha.handler/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}
             :uberjar {:aot :all
                       :uberjar-name "rinha.jar"}})
