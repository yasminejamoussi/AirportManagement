Airport Management - Module Réclamation
Description
Le module Réclamation fait partie du système Airport Management, une application basée sur une architecture de microservices pour gérer les opérations aéroportuaires. Ce module permet aux passagers de soumettre, suivre et gérer leurs réclamations liées aux services aéroportuaires (par exemple, bagages perdus, problèmes de vol, etc.). Il est construit avec Spring Boot et utilise une base de données H2 en mémoire pour un développement et des tests simplifiés.

Fonctionnalités principales
Soumission de réclamations : Les passagers peuvent créer des réclamations avec des détails (description, catégorie, etc.).
Upload d'images : Possibilité de joindre des images pour documenter les réclamations (par exemple, photos de bagages endommagés).
Tri et filtrage : Tri des réclamations par divers critères (date, statut, catégorie) et filtrage avancé.
Multirecherche : Recherche dynamique basée sur plusieurs critères (par exemple, numéro de réclamation, nom du passager, date).
Mailing : Envoi de notifications par e-mail aux passagers pour les mises à jour de leurs réclamations (par exemple, changement de statut).
Gestion des réclamations : Consultation, mise à jour et résolution des réclamations par les agents aéroportuaires.
Architecture
Le module Réclamation s'intègre dans l'architecture globale d'Airport Management, qui repose sur Spring Boot et Spring Cloud. Il communique avec d'autres services (comme Passager ou API Gateway) via OpenFeign et utilise Eureka pour la découverte des services.

Technologies Utilisées
Backend : Spring Boot 3.4.3, Spring Cloud 2024.0.3
Base de données : H2 (base de données en mémoire pour le développement/test)
Communication : OpenFeign pour les appels inter-services
Mailing : Spring Boot Starter Mail pour l'envoi d'e-mails
Upload d'images : Gestion des fichiers via Spring MultipartFile
Logs : SLF4J avec Logback
Java : Version 17
Build : Maven 3.6.0 ou supérieur
Tracing : Zipkin avec Brave (optionnel)
Docker : Support pour l'exécution via Docker Compose
Prérequis
Java : Version 17
Maven : Version 3.6.0 ou supérieure
Git : Pour cloner le repository
Docker : (Optionnel) Pour exécuter avec Docker Compose
SMTP Server : Un serveur SMTP (par exemple, Gmail) pour les fonctionnalités de mailing
Zipkin : (Optionnel) Instance pour le tracing (ex: http://localhost:9411)
Installation et Exécution
1. Clonage du projet
bash

Copy
git clone https://github.com/yasminejamoussi/AirportManagement.git
cd AirportManagement/Backend/services/Reclamation
2. Configuration
Base de données H2 : Aucune configuration externe n'est requise, car H2 est une base de données en mémoire. La configuration est définie dans src/main/resources/application.yml :
yaml

Copy
spring:
  datasource:
    url: jdbc:h2:mem:reclamationdb
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
Mailing : Configurez les paramètres SMTP dans application.yml :
yaml

Copy
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: votre-email@gmail.com
    password: votre-mot-de-passe-app
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
Note : Pour Gmail, utilisez un mot de passe d'application (App Password) si l'authentification à deux facteurs est activée.
