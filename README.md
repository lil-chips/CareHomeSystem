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
Authorized operations: Managers have the highest level of access. Manage all the staff, residents, shifts, beds 
and view system logs but can't add/update/delete prescription, can't administer medicines.

Nurse -> ID: nurse1, Password: 1111
Authorized operations: Can prescribe, update/delete prescription, administer medicines and check resident details.

Doctor -> ID: doctor1, Password: 1111
Authorized operations: Can move residents, check resident details and administer medicines.

## Functions
1. Add Staff -> Add new doctors or nurses to the system.
2. Add Resident -> Register new residents and assign them to beds.
3. Add Prescription -> Write new prescriptions for residents.
4. Update Prescription -> Modify medicine name, dosage and time.
5. Delete Prescription -> Delete old prescriptions.
6. Add Bed -> Add new beds in the nursing home.
7. Assign Shift -> Assign working shifts to doctors and nurses.
8. Modify Password -> Reset a staff's login password.
9. View Staff List -> Check all staff and their details.
10. View Logs -> View all the system activity logs (actions, time, staff IDs).
11. Administer Medicine -> Record when a patient has taken the prescribed medicine.
12. View Residents -> View resident details and their prescriptions.
13. Move Residents -> Move one resident to another bed.

## Logs
All the actions happens in the system will be recorded with timestamp, staff ID and description.
This can help us to track all the activities.

