package com.example.gestion_cours.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "enseignant_telephone")
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;

    private int credits;
    private String jour;
    private String horaire;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    // Statut du cours : toujours EN_ATTENTE à la création,
    // ensuite confirmé ou refusé par le professeur concerné.
    @Enumerated(EnumType.STRING)
    private StatutCours statut;
}