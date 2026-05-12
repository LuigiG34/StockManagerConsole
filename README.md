# StockManagerConsole

Mini application Java console de gestion de stock.

Ce projet permet de pratiquer Java orienté objet sans framework, avec une architecture simple inspirée des applications backend classiques : modèles, repositories, services, interface console, exceptions personnalisées, sauvegarde CSV et tests unitaires JUnit.

L'objectif est de comprendre les bases Java avant de passer à Spring Boot.

---

## Fonctionnalités

L'application permet de :

- se connecter avec un email et un mot de passe ;
- gérer deux rôles : `ADMIN` et `USER` ;
- afficher la liste des produits ;
- afficher le détail d'un produit ;
- ajouter un produit en tant qu'administrateur ;
- modifier un produit en tant qu'administrateur ;
- supprimer un produit en tant qu'administrateur ;
- afficher les utilisateurs en tant qu'administrateur ;
- créer un utilisateur en tant qu'administrateur ;
- sauvegarder les données dans des fichiers CSV ;
- lancer des tests unitaires avec JUnit.

---

## Comptes par défaut

Au premier lancement, l'application crée automatiquement deux utilisateurs.

### Administrateur

```txt
Email : admin@test.com
Mot de passe : admin123
```

### Utilisateur simple

```txt
Email : user@test.com
Mot de passe : user123
```

---

## Rôles

### Rôle USER

Un utilisateur simple peut :

```txt
1. Voir tous les produits
2. Voir le détail d'un produit
3. Se déconnecter
4. Quitter l'application
```

### Rôle ADMIN

Un administrateur peut :

```txt
1. Voir tous les produits
2. Voir le détail d'un produit
3. Ajouter un produit
4. Modifier un produit
5. Supprimer un produit
6. Voir les utilisateurs
7. Créer un utilisateur
8. Se déconnecter
9. Quitter l'application
```

---

## Produits par défaut

Au premier lancement, l'application crée automatiquement quelques produits :

```txt
1 - Clavier - 49.99€ - stock : 10
2 - Souris - 19.99€ - stock : 25
3 - Écran - 149.99€ - stock : 5
```

---

## Structure du projet

```txt
StockManagerConsole/
├── pom.xml
├── README.md
├── data/
│   ├── products.csv
│   └── users.csv
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── stockmanager/
│   │               ├── Main.java
│   │               ├── exception/
│   │               │   ├── DuplicateEmailException.java
│   │               │   ├── NotFoundException.java
│   │               │   ├── UnauthorizedException.java
│   │               │   └── ValidationException.java
│   │               ├── model/
│   │               │   ├── Product.java
│   │               │   ├── Role.java
│   │               │   └── User.java
│   │               ├── repository/
│   │               │   ├── ProductRepository.java
│   │               │   └── UserRepository.java
│   │               ├── service/
│   │               │   ├── AuthService.java
│   │               │   ├── ProductService.java
│   │               │   └── UserService.java
│   │               └── ui/
│   │                   ├── ConsoleMenu.java
│   │                   └── InputReader.java
│   └── test/
│       └── java/
│           └── com/
│               └── stockmanager/
│                   └── service/
│                       ├── AuthServiceTest.java
│                       ├── ProductServiceTest.java
│                       └── UserServiceTest.java
```

---

## Architecture

Le projet est découpé en plusieurs couches.

### `model`

Contient les objets métier :

```txt
Product
User
Role
```

Exemple :

- `Product` représente un produit avec un id, un nom, un prix et une quantité en stock.
- `User` représente un utilisateur avec un id, un email, un mot de passe et un rôle.
- `Role` est une enum avec les valeurs `ADMIN` et `USER`.

### `repository`

Contient les classes responsables du stockage des données.

```txt
ProductRepository
UserRepository
```

Les repositories utilisent une liste en mémoire (`ArrayList`) et sauvegardent les données dans des fichiers CSV.

### `service`

Contient la logique métier.

```txt
AuthService
ProductService
UserService
```

Exemples de règles métier :

- seul un admin peut créer un produit ;
- seul un admin peut modifier un produit ;
- seul un admin peut supprimer un produit ;
- seul un admin peut créer un utilisateur ;
- un produit doit avoir un nom non vide ;
- un produit doit avoir un prix supérieur à 0 ;
- un stock ne peut pas être négatif ;
- un email utilisateur doit être unique.

### `ui`

Contient l'interface console.

```txt
ConsoleMenu
InputReader
```

`ConsoleMenu` affiche les menus et appelle les services.

`InputReader` lit les entrées utilisateur et évite que l'application plante si l'utilisateur tape une mauvaise valeur, par exemple une lettre au lieu d'un nombre.

### `exception`

Contient les exceptions personnalisées.

```txt
ValidationException
UnauthorizedException
NotFoundException
DuplicateEmailException
```

Elles permettent d'obtenir des erreurs plus explicites que de simples `null` ou `false`.

Exemples :

```txt
Produit introuvable.
Action réservée aux administrateurs.
Le prix doit être supérieur à 0.
Cet email est déjà utilisé.
```

---

## Stockage CSV

Les données sont sauvegardées dans le dossier :

```txt
data/
```

Fichiers utilisés :

```txt
data/products.csv
data/users.csv
```

Exemple de fichier `products.csv` :

```csv
1;Clavier;49.99;10
2;Souris;19.99;25
3;Écran;149.99;5
```

Exemple de fichier `users.csv` :

```csv
1;admin@test.com;admin123;ADMIN
2;user@test.com;user123;USER
```

Si les fichiers n'existent pas, ils sont créés automatiquement au lancement.

Attention : les mots de passe sont stockés en clair volontairement pour garder le projet simple. Dans une vraie application, ils devraient être hashés.

---

## Prérequis

Le projet utilise :

```txt
Java 17
Maven
JUnit 5
```

Vérifier Java :

```bash
java -version
```

Vérifier Maven :

```bash
mvn -version
```

---

## Installation

Cloner le projet ou télécharger le dossier, puis ouvrir un terminal à la racine du projet :

```bash
cd StockManagerConsole
```

La racine du projet doit contenir le fichier :

```txt
pom.xml
```

---

## Compiler le projet

```bash
mvn clean compile
```

Si tout est correct, Maven doit afficher :

```txt
BUILD SUCCESS
```

---

## Lancer l'application

Depuis la racine du projet :

```bash
mvn exec:java
```

L'application démarre dans le terminal :

```txt
=== Stock Manager Console ===

=== Connexion ===
Email :
Mot de passe :
```

---

## Tester l'application manuellement

### 1. Se connecter en admin

Entrer :

```txt
Email : admin@test.com
Mot de passe : admin123
```

Menu attendu :

```txt
=== Menu ADMIN ===
1. Voir tous les produits
2. Voir le détail d'un produit
3. Ajouter un produit
4. Modifier un produit
5. Supprimer un produit
6. Voir les utilisateurs
7. Créer un utilisateur
8. Se déconnecter
9. Quitter l'application
```

### 2. Voir les produits

Choisir :

```txt
1
```

Résultat attendu :

```txt
=== Liste des produits ===
1 - Clavier - 49.99€ - stock : 10
2 - Souris - 19.99€ - stock : 25
3 - Écran - 149.99€ - stock : 5
```

### 3. Voir le détail d'un produit

Choisir :

```txt
2
```

Puis entrer un id, par exemple :

```txt
ID du produit : 1
```

Résultat attendu :

```txt
ID : 1
Nom : Clavier
Prix : 49.99€
Stock : 10
Disponible : Oui
```

### 4. Ajouter un produit

Choisir :

```txt
3
```

Puis entrer par exemple :

```txt
Nom : Casque
Prix : 79,99
Quantité en stock : 5
```

Le programme accepte les prix avec une virgule ou un point :

```txt
79,99
79.99
```

Résultat attendu :

```txt
Produit créé : 4 - Casque - 79.99€ - stock : 5
```

### 5. Modifier un produit

Choisir :

```txt
4
```

Puis entrer l'id du produit à modifier :

```txt
ID du produit à modifier : 4
```

Entrer les nouvelles valeurs :

```txt
Nouveau nom : Casque Bluetooth
Nouveau prix : 89,99
Nouvelle quantité en stock : 8
```

Résultat attendu :

```txt
Produit modifié.
```

### 6. Supprimer un produit

Choisir :

```txt
5
```

Puis entrer l'id du produit à supprimer :

```txt
ID du produit à supprimer : 4
```

L'application demande une confirmation :

```txt
Confirmer la suppression ? oui/non :
```

Entrer :

```txt
oui
```

Résultat attendu :

```txt
Produit supprimé.
```

### 7. Voir les utilisateurs

Choisir :

```txt
6
```

Résultat attendu :

```txt
=== Liste des utilisateurs ===
1 - admin@test.com - ADMIN
2 - user@test.com - USER
```

### 8. Créer un utilisateur

Choisir :

```txt
7
```

Entrer par exemple :

```txt
Email : test@test.com
Mot de passe : test123
Rôle :
1. ADMIN
2. USER
Choix : 2
```

Résultat attendu :

```txt
Utilisateur créé : 3 - test@test.com - USER
```

### 9. Se déconnecter

Choisir :

```txt
8
```

Résultat attendu :

```txt
Déconnexion réussie.
```

L'application revient ensuite à l'écran de connexion.

### 10. Se connecter avec le nouvel utilisateur

Entrer :

```txt
Email : test@test.com
Mot de passe : test123
```

Menu attendu :

```txt
=== Menu USER ===
1. Voir tous les produits
2. Voir le détail d'un produit
3. Se déconnecter
4. Quitter l'application
```

L'utilisateur simple ne peut pas créer, modifier ou supprimer de produits.

---

## Tester les erreurs

### Produit introuvable

Dans le menu, choisir le détail d'un produit et entrer :

```txt
ID du produit : 999
```

Résultat attendu :

```txt
Erreur : Produit introuvable.
```

### Prix invalide

Essayer de créer un produit avec :

```txt
Prix : 0
```

Résultat attendu :

```txt
Erreur : Le prix doit être supérieur à 0.
```

### Stock invalide

Essayer de créer un produit avec :

```txt
Quantité en stock : -1
```

Résultat attendu :

```txt
Erreur : Le stock ne peut pas être négatif.
```

### Email déjà utilisé

Essayer de créer un utilisateur avec :

```txt
Email : user@test.com
```

Résultat attendu :

```txt
Erreur : Cet email est déjà utilisé.
```

---

## Vérifier la sauvegarde CSV

Après avoir ajouté un produit ou un utilisateur, quitter l'application puis relancer :

```bash
mvn exec:java
```

Les données doivent toujours être présentes.

Il est aussi possible d'ouvrir les fichiers :

```txt
data/products.csv
data/users.csv
```

pour vérifier que les données ont été enregistrées.

---

## Lancer les tests unitaires

Depuis la racine du projet :

```bash
mvn test
```

Résultat attendu :

```txt
BUILD SUCCESS
```

Les tests couvrent notamment :

```txt
AuthService
- connexion réussie
- connexion échouée avec mauvais mot de passe
- connexion échouée avec email inconnu
- déconnexion

ProductService
- liste des produits par défaut
- détail produit
- produit introuvable
- création produit par admin
- refus de création produit par user
- validation du nom produit
- validation du prix
- validation du stock
- modification produit
- suppression produit

UserService
- liste utilisateurs par admin
- refus liste utilisateurs par user
- création utilisateur par admin
- refus création utilisateur par user
- email unique
- email non vide
- mot de passe non vide
- rôle obligatoire
```

Les tests utilisent `@TempDir`, donc ils créent leurs propres fichiers CSV temporaires et ne modifient pas les fichiers du dossier `data/`.

---

## Commandes utiles

Compiler :

```bash
mvn clean compile
```

Lancer l'application :

```bash
mvn exec:java
```

Lancer les tests :

```bash
mvn test
```

Nettoyer le projet :

```bash
mvn clean
```

Compiler puis lancer :

```bash
mvn clean compile exec:java
```

---

## Objectif pédagogique

Ce projet permet de pratiquer :

```txt
classes
objets
constructeurs
encapsulation
getters
enums
ArrayList
repositories
services
exceptions personnalisées
lecture console
CRUD
authentification simple
rôles ADMIN / USER
sauvegarde CSV
tests unitaires JUnit
Maven
```

---

## Comparaison avec Symfony / PHP

Pour quelqu'un qui vient de Symfony :

```txt
Java model       ≈ Entity Symfony
Java repository  ≈ Repository Symfony
Java service     ≈ Service Symfony
ConsoleMenu      ≈ Controller console
Maven            ≈ Composer
JUnit            ≈ PHPUnit
JPA/Hibernate    ≈ Doctrine ORM
Spring Boot      ≈ Symfony côté Java
```

Exemples :

```txt
userRepository.findByEmail(email)
≈
$userRepository->findOneBy(['email' => $email])
```

```txt
ProductService
≈
un service Symfony contenant la logique métier
```

```txt
ValidationException
≈
une exception métier ou une erreur de validation contrôlée
```

---

## Limites connues

Le projet reste volontairement simple :

```txt
pas de base de données
pas de hash de mot de passe
pas d'interface web
pas d'API REST
pas de Spring Boot
pas de vraie session sécurisée
pas de validation email avancée
pas de gestion fine des permissions
```

Les mots de passe sont stockés en clair dans le CSV uniquement pour simplifier l'apprentissage.

---

## Prochaines évolutions possibles

Évolutions possibles en Java pur :

```txt
1. Refactorer les repositories avec une classe abstraite CsvRepository<T>
2. Ajouter des interfaces Repository
3. Ajouter un hash de mot de passe simple
4. Ajouter plus de tests
5. Améliorer la validation des emails
```

Évolution principale recommandée :

```txt
Refaire le même projet en Spring Boot + Thymeleaf
```

Puis ensuite :

```txt
Spring Boot REST API + React
```

L'intérêt est de comparer ce qui a été fait manuellement ici avec ce que Spring Boot automatise.
