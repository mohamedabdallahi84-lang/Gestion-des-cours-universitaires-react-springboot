package com.example.gestion_cours.controller;

import com.example.gestion_cours.entity.Admin;
import com.example.gestion_cours.entity.Enseignant;
import com.example.gestion_cours.entity.StatutCompte;
import com.example.gestion_cours.repository.AdminRepository;
import com.example.gestion_cours.repository.EnseignantRepository;
import com.example.gestion_cours.securite.GestionnaireSessions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EnseignantRepository enseignantRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final GestionnaireSessions gestionnaireSessions;

    // Un professeur crée son compte : statut EN_ATTENTE, en attendant la validation de l'admin.
    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody Enseignant donnees) {
        if (enseignantRepository.existsById(donnees.getTelephone())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ce numéro est déjà utilisé."));
        }
        Enseignant enseignant = new Enseignant();
        enseignant.setTelephone(donnees.getTelephone());
        enseignant.setNom(donnees.getNom());
        enseignant.setMotDePasse(passwordEncoder.encode(donnees.getMotDePasse()));
        enseignant.setStatut(StatutCompte.EN_ATTENTE);
        enseignantRepository.save(enseignant);
        return ResponseEntity.ok(Map.of("message", "Inscription envoyée, en attente de validation par l'admin."));
    }

    @PostMapping("/connexion-professeur")
    public ResponseEntity<?> connexionProfesseur(@RequestBody Map<String, String> donnees) {
        String telephone = donnees.get("telephone");
        String motDePasse = donnees.get("motDePasse");

        Enseignant enseignant = enseignantRepository.findById(telephone).orElse(null);
        if (enseignant == null || enseignant.getMotDePasse() == null
                || !passwordEncoder.matches(motDePasse, enseignant.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("message", "Téléphone ou mot de passe incorrect."));
        }
        if (enseignant.getStatut() != StatutCompte.ACCEPTE) {
            return ResponseEntity.status(403).body(Map.of("message", "Votre compte n'est pas encore validé par l'administrateur."));
        }

        String jeton = gestionnaireSessions.creerSession(telephone, "PROFESSEUR");
        return ResponseEntity.ok(Map.of("jeton", jeton, "role", "PROFESSEUR", "nom", enseignant.getNom()));
    }

    @PostMapping("/connexion-admin")
    public ResponseEntity<?> connexionAdmin(@RequestBody Map<String, String> donnees) {
        String nomUtilisateur = donnees.get("nomUtilisateur");
        String motDePasse = donnees.get("motDePasse");

        Admin admin = adminRepository.findById(1L).orElse(null);
        if (admin == null || !admin.getNomUtilisateur().equals(nomUtilisateur)
                || !passwordEncoder.matches(motDePasse, admin.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects."));
        }

        String jeton = gestionnaireSessions.creerSession("admin", "ADMIN");
        return ResponseEntity.ok(Map.of("jeton", jeton, "role", "ADMIN"));
    }
}
