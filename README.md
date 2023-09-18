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
* Docker?

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
## How to Use - Swagger UI

Open the following link to access the Swagger-UI:

```
http://localhost:8080/swagger-ui/index.html
```

## How to Use - Person CRUD
Candidates and Interviewers supports all the same operations, The following examples will use the ``interviewers`` as an example. 

The available request paths are:
* {hostname}:{port}/candidates
* {hostname}:{port}/interviewer

### Create
To create a new person, use the following curl:

Request:
```
curl -X 'POST' 'localhost:8080/interviewers' \
-H 'Content-Type: application/json' \
-d '{
    "firstName":"Dinis",
    "lastName":"Antunes",
    "email":"da@mail.com",
    "phoneNumber":"912345678"
}'
```
Response:
```
{
    "firstName": "Dinis",
    "lastName": "Antunes",
    "email": "da@mail.com",
    "phoneNumber": "+351-912-345-678",
    "id": 4,
    "personType": "INTERVIEWER"
}
```

### Read - List
To list the available people, use the following curl:

Use ``page`` and ``size`` query parameters to customize the search.

Request:
```
curl 'localhost:8080/interviewers'
curl 'localhost:8080/interviewers?page=0&size=2'
```
Response:
```
[
    {
        "firstName": "Pedro",
        "lastName": "Vareta",
        "email": "pv@mail.com",
        "phoneNumber": "+351-910-000-000",
        "id": 1,
        "personType": "INTERVIEWER"
    },
    {
        "firstName": "Alberto",
        "lastName": "Mendes",
        "email": "am@mail.com",
        "phoneNumber": "+351-910-000-001",
        "id": 2,
        "personType": "INTERVIEWER"
    }
]
```

### Read - Get
Request:
```
curl 'localhost:8080/interviewers/1'
```
Response:
```
{
    "firstName": "Pedro",
    "lastName": "Vareta",
    "email": "pv@mail.com",
    "phoneNumber": "+351-910-000-000",
    "id": 1,
    "personType": "INTERVIEWER"
}
```

### Update
To update a person, use the following curl:

Request:
```
curl -X 'PUT' 'localhost:8080/interviewers/4' \
-H 'Content-Type: application/json' \
-d '{
    "firstName":"Dinis A.",
    "lastName":"Antunes",
    "email":"dinisaantunes@mail.com",
    "phoneNumber":"962345678"
}'
```
Response:
```
{
    "firstName": "Dinis A.",
    "lastName": "Antunes",
    "email": "dinisaantunes@mail.com",
    "phoneNumber": "+351-962-345-678",
    "id": 4,
    "personType": "INTERVIEWER"
}
```

### Delete
To update a person, use the following curl:

Request:
```
curl -X 'DELETE' 'localhost:8080/interviewers/4' 
```

It will respond with the deleted object

Response:
```
{
    "firstName": "Dinis A.",
    "lastName": "Antunes",
    "email": "dinisaantunes@mail.com",
    "phoneNumber": "+351-962-345-678",
    "id": 4,
    "personType": "INTERVIEWER"
}
```

## How to Use - Schedules CRUD
Candidates and Interviewers supports all the same operations, The following examples will use the ``interviewers`` as an example.

The available request paths are:
* {hostname}:{port}/candidates/{id}/schedules
* {hostname}:{port}/interviewer/{id}/schedules

### Create
To create a new person, use the following curl:

Request:
```
curl -X 'POST' 'localhost:8080/interviewers/1/schedules' \
-H 'Content-Type: application/json' \
-d '{
    "day":"2023-09-15",
    "hour":19
}'
```
Response:
```
{
	"day": "2023-09-15",
	"hour": 19,
	"id": 8
}

```

### Read
To list the available schedules of a given person, use the following curl:

Request:
```
curl 'localhost:8080/interviewers/2/schedules'
```
Response:
```
[
    {
        "day": "2023-05-15",
        "hour": 12,
        "id": 3
    },
    {
        "day": "2023-05-17",
        "hour": 18,
        "id": 4
    }
]
```
### Read - Get
Request:
```
curl 'localhost:8080/interviewers/2/schedules/3'
```
Response:
```
[
    {
        "day": "2023-05-15",
        "hour": 12,
        "id": 3
    },
    {
        "day": "2023-05-17",
        "hour": 18,
        "id": 4
    }
]
```
### Update
To update a person's schedule, use the following curl:

Request:
```
curl -X 'PUT' 'localhost:8080/interviewers/1/schedules/1' \
-H 'Content-Type: application/json' \
-d '{
    "day":"2023-12-15",
    "hour":12
}'
```
Response:
```
{
	"day": "2023-12-15",
	"hour": 12,
	"id": 1
}
```

### Delete
To update a person, use the following curl:

Request:
```
curl -X 'DELETE' 'localhost:8080/interviewers/1/schedules/1' 
```

It will respond with the deleted object

Response:
```
{
	"day": "2023-12-15",
	"hour": 12,
	"id": 1
}
```
