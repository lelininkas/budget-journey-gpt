# BudgetJourney - Trying out Vaadin, YugabyteDB and OpenAI GPT
Simple scalable WEB app for travel destination suggestions provided by OpenAPIService.
This was created following [Creating Scalable OpenAI GPT Applications in Java](https://dzone.com/articles/creating-scalable-openai-gpt-applications-in-java) write-up and referencing [BudgetJourney](https://github.com/YugabyteDB-Samples/budget-journey-gpt) Git repository.

## Original README
Imagine you want to visit a city and have a specific budget in mind. BudgetJourney is an app designed to suggest multiple points of interest within the city, tailored to fit your budget constraints.

This Java-based microservice application leverages the OpenAI GPT API to generate recommendations for points of interest. To optimize costs and reduce the volume of requests to the GPT API, all previous suggestions are stored in a YugabyteDB database. Alternatively, PostgreSQL can be used if required.

### Prerequisite
- Java 19: https://sdkman.io
- OpenAI API key: https://platform.openai.com/overview
- YugabyteDB Managed cluster (or another type of YugabyteDB/PostgreSQL deployment): https://cloud.yugabyte.com

### Configuration
Open the application.properties file and provide the following configuration settings:

OpenAI API key:
```
openai.key={YOUR_API_KEY}
```
Database connectivity settings:
```
spring.datasource.url = {DATABASE_JDBC_URL}
spring.datasource.username = {DATABASE_USERNAME}
spring.datasource.password = {DATABASE_PASSWORD}
```

### Usage
Start the app from a terminal:

```
mvn spring-boot:run
```
The application should automatically open a browser window on the following address: http://localhost:8080

To experience what BudgetJourney has to offer, simply provide a city name and budget limit. Please note that it may take 30 seconds or more for the OpenAI GPT to generate suggestions. However, to enhance efficiency, all previous suggestions are stored in the YugabyteDB database and will be served from there whenever you inquire about the same city and budget again.


