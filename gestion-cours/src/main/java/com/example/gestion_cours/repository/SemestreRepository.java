package com.example.gestion_cours.repository;

import com.example.gestion_cours.entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
}