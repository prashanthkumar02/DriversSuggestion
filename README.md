# Drivers Suggestion

## Description
A project used to back feed the store's and driver's data into database, also returns the N number of the closest drivers to the given store details.

## Follow these steps to run the application locally
 - Open a command line prompt on the package root folder and follow the below steps.
 - The following will create a package (JAR file) with a target folder in root directory, this JAR is used further to run the Microservices.
```
    mvn clean package 
```
 - If the command giving an error by failing the test case use the following command (for more information about test failure go to Git wiki)
```
    mvn clean package -DskipTests
```
 - Once the package is created use below command to run all the required containers - Zookeeper, Kafka, MySQL, Spring boot application. 
```
    docker-compose up
```
 - Once all the service are started and running use this [link](http://localhost:9080/swagger-ui/#/) to use swagger of the application.
 - For further information use git wiki pages.

## Download links 
 - [JDK_1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
 - [Maven](https://maven.apache.org/download.cgi) 
 - [Docker compose](https://docs.docker.com/compose/install/)
