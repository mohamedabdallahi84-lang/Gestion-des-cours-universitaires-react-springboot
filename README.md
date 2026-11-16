\# Gestion des cours universitaires



Application web full-stack de gestion des emplois du temps universitaires : un administrateur planifie les cours, les professeurs les confirment ou les refusent, et les étudiants consultent l'emploi du temps sans connexion.



\*\*Stack :\*\* Java · Spring Boot · Spring Security · Spring Data JPA · MySQL · React 19 · Vite · Axios · React Router



\## Fonctionnalités



\*\*Administrateur\*\*

\- Connexion sécurisée

\- Gestion des cours (ajout, modification, suppression, recherche par nom)

\- Gestion des enseignants

\- Validation ou refus des comptes professeurs en attente

\- Gestion des filières, semestres et groupes



\*\*Professeur\*\*

\- Création de compte (soumis à validation par l'administrateur)

\- Consultation de son emploi du temps

\- Confirmation ou refus de ses cours



\*\*Public (sans connexion)\*\*

\- Consultation de l'emploi du temps par filière et par semestre



\*\*Règles métier\*\*

\- Détection des conflits d'horaires pour un groupe ou un enseignant à une même date

\- Créneaux horaires prédéfinis, dimanche exclu, pas de cours planifié dans le passé

\- Un cours refusé par le professeur ne bloque plus le créneau



\## Sécurité



\- Mots de passe hachés avec BCrypt ; le hash n'est jamais renvoyé par l'API

\- Rôles `ADMIN` et `PROFESSEUR` appliqués côté serveur (Spring Security)

\- Authentification par jeton (en-tête `Authorization: Bearer`)

\- CORS restreint à l'origine du frontend



\*\*Limites connues :\*\* les sessions sont stockées en mémoire (elles sont perdues au redémarrage du serveur) et n'expirent pas. Un compte administrateur de démonstration est créé automatiquement au premier démarrage (voir ci-dessous) : à changer avant tout déploiement en ligne.



\## Prérequis



\- Java 17 ou supérieur

\- Node.js 18 ou supérieur

\- MySQL 8



\## Installation



\### 1. Base de données



Créer la base dans MySQL :



```sql

CREATE DATABASE gestion\_cours;

```



Les tables sont créées automatiquement au démarrage (Hibernate `ddl-auto=update`).



\### 2. Backend



```bash

cd gestion-cours

```



Copier `src/main/resources/application.properties.example` vers `src/main/resources/application.properties`, puis renseigner l'URL, l'utilisateur et le mot de passe MySQL.



Lancer le serveur (port 8080) :



```bash

./mvnw spring-boot:run        # Linux / macOS

.\\mvnw spring-boot:run        # Windows

```



\### 3. Frontend



```bash

cd gestion-cours-frontend

npm install

npm run dev

```



L'application est disponible sur http://localhost:5173



\## Compte de démonstration



Au premier démarrage, un compte administrateur est créé automatiquement :



| Nom d'utilisateur | Mot de passe |

|-------------------|--------------|

| `admin`           | `admin123`   |



Compte destiné uniquement au développement local.



Pour tester le côté professeur : créer un compte via « Créer un compte professeur », le valider depuis l'espace administrateur (« Comptes en attente »), puis se connecter.



\## Structure du projet



```

gestion-cours/             Backend Spring Boot (API REST)

&#x20; controller/  service/  repository/  entity/  config/  securite/

gestion-cours-frontend/    Frontend React (Vite)

&#x20; src/services/            Appels API (Axios)

&#x20; src/context/             Contexte d'authentification

```



\## Auteur



Mohamed Abdallahi : \[GitHub](https://github.com/mohamedabdallahi84-lang)

