FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/couchdb-test.jar /couchdb-test/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/couchdb-test/app.jar"]
