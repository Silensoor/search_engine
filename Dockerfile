FROM openjdk:17
LABEL maintainer="dev626@gmail.com"
ENV DB_HOST=db
COPY target/SearchEngine-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080