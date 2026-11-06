package com.example.gestion_cours.repository;

import com.example.gestion_cours.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
}