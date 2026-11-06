package com.example.gestion_cours.repository;

import com.example.gestion_cours.entity.AccesGroupe;
import com.example.gestion_cours.entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccesGroupeRepository extends JpaRepository<AccesGroupe, Long> {
    List<AccesGroupe> findByEnseignant(Enseignant enseignant);
}
