package com.example.gestion_cours.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// @RestControllerAdvice : s'applique à TOUS les contrôleurs de l'application,
// pour intercepter les exceptions et construire une réponse JSON propre.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(RuntimeException.class) : cette méthode s'exécute
    // chaque fois qu'une RuntimeException est levée n'importe où dans le code
    // (validerCours, verifierConflitHoraire, etc.), au lieu de laisser
    // Spring renvoyer une erreur 500 générique sans détail.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> gererErreur(RuntimeException ex) {
        // On renvoie le code 400 (Demande incorrecte) avec le VRAI message
        // de l'exception, dans un objet JSON {"message": "..."}.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }
}
