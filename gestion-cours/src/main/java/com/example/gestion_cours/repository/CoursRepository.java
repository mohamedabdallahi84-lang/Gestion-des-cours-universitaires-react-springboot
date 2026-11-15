package com.example.gestion_cours.repository;
import java.time.LocalDate;

import com.example.gestion_cours.entity.Cours;
import com.example.gestion_cours.entity.Groupe;
// JpaRepository est une interface fournie par Spring Data JPA.
// Elle contient déjà toutes les méthodes de base pour parler à la base de données
// (sauvegarder, lister, supprimer...), sans qu'on ait à écrire la moindre requête SQL.
import org.springframework.data.jpa.repository.JpaRepository;
// On importe Enseignant, car la nouvelle méthode de recherche en a besoin comme paramètre.
import com.example.gestion_cours.entity.Enseignant;
// List est utilisé comme type de retour pour la méthode de recherche ci-dessous,
// car une recherche peut renvoyer plusieurs cours.
import java.util.List;

// On déclare une INTERFACE (pas une classe), car on n'implémente rien nous-mêmes :
// Spring Data JPA va générer automatiquement le code derrière, au démarrage de l'application.
//
// JpaRepository<Cours, Long> veut dire :
// - Cours       : le type d'entité que ce repository gère
// - Long        : le type de la clé primaire (id) de cette entité
public interface CoursRepository extends JpaRepository<Cours, Long> {


    List<Cours> findByNomContainingIgnoreCase(String nom);

    // Tous les cours d'un groupe, un jour donné (peu importe l'horaire exact,
    // on vérifie le chevauchement nous-mêmes dans le service).
    List<Cours> findByGroupeAndJour(Groupe groupe, String jour);

    // Tous les cours d'un enseignant, un jour donné.
    List<Cours> findByEnseignantAndJour(Enseignant enseignant, String jour);
    // Spring Data JPA comprend "Enseignant_Telephone" comme "le champ telephone
// de l'enseignant lié à ce cours", grâce au underscore qui sépare la relation du champ.
    List<Cours> findByEnseignant_Telephone(String telephone);
    
    List<Cours> findByGroupeAndDate(Groupe groupe, LocalDate date);

    List<Cours> findByEnseignantAndDate(Enseignant enseignant, LocalDate date);
}
