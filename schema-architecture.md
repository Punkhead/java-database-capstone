# Smart Clinic Management System - Architecture

## Section 1: Architecture Summary

This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve the appointment and patient-related modules. The application interacts with two databases: MySQL stores structured data such as patients, doctors, appointments, and admins, while MongoDB stores flexible prescription documents.

Controllers route requests through a common service layer, which contains the business logic and validation rules. The service layer delegates data access to the appropriate repositories. MySQL data is represented using JPA entities, while MongoDB data is represented using document models.

## Section 2: Numbered Flow of Data and Control

1. The user accesses an Admin or Doctor dashboard, or interacts with an appointment or patient module.
2. The request is routed to the appropriate Thymeleaf MVC controller or REST controller.
3. The controller passes the request to the service layer.
4. The service layer applies business rules, validations, and required workflows.
5. The service layer calls the appropriate repository to access the required data.
6. The repository communicates with MySQL using Spring Data JPA or with MongoDB using Spring Data MongoDB.
7. The retrieved or updated data flows back through the repository and service layers to the controller, which returns an HTML view for MVC requests or JSON data for REST requests.
