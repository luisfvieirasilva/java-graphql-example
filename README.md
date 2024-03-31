# Example project - JAVA/Spring Boot/GraphQL/MySQL

## Running the project

### Prerequisites

- [Docker](https://docs.docker.com/desktop/?_gl=1*ah4slm*_ga*MTYzNTIzOTQ5Mi4xNzAzMjkyOTU3*_ga_XJWPQMJYHQ*MTcwMzM2OTc2My4zLjEuMTcwMzM2OTc2OC41NS4wLjA.)
- [Docker Compose](https://docs.docker.com/compose/install/)

To run this project go to its root directory and them execute the following steps:

1. Start docker containers
    - `docker-compose up`
    - This command will start a MySQL server and Adminer. Go to http://localhost:8081 to access adminer. Credentials
      can be found at `docker-compose.yml` file
2. Start web server
    - `./gradlew run`

## Building the project

To build the project run `./gradlew build`. This will run tests and build the project. If you're interested in only
generating a WAR file you can run `./gradlew bootWar`. The generated WAR file will be located at `./build/libs`
directory.

## Development

### Running tests

To run tests first make sure you have docker containers running (or at least another MySQL server running). Then
run: ` ./gradlew check` to run both unit and integration tests.

You can also run `./gradlew test` to run only unit tests or `./gradlew integrationTest` to run only integration tests.

### Intellij IDEA

With you're using Intellij IDEA you can use the run configurations already set up at `./run` directory.

### Postman

You can import the postman collection `java_graphql_example.postman_collection.json` and run the requests in order
to manually test the API.