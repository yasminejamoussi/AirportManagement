✈️ Airport Management - Module Réclamation

📖 Description

Le module Réclamation est une composante clé du système Airport Management, une application basée sur une architecture de microservices conçue pour gérer les opérations aéroportuaires. Ce module permet aux passagers de soumettre, suivre et gérer leurs réclamations liées aux services aéroportuaires (par exemple, bagages perdus, problèmes de vol). Développé avec Spring Boot, il utilise une base de données H2 en mémoire pour simplifier le développement et les tests.

✨ Fonctionnalités Principales

📝 Soumission de réclamations : Créez des réclamations avec des détails précis (description, catégorie, etc.).

🖼️ Upload d'images : Joignez des images pour documenter les réclamations (ex. : photos de bagages endommagés).

🔍 Tri et filtrage : Triez les réclamations par critères (date, statut, catégorie) et appliquez des filtres avancés.

🔎 Multirecherche : Effectuez des recherches dynamiques basées sur plusieurs critères (numéro, passager, date).

📧 Mailing : Envoyez des notifications par e-mail aux passagers pour les mises à jour (ex. : changement de statut).

⚙️ Gestion des réclamations : Consultez, mettez à jour et résolvez les réclamations via une interface pour les agents.



🏗️ Architecture

Le module Réclamation s'intègre dans l'architecture modulaire d'Airport Management, basée sur Spring Boot et Spring Cloud. Il communique avec d'autres services (Passager, API Gateway) via OpenFeign et utilise Eureka pour la découverte des services.

🛠️ Technologies Utilisées

Backend : Spring Boot 3.4.3, Spring Cloud 2024.0.3

Base de données : H2 (en mémoire pour développement/test)

Communication : OpenFeign pour les appels inter-services

Mailing : Spring Boot Starter Mail pour l'envoi d'e-mails

Upload d'images : Gestion des fichiers via Spring MultipartFile

Logs : SLF4J avec Logback

Java : Version 17

Build : Maven 3.6.0+

Tracing : Zipkin avec Brave (optionnel)

Conteneurisation : Docker Compose


📋 Prérequis

Java : Version 17

Maven : Version 3.6.0 ou supérieure

Git : Pour cloner le repository

Docker : (Optionnel) Pour exécuter avec Docker Compose

SMTP Server : Un serveur SMTP (ex. : Gmail) pour les e-mails

Zipkin : (Optionnel) Instance pour le tracing (http://localhost:9411)


🚀 Installation et Exécution

1. Clonage du projet

git clone https://github.com/yasminejamoussi/AirportManagement.git

cd AirportManagement/Backend/services/Reclamation


2. Compilation et exécution
mvn clean install
mvn spring-boot:run

3. Exécution avec Docker Compose
Ajoutez le service Réclamation dans docker-compose.yml :
services:
  reclamation:
    image: reclamation-service:latest
    ports:
      - "8083:8083"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - eureka-server
      - config-server

Exécutez :
docker-compose up -d


🔒 Sécurité

Secrets : Utilisez des variables d'environnement ou Spring Cloud Config pour les clés sensibles (SMTP, API keys).
GitHub Push Protection : Scannez les commits avec truffleHog pour éviter les fuites de secrets.
Fichiers sensibles : Ajoutez application.yml à .gitignore si nécessaire.


