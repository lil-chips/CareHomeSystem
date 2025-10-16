# CareHomeSystem

## Overview
CareHome System is a simple management system for a nursing home. It allows managers, nurses and doctors to log in and 
perform different actions. These actions are:
1. Adding new staffs
2. Assigning shifts
3. Checking staff list
4. Adding new residents
5. Adding new beds
6. Checking residents details
7. Checking residents list
8. Moving residents
9. Adding prescription
10. Recording administered medicines
11. Deleting or updating prescription
12. Modifying staff password
13. Viewing system logs

## How to Run the program
### Requirements
1. Java 21+
2. Maven installed 
3. SQLite JDBC

### Run Command
-> mvn clean javafx:run
(After starting the system, it will automatically connect to the database and build the necessary tables.)

## Login Roles
The system provides three roles to login, after login you will see different function buttons for different role.

### For example:
Manager -> ID: manager1, Password: 0722
Authorized operations: Manage all the staff and residents, but can't add/update/delete prescription, 
can't administer medicines.

Nurse -> ID: nurse1, Password: 1111
Authorized operations: Can prescribe, update/delete prescription, administer medicines and check resident details.

Doctor -> ID: doctor1, Password: 1111
Authorized operations: Can move residents, check resident details and administer medicines.