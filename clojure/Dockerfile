FROM clojure:temurin-17-lein

WORKDIR /app

COPY src src
COPY resources resources
COPY project.clj .

RUN lein deps
RUN rm -dr target && lein uberjar && chmod +x target/rinha.jar

CMD [ "java", "-jar", "target/rinha.jar" ]