#!/bin/bash
mvn -U clean package
docker run --name db -e POSTGRES_PASSWORD=198855 -e POSTGRES_USER=root -e POSTGRES_DB=search_engine -d -p 5434:5433 postgres:latest
docker build -t docker-java:searchengine .
docker run -d --name app --link db:db -p 8080:8080 docker-java:searchengine