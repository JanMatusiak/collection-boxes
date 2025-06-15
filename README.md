# Collection-Boxes Service

A Spring Boot application to manage fundraising events and donation boxes.

## Prerequisites

- Java 17+ (written on Java 24)
- Git
- Maven (wrapper included)

## Getting Started

```bash
git clone https://github.com/JanMatusiak/collection-boxes
cd collection-boxes

### Using the Maven wrapper (included in the project)
./mvnw clean package
### Run via Spring Boot plugin
./mvnw spring-boot:run

### —or— run the packaged JAR
java -jar target/collection-boxes-0.1.0.jar
## 1. Create a new fundraising event
curl -i -X POST "http://localhost:8080/createEvent?name=CharityRun&currency=EUR"

## 2. Register a new empty box
curl -i -X POST "http://localhost:8080/registerBox"

## 3. List all boxes (shows only empty/assigned flags)
curl -i -X GET "http://localhost:8080/listBoxes"

## 4. Assign box #1 to event #1
curl -i -X PUT "http://localhost:8080/assignBox/1?eventName=CharityRun"

## 5. Add €50 to box #1
curl -i -X PUT "http://localhost:8080/addMoney/1?amount=50&currency=EUR"

## 6. Empty box #1 (transfer its funds into the event account)
curl -i -X PUT "http://localhost:8080/emptyBox/1"

## 7. Unregister box #1 (remove it from the database)
curl -i -X DELETE "http://localhost:8080/unregisterBox/1"

## 8. Display financial report
curl -i -X GET "http://localhost:8080/generateReport"
