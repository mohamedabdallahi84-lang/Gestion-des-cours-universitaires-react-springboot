package com.example.gestion_cours.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    // Il n'y aura toujours qu'UNE seule ligne dans cette table,
    // donc pas besoin d'auto-génération : on fixera l'id à 1 nous-mêmes.
    @Id
    private Long id;

    private String nomUtilisateur;

    // Le mot de passe sera stocké HACHÉ (jamais en texte brut),
    // grâce à BCryptPasswordEncoder fourni par Spring Security.
    private String motDePasse;
}