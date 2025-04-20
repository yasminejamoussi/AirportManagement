
## 💬 Microservice Forum (MySQL)

### Description
Ce microservice propose un **forum de discussion** permettant aux utilisateurs de publier et consulter des sujets liés à l’aéroport.

### Technologies
- Spring Boot 3.4.3
- MySQL
- Spring Data JPA
- Eureka Client
- Spring Cloud Config
- Java 17

### Fonctionnalités avancées
- Notifications
- Tri
- Recherche

### Configuration
```properties
spring.application.name=forum-service
spring.datasource.url=jdbc:mysql://localhost:3306/forumdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Exécution
```bash
mvn spring-boot:run
```
