package com.example.gestion_cours.controller;

import com.example.gestion_cours.entity.AccesGroupe;
import com.example.gestion_cours.entity.Enseignant;
import com.example.gestion_cours.entity.Groupe;
import com.example.gestion_cours.entity.StatutCompte;
import com.example.gestion_cours.repository.AccesGroupeRepository;
import com.example.gestion_cours.repository.EnseignantRepository;
import com.example.gestion_cours.repository.GroupeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EnseignantRepository enseignantRepository;
    private final GroupeRepository groupeRepository;
    private final AccesGroupeRepository accesGroupeRepository;

    // Liste tous les comptes professeurs en attente de validation.
    @GetMapping("/enseignants-en-attente")
    public List<Enseignant> getEnseignantsEnAttente() {
        return enseignantRepository.findAll().stream()
                .filter(e -> e.getStatut() == StatutCompte.EN_ATTENTE)
                .toList();
    }
    // GET /api/admin/enseignants/recherche?telephone=...&nom=...&filiere=...
// Les trois paramètres sont optionnels et combinables (tous ceux fournis doivent correspondre).
    @GetMapping("/enseignants/recherche")
    public List<Enseignant> rechercherEnseignants(
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String filiere) {

        List<Enseignant> resultat = enseignantRepository.findAll();

        // Téléphone : correspondance exacte (c'est un identifiant).
        if (telephone != null && !telephone.isBlank()) {
            resultat = resultat.stream()
                    .filter(e -> e.getTelephone().equals(telephone))
                    .toList();
        }

        // Nom : correspondance partielle, n'importe où dans le nom.
        if (nom != null && !nom.isBlank()) {
            resultat = resultat.stream()
                    .filter(e -> e.getNom() != null && e.getNom().toLowerCase().contains(nom.toLowerCase()))
                    .toList();
        }

        // Filière : "commence par" (ex: "m" trouve "Mathématiques", "Médecine"...).
        // On regarde, pour chaque enseignant, s'il a au moins un accès dont la filière
        // commence par le texte donné.
        if (filiere != null && !filiere.isBlank()) {
            resultat = resultat.stream()
                    .filter(e -> accesGroupeRepository.findByEnseignant(e).stream()
                            .anyMatch(a -> a.getGroupe().getFiliere().getNom().toLowerCase().startsWith(filiere.toLowerCase())))
                    .toList();
        }

        return resultat;
    }
    // Accepte un compte professeur.
    @PutMapping("/enseignants/{telephone}/accepter")
    public ResponseEntity<Enseignant> accepterEnseignant(@PathVariable String telephone) {
        Enseignant enseignant = enseignantRepository.findById(telephone)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
        enseignant.setStatut(StatutCompte.ACCEPTE);
        return ResponseEntity.ok(enseignantRepository.save(enseignant));
    }

    // Refuse un compte professeur.
    @PutMapping("/enseignants/{telephone}/refuser")
    public ResponseEntity<Enseignant> refuserEnseignant(@PathVariable String telephone) {
        Enseignant enseignant = enseignantRepository.findById(telephone)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
        enseignant.setStatut(StatutCompte.REFUSE);
        return ResponseEntity.ok(enseignantRepository.save(enseignant));
    }

    // Donne à un enseignant ACCEPTÉ accès à un groupe précis (qui porte déjà sa filière et son semestre).
    // Corps attendu : {"telephone": "771234567", "groupeId": 1}
    @PostMapping("/acces")
    public ResponseEntity<?> donnerAcces(@RequestBody Map<String, Object> donnees) {
        String telephone = donnees.get("telephone").toString();
        Long groupeId = Long.valueOf(donnees.get("groupeId").toString());

        Enseignant enseignant = enseignantRepository.findById(telephone)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        if (enseignant.getStatut() != StatutCompte.ACCEPTE) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cet enseignant n'est pas encore accepté."));
        }

        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new RuntimeException("Groupe introuvable"));

        AccesGroupe acces = new AccesGroupe();
        acces.setEnseignant(enseignant);
        acces.setGroupe(groupe);
        return ResponseEntity.ok(accesGroupeRepository.save(acces));
    }

    // Liste les groupes auxquels un enseignant donné a accès.
    @GetMapping("/acces/{telephone}")
    public List<AccesGroupe> getAccesDeLEnseignant(@PathVariable String telephone) {
        Enseignant enseignant = enseignantRepository.findById(telephone)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
        return accesGroupeRepository.findByEnseignant(enseignant);
    }

    // Retire un accès précis.
    @DeleteMapping("/acces/{id}")
    public ResponseEntity<Void> retirerAcces(@PathVariable Long id) {
        accesGroupeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
