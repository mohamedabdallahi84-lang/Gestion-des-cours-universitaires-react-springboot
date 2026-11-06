package com.example.gestion_cours.repository;

// On importe l'entité Enseignant, que ce repository va manipuler.
import com.example.gestion_cours.entity.Enseignant;

// Fournit gratuitement les méthodes de base (save, findAll, findById, deleteById...).
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Enseignant, String> : "Enseignant" est le type d'entité géré,
// "String" est le type de sa clé primaire (le téléphone), au lieu de Long habituellement.
public interface EnseignantRepository extends JpaRepository<Enseignant, String> {
}
