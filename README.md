# EMS – junior developer interview exercise

## Task II overview
The goal is to adapt the existing system by expanding old and introducing new
functionalities.

## Technical requirements
The same tech stack should be used, but it's open for discussion if considered necessary.

## System requirements
Employee data stored in the application should now also include:

Employees also have their own work badges to enter and exit the work place buildings.
There are no two same badges and every employee has only one which can identify them.

## Non-functional
Solution should be designed so it can allow further grow in the future. It should be easy for
specialists to maintain and understand.

## Functional
Employee attendance tracking
Service should allow to process and gather clock-in and clock-out times.
In the company facilities there are two systems for this already:
- Place of birth (city only)
- Date of birth
- Gender
- Address
- Contact information (since the first iteration of the project where email was mandatory,
employees can now also share their phone number. At least one is required now.)
- Bank account number
- PESEL or NIP
- Date of employment

More technical documentation about those can be found in the separate document (provided below).

## Improved searching
Searching for employees should now include all the existing and new options. Explanation:

Do not include time clock searches.
Anything that requires more explanation is located in the separate document attached.

## Data import and export
Service should be able to import and export data out of the system easily.
That includes all business information about employees and their time clocks.
Due to the fact that there is not much data yet, exporting can be implemented by extracting
everything from the system into single .csv file.

## Time constraint
No deadline.

## Employee attendance tracking
System A
Clocks sends the data in JSON format exactly* like this: 

{ <br>

"badgeNumber": "12345", <br>
"location": "Factory hall #01", <br>
"device": "RFID card reader #0123" <br>

}

* the values are not real.

System B
Clocks sends the data in JSON format exactly* like this: <br>

{ <br>

"badgeNumber": "12345", <br>
"action": "clock-in", <br>
"timestamp": "2024-01-18T14:21:51Z", <br>
"location": "Warehouse entrance #02", <br>
"device": "RFID card reader #789" <br>

}

* the values are not real.

Overview
action can be "clock-in" or "clock-out".
System A doesn't send action type, which implies that external service has to decide what it
was.

Searching
Below are some examples and further explanation of what's required in specific search
queries.

By name <br>

{ <br>

"badgeNumber": "12345", <br>
"location": "Factory hall #01", <br>
"device": "RFID card reader #0123" <br>

} <br>

{ <br>

"badgeNumber": "12345", <br>
"action": "clock-in", <br>
"timestamp": "2024-01-18T14:21:51Z", <br>
"location": "Warehouse entrance #02", <br>
"device": "RFID card reader #789" <br>

} <br>

Considering simplified example and an employee table like this:
First name Last name
Jimmy Smith
David Bowie
Simon Brown
John Davidson
Sarah Davis
Laura Thomas
Input Davi should return such list of employees:

[ <br>

"David Bowie", <br>
"John Davidson", <br>
"Sarah Davis" <br>
]

By date
Exclusive range - searching by from: 2023-10-29 and to: 2023-11-13 should include
given dates in the search.

By address
Should allow to search by various combinations of address components, such as postal
code and country, or street and postal code. It should not be mandatory to input all address
details. Instead, system should allow to provide one component, but not restrict to combine
multiple components, too, for more precise search results.


-----------------------------------------------------------------------------------------------------------------------------------------------------------
## Task I overview
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

