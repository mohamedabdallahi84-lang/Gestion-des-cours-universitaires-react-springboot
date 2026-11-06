package com.example.gestion_cours.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AccesGroupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enseignant_telephone")
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;
}