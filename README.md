# Drivers Suggestion

## Description
A project used to back feed the store's and driver's data into the database.A service that returns the N number of closest drivers for the given store details.

## Follow these steps to run the application locally
 - Open a command-line prompt on the package root folder and follow the below steps.
 - The following will create a package (JAR file) with a target folder in the root directory, this JAR is used further to run the Microservices.
```
    mvn clean package 
```
 - If the above command fails to build a package use the following command (Generally occurs because of failure in running test cases, for further information go to Git wiki)
```
    mvn clean package -DskipTests
```
 - Once the package is created, use the below command to run all the required containers - Zookeeper, Kafka, MySQL, Spring boot application. 
```
    docker-compose up
```
 - Once all the services are started and running use the following [link](http://localhost:9080/swagger-ui/#/) to use the swagger of the application.
 - For further information use git wiki pages.

## Download links 
 - [JDK_1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
 - [Maven](https://maven.apache.org/download.cgi) 
 - [Docker compose](https://docs.docker.com/compose/install/)
