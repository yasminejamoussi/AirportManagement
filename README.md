# Airport Management - Système de Gestion d'Aéroport

## Description
**Airport_Management** est une application basée sur une architecture de microservices conçue pour gérer diverses opérations aéroportuaires. Elle repose sur **Spring Boot** et **Spring Cloud** afin d'offrir une solution **modulaire, scalable et distribuée**.

L'application prend en charge :
- La gestion des **passagers**
- La gestion des **objets perdus**
- Le suivi des **livraisons de bagages**
- Le traitement des **réclamations**
- La gestion d’un **forum de discussion**
- L’administration des **agents aéroportuaires**

Une **API Gateway** centralise les requêtes et **Eureka** assure la découverte des services.

---

## Services Principaux
### Infrastructure
- **Discovery (Eureka Server)** : Service d’enregistrement et de localisation des microservices.
- **Config Server** : Gestion centralisée des configurations des microservices.
- **API Gateway** : Point d’entrée unique pour les requêtes, assurant routage et sécurité.

### Services Métier
- **Passager** : Gestion des informations des passagers (basé sur JPA et une base SQL).
- **ObjetPerdu** : Gestion des objets perdus dans l’aéroport (basé sur JPA et une base SQL).
- **LivraisonBagages** : Gestion des livraisons de bagages perdus (basé sur MongoDB).
- **Reclamation** : Gestion des réclamations des passagers.
- **Forum** : Plateforme de discussion pour les utilisateurs.
- **AgentAeroport** : Gestion des agents aéroportuaires.

---

## Technologies Utilisées
### Backend
- **Spring Boot** 3.4.3
- **Spring Cloud** 2024.0.3

### Bases de Données
- **MongoDB** : Pour le service **LivraisonBagages**
- **Base SQL** (MySQL, PostgreSQL, etc.) : Pour les services **Passager, ObjetPerdu, Reclamation**, etc.

### Communication & Architecture
- **OpenFeign** : Communication entre microservices
- **Netflix Eureka** : Découverte des services
- **Spring Cloud Config** : Gestion centralisée de la configuration
- **Spring Cloud Gateway** : API Gateway pour le routage et la sécurité
- **Zipkin avec Brave** : Tracing des requêtes
- **SLF4J avec Logback** : Gestion des logs
- **Java 17** : Version utilisée
- **Docker Compose** : Orchestration des services

---

## Prérequis
- **Java** : Version 17
- **Maven** : Version 3.6.0 ou supérieure
- **MongoDB** : Instance locale ou distante (ex: `mongodb://localhost:27017`)
- **Base SQL** : MySQL, PostgreSQL ou autre (ex: `jdbc:mysql://localhost:3306/airportdb`)
- **Git** : Pour cloner le repository (si utilisé avec Config Server)
- **Docker** : Pour exécuter les services avec Docker Compose
- **Zipkin** *(optionnel)* : Instance pour le tracing (ex: `http://localhost:9411`)

---

## Installation et Exécution
### Clonage du projet
```bash
git clone https://github.com/votre-repository/airport_management.git
cd airport_management
```

### Compilation et Exécution des Services
```bash
mvn clean install
mvn spring-boot:run
```

### Exécution avec Docker Compose
```bash
docker-compose up -d
```
