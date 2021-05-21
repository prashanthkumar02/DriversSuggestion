# Drivers Suggestion

## Description
A project used to back feed the store's and driver's data into database, also returns the N number of the closest drivers to the given store details.

## Follow these steps to run the application locally
 - Open a command line prompt on the package root folder and follow the below steps.
 - The following will create a package (JAR file) with a target folder in root directory, this JAR is used further to run the Microservices.
```
    mvn clean package 
```
 - Once the package is created use below command to run all the required containers - Zookeeper, Kafka, MySQL, Spring boot application. 
```
    docker-compose up
```

## Download links 
 - [JDK_1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
 - [Maven](https://maven.apache.org/download.cgi) 
 - [Docker compose](https://docs.docker.com/compose/install/)