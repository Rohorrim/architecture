# 🏦 Architecture Bancaire - Gestion de Clients et Comptes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Database-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Description

Application Spring Boot de gestion de clients et de comptes bancaires, développée dans le cadre d'un TP sur l'architecture logicielle à l'IMT. Ce projet démontre l'utilisation des bonnes pratiques d'architecture logicielle avec une séparation claire des responsabilités en couches.

### ✨ Fonctionnalités

- **Gestion des clients** : CRUD complet (Create, Read, Update, Delete)
- **Gestion des comptes bancaires** : Création et gestion de comptes par client
- **Types de comptes** : Compte Courant, Livret A, LDDS
- **Système d'agios automatique** : Application automatique d'agios en cas de solde négatif
- **Validation robuste** : Validation des données avec Jakarta Validation
- **Gestion d'événements** : Pattern Observer pour les événements métier

## 🏗️ Architecture

Le projet suit une architecture en couches (Layered Architecture) avec les principes SOLID :

```
┌─────────────────────────────────────────┐
│         Interface Layer (REST)          │
│  Controllers, DTOs, Exception Handlers  │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Business Layer                │
│   Services, Validators, Domain Models   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│       Infrastructure Layer              │
│   Database, Events, Mappers, Entities   │
└─────────────────────────────────────────┘
```

### 📁 Structure du projet

```
src/main/java/com/wgt/imt/architecture/
├── business/                    # Couche métier
│   ├── clients/                 # Domaine clients
│   │   ├── model/              # Modèles métier
│   │   ├── validators/         # Validateurs métier
│   │   ├── ClientsService.java
│   │   └── ClientsServiceValidator.java
│   ├── comptes/                # Domaine comptes
│   │   ├── model/
│   │   ├── validators/
│   │   ├── ComptesService.java
│   │   └── ComptesServiceValidator.java
│   └── common/                 # Éléments communs
│       ├── model/              # Enums, ValidatorResult
│       └── validators/         # Validateurs abstraits
├── common/                     # Utilitaires
│   └── utils/
├── infrastructures/            # Couche infrastructure
│   ├── bdd/                   # Accès base de données
│   │   └── clients/
│   │       ├── repositories/
│   │       └── ClientsBddService.java
│   └── events/                # Gestion d'événements
│       └── comptes/
└── interfaces/                # Couche interface
    ├── events/                # Event listeners
    └── rest/                  # API REST
        ├── clients/
        ├── comptes/
        └── common/
```

## 🚀 Technologies

- **Java 21** - Langage de programmation
- **Spring Boot 3.5.6** - Framework applicatif
- **Spring Data MongoDB** - Persistance des données
- **Spring Validation** - Validation des données
- **Lombok** - Réduction du code boilerplate
- **MongoDB** - Base de données NoSQL
- **Maven** - Gestion des dépendances

## 📦 Installation

### Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MongoDB 4.4+ (ou Docker)

### Lancement de MongoDB

```bash

```

### Configuration

Modifier le fichier `src/main/resources/application.properties` si nécessaire :

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/architecture
spring.data.mongodb.database=architecture

# Server Configuration
server.port=8080
```

### Compilation et lancement

```bash
# Compilation
mvn clean install

# Lancement de l'application
mvn spring-boot:run
```

L'application sera accessible sur : **http://localhost:8080**

## 🔌 API REST

### Endpoints Clients

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/imt/v1/clients` | Récupérer tous les clients |
| `GET` | `/api/imt/v1/clients/{id}` | Récupérer un client par ID |
| `POST` | `/api/imt/v1/clients` | Créer un nouveau client |
| `PATCH` | `/api/imt/v1/clients/{id}` | Mettre à jour un client |
| `DELETE` | `/api/imt/v1/clients/{id}` | Supprimer un client |

### Endpoints Comptes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/imt/v1/clients/{idClient}/comptes` | Récupérer tous les comptes d'un client |
| `GET` | `/api/imt/v1/clients/{idClient}/comptes/{id}` | Récupérer un compte spécifique |
| `POST` | `/api/imt/v1/clients/{idClient}/comptes` | Créer un nouveau compte |
| `PATCH` | `/api/imt/v1/clients/{idClient}/comptes/{id}` | Mettre à jour un compte |
| `DELETE` | `/api/imt/v1/clients/{idClient}/comptes/{id}` | Supprimer un compte |

### 📝 Exemples de requêtes

#### Créer un client

```bash
POST /api/imt/v1/clients
Content-Type: application/json

{
  "lastname": "Dupont",
  "firstname": "Jean",
  "genre": "HOMME"
}
```

#### Créer un compte

```bash
POST /api/imt/v1/clients/{clientId}/comptes
Content-Type: application/json

{
  "name": "Mon Compte Courant",
  "type": "COMPTE_COURANT",
  "solde": 1000.0
}
```

## 🎯 Patterns et Principes

### Patterns Utilisés

- **Chain of Responsibility** : Pour la validation en chaîne des données
- **Builder Pattern** : Construction des objets métier (via Lombok)
- **Repository Pattern** : Abstraction de l'accès aux données
- **Observer Pattern** : Système d'événements pour les comptes négatifs
- **Mapper Pattern** : Conversion entre entités et modèles métier
- **DTO Pattern** : Objets de transfert pour les API REST

### Principes SOLID

- **Single Responsibility** : Chaque classe a une responsabilité unique
- **Open/Closed** : Extension par héritage (validateurs, mappers)
- **Liskov Substitution** : Les validateurs sont interchangeables
- **Interface Segregation** : Interfaces ciblées et spécifiques
- **Dependency Inversion** : Injection de dépendances Spring

## 🔍 Fonctionnalités Avancées

### Système de Validation en Chaîne

Le projet utilise un système de validation sophistiqué basé sur le pattern Chain of Responsibility :

```java
new ConstraintValidatorStep<Client>()
    .linkWith(new ClientAlreadyExistValidatorStep(service))
    .linkWith(new ClientGenreValidatorStep())
    .validate(client)
    .throwIfInvalid();
```

### Gestion des Événements

Lorsqu'un compte passe en négatif, un événement est automatiquement publié et traité de manière asynchrone pour appliquer des agios :

```
Compte négatif détecté → NegativeCompteEvent → NegativeCompteEventListener → Application d'agios
```

### Gestion des Erreurs

Toutes les erreurs sont gérées de manière centralisée avec des codes HTTP appropriés :

- **400 Bad Request** : Erreurs de validation
- **404 Not Found** : Ressource non trouvée
- **409 Conflict** : Conflit (client/compte déjà existant)
- **500 Internal Server Error** : Erreurs serveur

## 🧪 Tests

```bash
# Lancer les tests unitaires
mvn test

# Lancer les tests avec rapport de couverture
mvn clean test jacoco:report
```

## 📚 Documentation

La Javadoc complète est disponible dans le code source. Pour générer la documentation HTML :

```bash
mvn javadoc:javadoc
```

La documentation sera générée dans `target/site/apidocs/`

## 🤝 Contribution

Ce projet est un TP académique. Pour toute question ou suggestion :

1. Créez une issue
2. Proposez une Pull Request
3. Contactez l'équipe pédagogique IMT

## 📄 Licence

Ce projet est développé dans un cadre pédagogique à l'IMT.

## 👥 Auteurs

- **Emmanuel WAGUET** - Travaux Pratiques Architecture Logicielle

## Exercices 
Pour chaque exercice, il y a un commit associé dans le repo Git

### 1 - Création des ressources Clients et Comptes
    Le but est d'initialiser un projet (idéalement Spring), et d'exposer pour la première fois deux ressources distinctes :
    -	Clients : Le client est composé, a minima, des attributs suivants : identifiant, nom, prenom
    -	Comptes : Le compte est composé, a minima, des attributs suivants : identifiant, nom, type, solde
            
    Vous pouvez bouchonner le code métier, ce qu'on l'on souhaite pour l'instant c'est retourner les bons codes HTTP, utiliser les bons verbes, ...

### 2 - Création des controller métier
    Le but est de voir comment on passe des infos au controller métier (via les mapper) et de
    commencer a mettre en oeuvre une architecture en couche

    Créer un controller métier pour les clients et un autre pour les comptes, avec les 
    opérations suivantes :
        -	Listing
        -   Récupération d'une entrée unique
        -   Sauvegarde d'une entrée
        -   Suppression d'une entrée

    N'ayant pas encore de BDD, on stockera les objets dans des List ou des Map, comme vous
    préférez, en attribut du controller

### 3 - Mise en oeuvre d'une BDD
    Le but est de voir créer la troisième couche de notre architecture : la couche infrastructure. 
    On va donc mettre en oeuvre une BDD H2 ou une mongoBD, et utiliser Spring Data JPA pour faire le lien entre la BDD et notre couche métier.

    Il faut impacter notre code métier afin d'utiliser la BDD en question

    PS : Pour lancer la mongo : mongo.exe --dbpath /c/Users/A388771/local/IMT/architecture/mongo

### 4 - Mise en oeuvre des validateurs
    Maintenant que notre application est complète, nous allons ajouter des validateurs pour s'assurer que les données envoyées par le client sont correctes
    Le but est de commencer a mettre en oeuvre des pattern de conception

    Mettre en oeuvre une chain de responsabilité pour gérer plusieurs validateurs a la suite les uns des autres

### 5 - Ajout d'un lecteur, d'un écrivain et gestion simple d'une file d'évènement
    Le but est maintenant d'explorer l'architecture évènementielle. Nous allons donc créer un écrivain, qui s'activera lorsque le compte passe en négatif
    et un lecteur qui recevra l'info, et l'appliquera un agios sans tomber dans une boucle infinie

    PS : En supplément, nous pourrions créer un deuxième écrivain qui enverrait l'historique des opérations sur les comptes 
    et un écrivain qui stockerai ça dans la BDD

### 6 - Ajout des TU / TI
    Le développement de notre application touche a sa fin, nous allons maintenant faire les TU pour s'assurer de la non regression
    de notre application lors des prochains développements


