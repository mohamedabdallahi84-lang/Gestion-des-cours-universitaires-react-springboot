package com.example.gestion_cours.controller;

// L'entité que ce contrôleur reçoit et renvoie.
import com.example.gestion_cours.entity.Enseignant;

// Le repository utilisé pour parler à la base de données.
import com.example.gestion_cours.repository.EnseignantRepository;

// Génère le constructeur permettant à Spring d'injecter EnseignantRepository.
import lombok.RequiredArgsConstructor;

// Contient les codes de statut HTTP (200, 201, 204...).
import org.springframework.http.HttpStatus;

// Permet de construire une réponse HTTP complète (corps + code de statut).
import org.springframework.http.ResponseEntity;

// Regroupe les annotations de routes : @RestController, @RequestMapping,
// @GetMapping, @PostMapping, @DeleteMapping, @PathVariable, @RequestBody.
import org.springframework.web.bind.annotation.*;

// Utilisé comme type de retour pour la liste de tous les enseignants.
import java.util.List;

// @RestController : cette classe gère des requêtes HTTP et renvoie du JSON.
@RestController
// Toutes les routes de cette classe commencent par "/api/enseignants".
@RequestMapping("/api/enseignants")
// Génère le constructeur qui permet à Spring d'injecter EnseignantRepository.
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantRepository enseignantRepository;
    // GET /api/enseignants/{telephone} -> récupère un enseignant précis (pour préremplir le formulaire de modification).
    @GetMapping("/{telephone}")
    public ResponseEntity<Enseignant> getEnseignantByTelephone(@PathVariable String telephone) {
        return enseignantRepository.findById(telephone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/enseignants/{telephone} -> modifie le nom d'un enseignant existant.
// Le téléphone (clé primaire) ne peut pas être changé, seul le nom est modifiable.
    @PutMapping("/{telephone}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable String telephone, @RequestBody Enseignant enseignantDetails) {
        Enseignant enseignant = enseignantRepository.findById(telephone)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable avec le téléphone " + telephone));
        enseignant.setNom(enseignantDetails.getNom());
        Enseignant misAJour = enseignantRepository.save(enseignant);
        return ResponseEntity.ok(misAJour);
    }
    // GET /api/enseignants -> liste tous les enseignants enregistrés.
    @GetMapping
    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    // POST /api/enseignants -> enregistre un nouvel enseignant.
    // @RequestBody transforme le JSON envoyé (ex: {"telephone":"...", "nom":"..."})
    // en objet Java Enseignant.
    @PostMapping
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant nouveau = enseignantRepository.save(enseignant);
        // Code 201 (CREATED) : la ressource a bien été créée.
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    // DELETE /api/enseignants/{telephone} -> supprime un enseignant par son téléphone.
    // @PathVariable String telephone récupère la valeur dans l'URL.
    @DeleteMapping("/{telephone}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable String telephone) {
        enseignantRepository.deleteById(telephone);
        // Code 204 (NO CONTENT) : succès, sans rien à renvoyer.
        return ResponseEntity.noContent().build();
    }
}
