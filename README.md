
## 📦 Microservice Passager (MongoDB)

### Description
Ce microservice gère les données des **passagers** dans l'aéroport. Il permet la création, la mise à jour, la suppression et la consultation des informations liées aux passagers.

Ce service utilise **MongoDB** pour le stockage des données, et communique avec les autres microservices via **OpenFeign**.

### Technologies
- Spring Boot 3.4.3
- MongoDB
- Spring Data MongoDB
- Spring Cloud Config
- Eureka Client
- OpenFeign
- Lombok
- Java 17

### Fonctionnalités avancées
- SMS
- Tri
- Recherche

### Configuration
```properties
spring.application.name=passager-service
server.port=8081

spring.data.mongodb.uri=mongodb://localhost:27017/passagerdb

eureka.client.service-url.defaultZone=http://localhost:8761/eureka
spring.config.import=optional:configserver:http://localhost:8888
```

### Exécution
```bash
mvn clean install
mvn spring-boot:run
```
