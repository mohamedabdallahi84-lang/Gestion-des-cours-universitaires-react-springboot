package com.example.gestion_cours.controller;

import com.example.gestion_cours.entity.Filiere;
import com.example.gestion_cours.entity.Groupe;
import com.example.gestion_cours.entity.Semestre;
import com.example.gestion_cours.repository.FiliereRepository;
import com.example.gestion_cours.repository.GroupeRepository;
import com.example.gestion_cours.repository.SemestreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ReferentielController {

    private final FiliereRepository filiereRepository;
    private final GroupeRepository groupeRepository;
    private final SemestreRepository semestreRepository;

    // --- Filières ---

    @GetMapping("/filieres")
    public List<Filiere> listerFilieres() {
        return filiereRepository.findAll();
    }

    @PostMapping("/filieres")
    public Filiere ajouterFiliere(@RequestBody Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    @DeleteMapping("/filieres/{id}")
    public void supprimerFiliere(@PathVariable Long id) {
        filiereRepository.deleteById(id);
    }

    // --- Semestres ---

    @GetMapping("/semestres")
    public List<Semestre> listerSemestres() {
        return semestreRepository.findAll();
    }

    @PostMapping("/semestres")
    public Semestre ajouterSemestre(@RequestBody Semestre semestre) {
        return semestreRepository.save(semestre);
    }

    @DeleteMapping("/semestres/{id}")
    public void supprimerSemestre(@PathVariable Long id) {
        semestreRepository.deleteById(id);
    }

    // --- Groupes (dépendent d'une filière ET d'un semestre) ---

    @GetMapping("/groupes")
    public List<Groupe> listerGroupes() {
        return groupeRepository.findAll();
    }

    // Le corps de la requête attendu est : {"nom": "Groupe A", "filiereId": 1, "semestreId": 1}
    @PostMapping("/groupes")
    public ResponseEntity<?> ajouterGroupe(@RequestBody Map<String, Object> donnees) {
        Long filiereId = Long.valueOf(donnees.get("filiereId").toString());
        Long semestreId = Long.valueOf(donnees.get("semestreId").toString());
        String nom = donnees.get("nom").toString();

        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException("Semestre introuvable"));

        Groupe groupe = new Groupe();
        groupe.setNom(nom);
        groupe.setFiliere(filiere);
        groupe.setSemestre(semestre);
        return ResponseEntity.ok(groupeRepository.save(groupe));
    }

    @DeleteMapping("/groupes/{id}")
    public void supprimerGroupe(@PathVariable Long id) {
        groupeRepository.deleteById(id);
    }
}