
## 🎒 Microservice Objet Perdu (MySQL)

### Description
Ce microservice gère la déclaration et la gestion des **objets perdus** dans l'aéroport. Il utilise **MySQL** pour le stockage des données.

### Technologies
- Spring Boot 3.4.3
- MySQL
- Spring Data JPA
- Eureka Client
- Spring Cloud Config
- Java 17

### Fonctionnalités avancées
- Statistiques
- Tri
- Recherche

### Configuration
```properties
spring.application.name=objetperdu-service
spring.datasource.url=jdbc:mysql://localhost:3306/objetperdudb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Exécution
```bash
mvn spring-boot:run
```
