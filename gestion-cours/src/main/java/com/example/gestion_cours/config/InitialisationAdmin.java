package com.example.gestion_cours.config;

import com.example.gestion_cours.entity.Admin;
import com.example.gestion_cours.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
// CommandLineRunner : Spring exécute automatiquement la méthode run()
// une seule fois, juste après le démarrage complet de l'application.
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialisationAdmin implements CommandLineRunner {

    private final AdminRepository adminRepository;

    @Override
    public void run(String... args) {
        // Si aucun admin n'existe encore en base (premier démarrage du projet),
        // on en crée un avec un mot de passe par défaut.
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setId(1L);
            admin.setNomUtilisateur("admin");
            // On hache le mot de passe par défaut avant de l'enregistrer.
            admin.setMotDePasse(new BCryptPasswordEncoder().encode("admin123"));
            adminRepository.save(admin);
            System.out.println("Compte admin créé : nomUtilisateur=admin, motDePasse=admin123 (à changer après connexion)");
        }
    }
}
