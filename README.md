
## 📦 Microservice Livraison (MongoDB)

### Description
Ce microservice s'occupe de la **livraison des bagages perdus** aux passagers. Les données sont stockées dans une base **MongoDB**.

### Technologies
- Spring Boot 3.4.3
- MongoDB
- Spring Data MongoDB
- Eureka Client
- Spring Cloud Config
- OpenFeign
- Java 17

### Fonctionnalités avancées
- PDF
- Tri
- Recherche

### Configuration
```properties
spring.application.name=livraison-service
spring.data.mongodb.uri=mongodb://localhost:27017/livraisondb
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Exécution
```bash
mvn spring-boot:run
```
