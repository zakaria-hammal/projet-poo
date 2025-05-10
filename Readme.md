# 🚗 Projet POO: Système de Covoiturage Universitaire

[![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](https://opensource.org/licenses/MIT)
[![GitHub stars](https://img.shields.io/github/stars/zakaria-hammal/projet-poo?style=social)](https://github.com/zakaria-hammal/projet-poo/stargazers)

Un système de covoiturage conçu pour la communauté universitaire, implémentant les principes de POO avec Java.

<p align="center">
  <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExcWZ5b2V0Y3J5b3R1a2R4Y2Z5eHp4b2R4Y3B2dGJ6Z2VjZ3V0eCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/3o7qE1YN7aBOFPRw8E/giphy.gif" width="400" alt="Carpool Animation">
</p>

## 🌟 Fonctionnalités

- **Gestion des utilisateurs** (Étudiants, Enseignants, ATS)
- **Système de réputation** basé sur les trajets
- **Recherche de covoiturage** par faculté/date
- **Sérialisation** des données pour persistance
- **Validation stricte** des matricules universitaires
- **Système d'authentification** sécurisé

## 🛠️ Structure du Code

```bash
projet-poo/
├── src/
│   ├── Utilisateur.java          # Classe abstraite parente
│   ├── Etudiant.java             # Implémentation étudiante
│   ├── Enseignant.java           # Implémentation enseignante
│   ├── ATS.java                  # Implémentation personnel ATS
│   ├── UtilisateurExistDeja.java  # Exception personnalisée
│   └── ... (autres classes)
├── FichiersDeSauvegarde/          # Données sérialisées
└── README.md