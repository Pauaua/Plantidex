@echo off 
mvn clean package 
docker build -t ciisaaaa/ctrl-app:latest . 
