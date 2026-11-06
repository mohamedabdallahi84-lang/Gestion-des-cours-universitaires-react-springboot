package com.example.gestion_cours.repository;

import com.example.gestion_cours.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}