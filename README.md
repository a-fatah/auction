# auktion

This is an API that allows users to create, manage, and bid on offers while ensuring the integrity of the auction process through the use of password authentication. 

It's implemented in Kotlin using Spring Boot and uses Gradle as a build tool.

For storing data in memory it uses H2 database.

For working with amounts it uses Integer type and stores amounts in cents.

### API Documentation

GET /offers - returns all offers

GET /offers/{id} - returns offer with given id

GET /offers/{id}/bids - returns all bids for offer with given id

POST /offers - creates new offer

PUT /offers/{id} - updates offer with given id

DELETE /offers/{id} - deletes offer with given id

POST /offers/{id}/bids - creates new bid for offer with given id

PUT /offers/{id}/close - closes offer with given id

### Authentication

To be implemented

### Running the application


To run the application, use the following command:

`./gradlew bootRun`

### Running the tests

To run the tests, use the following command:

`./gradlew test`

### Building the application

To build the application, use the following command:

`./gradlew build`

To build the application without running the tests, use the following command:

`./gradlew build -x test`

### Building the Docker image

To build the Docker image, use the following command:

`./gradlew jibDockerBuild`

The image will be available in the local Docker registry and then you can run it using the following command:

`docker run -p 8080:8080 auktion:0.0.1-SNAPSHOT`
