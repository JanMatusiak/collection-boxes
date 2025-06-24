# Collection-Boxes Service

A Spring Boot application to manage fundraising events and donation boxes.

## Prerequisites

- Java 17+ (written on Java 24)
- Git
- Maven (wrapper included)

## Getting Started

### Profiles

By default, the service uses built-in (constant) currency rates.
For real‐time conversions, a key from https://www.exchangerate-api.com/ must be generated.

```bash
git clone https://github.com/JanMatusiak/collection-boxes
cd collection-boxes

# constant (static) rates (default)
./mvnw spring-boot:run
# —or—
java -jar target/collection-boxes-0.1.0.jar

# live (external API) rates
export EXCHANGERATE_API_KEY=your_real_api_key_here
./mvnw spring-boot:run \
  -Dspring-boot.run.profiles=live-rates
# —or—
export EXCHANGERATE_API_KEY=your_real_api_key_here
java -jar target/collection-boxes-0.1.0.jar \
  --spring.profiles.active=live-rates

## 1. Create a new event
curl -i -X POST "http://localhost:8080/events?name=CharityRun&currency=EUR"

## 2. Register a new empty box
curl -i -X POST "http://localhost:8080/box"

## 3. List all boxes (shows only empty/assigned flags)
curl -i -X GET "http://localhost:8080/box"

## 4. Assign box #1 to event #1
curl -i -X PUT "http://localhost:8080/box/1/assign?eventName=CharityRun"

## 5. Add €50 to box #1
curl -i -X POST "http://localhost:8080/box/1/add?amount=50&currency=EUR"

## 6. Empty box #1 (transfer its funds into the event account)
curl -i -X PUT "http://localhost:8080/box/1/empty"

## 7. Unregister box #1 (remove it from the database)
curl -i -X DELETE "http://localhost:8080/box/1"

## 8. Display financial report
curl -i -X GET "http://localhost:8080/report"

```