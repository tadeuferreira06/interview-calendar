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

## Summary

This is a basic API to schedule meetings between Candidates and Interviewers.

To start, create the necessary Candidates and Interviewers. For each one, add schedules (Day and Hour of availability).
Query, for meetings and book them.

## How to Use

### Swagger-UI

Open the following link to access the Swagger-UI:

```
http://localhost:8080/swagger-ui/index.html
```

### Query Meeting

The meeting is done based on the candidate's schedules and the service will match them with available interviewers. You
must provide a ``candidateId`` and a list of ``interviewerId``

To query a meeting use one of the following curls:

```
curl 'localhost:8080/meetings?candidateId=3&interviewerId=1%2C2'
```

```
curl 'localhost:8080/meetings?candidateId=3&interviewerId=1&interviewerId=2'
```

### Person Operations

Candidates and Interviewers supports all the same operations, The following examples will use the ``interviewers`` as an
example.

The available request paths are:

* {hostname}:{port}/candidates
* {hostname}:{port}/interviewer

#### Create

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
    "status": 201,
    "message": "Ok",
    "response": {
        "firstName": "Dinis",
        "lastName": "Antunes",
        "email": "da@mail.com",
        "phoneNumber": "+351-912-345-678",
        "id": 4,
        "personType": "INTERVIEWER"
    }
}
```

#### Read - List

To list the available people, use the following curl:

Use ``page`` and ``size`` query parameters to customize the search.

Request:

```
curl 'localhost:8080/interviewers'
curl 'localhost:8080/interviewers?page=0&size=2'
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": [
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
}
```

#### Read - Get

Request:

```
curl 'localhost:8080/interviewers/1'
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "firstName": "Pedro",
        "lastName": "Vareta",
        "email": "pv@mail.com",
        "phoneNumber": "+351-910-000-000",
        "id": 1,
        "personType": "INTERVIEWER"
    }
}
```

#### Update

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
    "status": 200,
    "message": "Ok",
    "response": {
        "firstName": "Dinis A.",
        "lastName": "Antunes",
        "email": "dinisaantunes@mail.com",
        "phoneNumber": "+351-962-345-678",
        "id": 4,
        "personType": "INTERVIEWER"
    }
}
```

#### Delete

To update a person, use the following curl:

Request:

```
curl -X 'DELETE' 'localhost:8080/interviewers/4' 
```

It will respond with the deleted object

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "firstName": "Dinis A.",
        "lastName": "Antunes",
        "email": "dinisaantunes@mail.com",
        "phoneNumber": "+351-962-345-678",
        "id": 4,
        "personType": "INTERVIEWER"
    }
}
```

### Schedules Operations

Candidates and Interviewers supports all the same operations, The following examples will use the ``interviewers`` as an
example.

The available request paths are:

* {hostname}:{port}/candidates/{id}/schedules
* {hostname}:{port}/interviewer/{id}/schedules

#### Create

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
    "status": 201,
    "message": "Ok",
    "response": {
	    "day": "2023-09-15",
	    "hour": 19,
	    "id": 8
	}
}

```

#### Read

To list the available schedules of a given person, use the following curl:

Request:

```
curl 'localhost:8080/interviewers/2/schedules'
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": [
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
]
```

#### Read - Get

Request:

```
curl 'localhost:8080/interviewers/2/schedules/3'
```

Response:

```

{
    "status": 200,
    "message": "Ok",
    "response": {
            "day": "2023-05-15",
            "hour": 12,
            "id": 3
    }
}

```

#### Update

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
    "status": 200,
    "message": "Ok",
    "response": {
        "day": "2023-12-15",
        "hour": 12,
        "id": 1
    }
}
```

#### Delete

To update a person, use the following curl:

Request:

```
curl -X 'DELETE' 'localhost:8080/interviewers/1/schedules/1' 
```

It will respond with the deleted object

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "day": "2023-12-15",
        "hour": 12,
        "id": 1
    }
}
```

### Meetings Operations

Candidates and Interviewers supports all the same operations, The following examples will use the ``interviewers`` as an
example.

The available request paths are:

* {hostname}:{port}/candidates/{id}/schedules
* {hostname}:{port}/interviewer/{id}/schedules

#### Query

To query for a meeting, you must provide a CandidateId. InterviewerId are optional and work as filter.

Request:

```
localhost:8080/meetings?candidateId=3
```

```
localhost:8080/meetings?candidateId=3&interviewerId=1,2
```

Response:

```
[
    {
    "status": 200,
    "message": "Ok",
    "response": [
        {
            "candidateSchedule": {
                "day": "2023-05-15",
                "hour": 12,
                "id": 5,
                "booked": false
            },
            "availableInterviewerList": [
                {
                    "firstName": "Pedro",
                    "lastName": "Vareta",
                    "email": "pv@mail.com",
                    "phoneNumber": "+351-910-000-000",
                    "id": 1,
                    "personType": "INTERVIEWER"
                }
            ]
        },
        {
            "candidateSchedule": {
                "day": "2023-05-17",
                "hour": 11,
                "id": 6,
                "booked": false
            },
            "availableInterviewerList": [
                {
                    "firstName": "Alberto",
                    "lastName": "Mendes",
                    "email": "am@mail.com",
                    "phoneNumber": "+351-910-000-001",
                    "id": 2,
                    "personType": "INTERVIEWER"
                }
            ]
        },
        {
            "candidateSchedule": {
                "day": "2023-05-19",
                "hour": 18,
                "id": 7,
                "booked": false
            },
            "availableInterviewerList": [
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
        }
    ]
}
]
```

#### Book

To book a meeting, first query for them and then provide:

* scheduleId
* candidateId
* list of available interviewers

Request:

```
curl -X 'POST' 'localhost:8080/meetings/book/7?candidateId=3&interviewerId=1,2' -d ''
```

or

```
curl -X 'POST' 'localhost:8080/meetings/book/7?candidateId=3&interviewerId=1&interviewerId=2' -d ''
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "id": 1,
        "scheduleDTO": {
            "day": "2023-05-19",
            "hour": 18,
            "id": 7,
            "booked": true
        },
        "candidate": {
            "firstName": "Tadeu",
            "lastName": "Ferreira",
            "email": "tf@mail.com",
            "phoneNumber": "+351-910-000-002",
            "id": 3,
            "personType": "CANDIDATE"
        },
        "interviewerList": [
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
    }
}
```

#### Read - Get

Request:

```
curl 'localhost:8080/meetings/1'
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "id": 1,
        "candidateSchedule": {
            "day": "2023-05-19",
            "hour": 18,
            "id": 7,
            "booked": true
        },
        "candidate": {
            "firstName": "Tadeu",
            "lastName": "Ferreira",
            "email": "tf@mail.com",
            "phoneNumber": "+351-910-000-002",
            "id": 3,
            "personType": "CANDIDATE"
        },
        "interviewerList": [
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
    }
}
```

#### Cancel

Request:

```
curl -X 'DELETE' 'localhost:8080/meetings/1' 
```

It will respond with the deleted object

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": {
        "id": 1,
        "candidateSchedule": {
            "day": "2023-05-19",
            "hour": 18,
            "id": 7,
            "booked": false
        },
        "candidate": {
            "firstName": "Tadeu",
            "lastName": "Ferreira",
            "email": "tf@mail.com",
            "phoneNumber": "+351-910-000-002",
            "id": 3,
            "personType": "CANDIDATE"
        },
        "interviewerList": [
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
    }
}
```

#### Read - Get Person Meeting

Request:

```
curl 'localhost:8080/interviewers/1/meetings'
```
```
curl 'localhost:8080/candidates/3/meetings'
```

Response:

```
{
    "status": 200,
    "message": "Ok",
    "response": [
        {
            "id": 1,
            "candidateSchedule": {
                "day": "2023-05-19",
                "hour": 18,
                "id": 7,
                "booked": true
            },
            "candidate": {
                "firstName": "Tadeu",
                "lastName": "Ferreira",
                "email": "tf@mail.com",
                "phoneNumber": "+351-910-000-002",
                "id": 3,
                "personType": "CANDIDATE"
            },
            "interviewerList": [
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
        }
    ]
}
```