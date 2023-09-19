# interview-calendar-api

## Database Access

Use the following url to access the database console:

```
http://localhost:9001/h2-console
```

Use the correct url and credentials that are in: ``application.properties``

## Run Application

### Requirements to Run

* Java 17
* Maven

### Build Project

```
mvn clean package
```

### Run Project

#### Using Maven

```
mvn spring-boot:run
```

#### Using Java Directly

```
java -jar target/interview-calendar-*.jar
```

## Summary

This is a basic API to schedule meetings between Candidates and Interviewers.

To start, create the necessary Candidates and Interviewers. For each one, add schedules (Day and Hour of availability).
Query, for meetings and book them.

## How to Use

### Basic Security

As POC, Basic Auth is setup. Please use the following credentials.

Access to Everything:
``admin/admin``

Access to Interviewers:
``interviewer/interviewer``

Access to Candidates:
``candidate/candidate``

### Swagger-UI

Open the following link to access the Swagger-UI:

```
http://localhost:8080/swagger-ui/index.html
```

### API Description

[API description](API.md)