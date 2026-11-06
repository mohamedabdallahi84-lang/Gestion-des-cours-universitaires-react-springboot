package com.example.gestion_cours.controller;

import com.example.gestion_cours.entity.Cours;
import com.example.gestion_cours.entity.Filiere;
import com.example.gestion_cours.entity.Groupe;
import com.example.gestion_cours.entity.Semestre;
import com.example.gestion_cours.repository.CoursRepository;
import com.example.gestion_cours.repository.FiliereRepository;
import com.example.gestion_cours.repository.GroupeRepository;
import com.example.gestion_cours.repository.SemestreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Routes sous /api/public/** : accessibles SANS connexion,
// comme défini dans SecurityConfig (.requestMatchers("/api/public/**").permitAll()).
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final FiliereRepository filiereRepository;
    private final SemestreRepository semestreRepository;
    private final GroupeRepository groupeRepository;
    private final CoursRepository coursRepository;

    @GetMapping("/filieres")
    public List<Filiere> listerFilieres() {
        return filiereRepository.findAll();
    }

    @GetMapping("/semestres")
    public List<Semestre> listerSemestres() {
        return semestreRepository.findAll();
    }

    // Liste les groupes correspondant à la filière et au semestre choisis par l'étudiant.
    @GetMapping("/groupes")
    public List<Groupe> listerGroupes(@RequestParam Long filiereId, @RequestParam Long semestreId) {
        return groupeRepository.findAll().stream()
                .filter(g -> g.getFiliere().getId().equals(filiereId) && g.getSemestre().getId().equals(semestreId))
                .toList();
    }

    // Renvoie tous les cours d'un groupe précis, avec leur statut (en attente/confirmé/refusé).
    @GetMapping("/emploi-du-temps")
    public List<Cours> getEmploiDuTemps(@RequestParam Long groupeId) {
        return coursRepository.findAll().stream()
                .filter(c -> c.getGroupe() != null && c.getGroupe().getId().equals(groupeId))
                .toList();
    }
}