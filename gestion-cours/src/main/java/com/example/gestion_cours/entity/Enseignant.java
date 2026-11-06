package com.example.gestion_cours.entity;

// Marque cette classe comme une entité JPA : elle correspondra
// à une nouvelle table "enseignant" dans la base de données.
import jakarta.persistence.Entity;

// Permet de marquer le champ qui sert de clé primaire de la table.
import jakarta.persistence.Id;
// @Enumerated précise comment stocker l'enum en base de données.
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
// Génère automatiquement un constructeur avec tous les champs (telephone, nom).
import lombok.AllArgsConstructor;

// Génère automatiquement les getters, setters, toString(), equals() et hashCode().
import lombok.*;

// Génère automatiquement un constructeur vide, nécessaire pour JPA/Hibernate.
//import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enseignant {

    // @Id : indique que ce champ est la clé primaire de la table.
    // Contrairement à Cours, il n'y a PAS de @GeneratedValue ici :
    // c'est nous (via le JSON envoyé) qui fournissons cette valeur,
    // la base de données ne la génère pas automatiquement.
    @Id
    private String telephone;

    // Le nom de l'enseignant (ex : "M. Diop").
    private String nom;
    // Le mot de passe sera stocké HACHÉ (jamais en texte brut).
    private String motDePasse;
    // EnumType.STRING : stocke "EN_ATTENTE", "ACCEPTE", "REFUSE" en texte
    // dans la base (plus lisible que des nombres, plus simple à déboguer).
    @Enumerated(EnumType.STRING)
    private StatutCompte statut;
}
