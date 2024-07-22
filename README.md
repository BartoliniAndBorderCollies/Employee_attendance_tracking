# EMS – junior developer interview exercise

## Task overview
Goal of this project is to create a backend service to manage employee details including:
adding, updating and deleting employee records.
The task can be continued with new features as a new project if this one is implemented
correctly.
The project is actually split into the two parts.
First: Propose a solution according to the requirements below.
Second: Implement the solution according to the proposition from the first step.

## Technical requirements

- Java 11+
- Spring Boot 2.5+
- Maven
- Relational database (i.e. MySQL)

## System requirements

### Non-functional
Scalability - system should be scalable to accommodate future growth

### Functional

- Employee management - to add, update, delete and view employee details
- Searching - to search employees by name, salary etc.

### Architecture
- REST with clear controller, service, repository and model layer

### Database design
Employee table:
- id (Primary Key)
- first_name
- last_name'
- email
- department
- salary

## Time constraint
One full week

## Evaluation criteria
Sorted by the most important first.
- Was the deadline met?
- Completeness - does the system include all required features?
- Correctness - are the features implemented correctly and do the work as expected?
- Testing - are there comprehensive unit and integration tests?
- Error handling - how well does the system handle exceptions and errors?
- Modularity - are the components of the system modular and reusable?
- Code quality - is the code well-structured, commented, follows industry standards and
  best practices?
- Code scalability - is it easy to add future features especially by different developers?
(key point for continuation of the project later)
- Documentation - is there sufficient documentation for developers?
- Data import/export - can data be easily imported into and exported from the system?

  ## My proposal and schema

  Basic structure of app:
  
![image](https://github.com/BartoliniAndBorderCollies/EMS_junior_developer_interview/assets/126821059/a18ca0b4-59d4-44ba-908a-e1e73525792d)


  ![image](https://github.com/BartoliniAndBorderCollies/EMS_junior_developer_interview/assets/126821059/70b1e6a9-ecd3-4a55-b033-5e519359522c)


![image](https://github.com/BartoliniAndBorderCollies/EMS_junior_developer_interview/assets/126821059/c22d3866-cbc0-40e8-9dd7-ff584cb13066)

