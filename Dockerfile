FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/clojure-wiki.jar /clojure-wiki/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/clojure-wiki/app.jar"]
