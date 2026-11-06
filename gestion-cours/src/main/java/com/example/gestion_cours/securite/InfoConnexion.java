package com.example.gestion_cours.securite;

// Représente l'identité d'une personne connectée : qui elle est, et son rôle.
public class InfoConnexion {
    private final String identifiant; // le téléphone pour un professeur, "admin" pour l'admin
    private final String role; // "ADMIN" ou "PROFESSEUR"

    public InfoConnexion(String identifiant, String role) {
        this.identifiant = identifiant;
        this.role = role;
    }

    public String getIdentifiant() { return identifiant; }
    public String getRole() { return role; }
}