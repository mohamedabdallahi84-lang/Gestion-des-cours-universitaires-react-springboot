package com.example.gestion_cours.securite;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// @Component : Spring va créer une seule instance de cette classe,
// partagée par toute l'application (donc tous les jetons sont stockés au même endroit).
@Component
public class GestionnaireSessions {

    // Associe chaque jeton généré à l'identité de la personne connectée.
    // ConcurrentHashMap : une Map utilisable sans risque même si plusieurs
    // requêtes arrivent en même temps (normal pour un serveur web).
    private final Map<String, InfoConnexion> sessions = new ConcurrentHashMap<>();

    // Génère un nouveau jeton aléatoire unique pour une connexion réussie.
    public String creerSession(String identifiant, String role) {
        String jeton = UUID.randomUUID().toString();
        sessions.put(jeton, new InfoConnexion(identifiant, role));
        return jeton;
    }

    // Retrouve l'identité associée à un jeton, ou null si le jeton est invalide/expiré.
    public InfoConnexion getInfoConnexion(String jeton) {
        return sessions.get(jeton);
    }
}