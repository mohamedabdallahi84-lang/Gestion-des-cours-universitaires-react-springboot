package com.example.gestion_cours.controller;

import com.example.gestion_cours.entity.Cours;
import com.example.gestion_cours.service.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// Principal : Spring injecte automatiquement l'identité de la personne
// connectée (extraite du jeton par notre FiltreAuthentification).
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/professeur")
@RequiredArgsConstructor
public class ProfesseurController {

    private final CoursService coursService;
    @GetMapping("/mon-emploi")
    public List<Cours> monEmploi(Principal principal) {
        return coursService.getMonEmploiDuTemps(principal.getName());
    }
    @PutMapping("/cours/{id}/confirmer")
    public Cours confirmer(@PathVariable Long id, Principal principal) {
        // principal.getName() renvoie le téléphone du professeur connecté.
        return coursService.confirmerCours(id, principal.getName());
    }

    @PutMapping("/cours/{id}/refuser")
    public Cours refuser(@PathVariable Long id, Principal principal) {
        return coursService.refuserCours(id, principal.getName());
    }
}