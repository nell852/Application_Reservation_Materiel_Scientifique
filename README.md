# 🧪 GestionMatérielScientifique

**GestionMatérielScientifique** est une application Java conçue pour faciliter la gestion du matériel scientifique dans un laboratoire ou une institution de recherche.  
Elle permet de suivre l'inventaire, les emprunts, les maintenances et de générer des rapports sur l'état du matériel.

---

## ✨ Fonctionnalités

- ✅ **Ajout/Suppression/Modification** : Gestion des équipements scientifiques (nom, référence, catégorie, état, etc.)
- 📦 **Suivi des emprunts** : Enregistrement des prêts et retours de matériel avec dates et utilisateurs
- 🛠️ **Maintenance** : Planification et suivi des opérations de maintenance ou de calibration
- 📈 **Rapports** : Génération de rapports sur l’utilisation, l’état et la disponibilité du matériel
- 💻 **Interface utilisateur** : Interface graphique intuitive (JavaFX/Swing) ou console selon la version

---

## 📋 Prérequis

- ☕ **Java Runtime Environment (JRE)** : version 8 ou supérieure  
- 🖥️ **Système d’exploitation compatible** : Windows, macOS ou Linux  
- 🛢️ **(Facultatif) Base de données** : MySQL, SQLite ou autre (si l'application utilise la persistance des données)

---

## 📦 Installation

### 1. Télécharger le fichier `.jar`

Accédez à la section [Releases](#) pour télécharger la dernière version du fichier **GestionMaterielScientifique.jar**

### 2. Vérifier Java

```bash
java -version
```

### 3. Lancer l'application

- 🖱️ Double-cliquez sur le fichier `.jar`  
- ou lancez-le en ligne de commande :

```bash
java -jar GestionMaterielScientifique.jar
```

---

## ⚙️ Configuration

### 📁 Base de données (si applicable)

Configurez les paramètres de connexion dans le fichier `config.properties` (situé à côté du `.jar` ou généré automatiquement au premier lancement).

**Exemple :**

```properties
db.url=jdbc:mysql://localhost:3306/gestion_materiel
db.user=votre_utilisateur
db.password=votre_mot_de_passe
```

### 🎛️ Personnalisation

Modifiez les paramètres de l’application (langue, format des rapports, etc.) via le menu de configuration de l’interface.

---

## 🚀 Utilisation

- ▶️ **Démarrage** : Lancez l’application via le `.jar`
- 🧭 **Navigation** : Utilisez le menu principal pour accéder aux différentes fonctionnalités (ajout de matériel, emprunts, etc.)
- 📤 **Rapports** : Exportez les rapports au format **PDF** ou **CSV**

---

## 🤝 Contribution

1. Clonez le dépôt GitHub :

```bash
git clone https://github.com/votre-nom-utilisateur/GestionMaterielScientifique.git
```

2. Créez une branche, apportez vos modifications, puis soumettez une pull request.

Signalez les bogues ou proposez des améliorations via la section **Issues**.

---

## 📝 Licence

Ce projet est sous licence **MIT**.  
Voir le fichier `LICENSE` pour plus de détails.
