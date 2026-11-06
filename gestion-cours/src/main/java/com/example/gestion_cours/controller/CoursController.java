package com.example.gestion_cours.controller;

// On importe Cours, car le contrôleur reçoit et renvoie des objets Cours.
import com.example.gestion_cours.entity.Cours;

// On importe le service créé à l'étape 6 : le contrôleur va l'utiliser
// pour exécuter la vraie logique métier, il ne fait que "router" les requêtes.
import com.example.gestion_cours.service.CoursService;


import lombok.RequiredArgsConstructor;

// HttpStatus contient les codes de statut HTTP standards (200, 201, 404...).
import org.springframework.http.HttpStatus;

// ResponseEntity permet de construire une réponse HTTP complète :
// le corps (les données) ET le code de statut (200, 201, 204...).
import org.springframework.http.ResponseEntity;

// Regroupe toutes les annotations Spring nécessaires pour définir les routes :
// @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping,
// @DeleteMapping, @PathVariable, @RequestParam, @RequestBody.
import org.springframework.web.bind.annotation.*;


import java.util.List;

// @RestController : dit à Spring que cette classe gère des requêtes HTTP
// et que chaque méthode doit renvoyer directement du JSON (pas une page HTML).
@RestController

// @RequestMapping("/api/cours") : préfixe commun à toutes les routes
// de cette classe. Donc chaque route ci-dessous commence par "/api/cours".
@RequestMapping("/api/cours")

// Génère le constructeur qui permet à Spring d'injecter automatiquement
// l'instance de CoursService dans ce contrôleur.
@RequiredArgsConstructor
public class CoursController {

    // Le service métier, utilisé pour exécuter les vraies opérations.
    private final CoursService coursService;

    // @GetMapping : répond aux requêtes GET sur "/api/cours" (sans rien après).
    // Besoin "Afficher" (tous les cours).
    @GetMapping
    public List<Cours> getAllCours() {
        return coursService.getAllCours();
    }

    // @GetMapping("/{id}") : répond aux requêtes GET sur "/api/cours/3" par exemple.
    // @PathVariable Long id : récupère le "3" dans l'URL et le met dans la variable id.
    // Besoin "Afficher" (un seul cours).
    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.getCoursById(id));
    }

    // @GetMapping("/recherche") : répond aux requêtes GET sur "/api/cours/recherche?nom=algo".
    // @RequestParam String nom : récupère la valeur après "?nom=" dans l'URL.
    // Besoin "Rechercher".
    @GetMapping("/recherche")
    public List<Cours> rechercher(@RequestParam String nom) {
        return coursService.rechercherParNom(nom);
    }

    // @PostMapping : répond aux requêtes POST sur "/api/cours".
    // @RequestBody Cours cours : transforme automatiquement le JSON envoyé
    // par React (dans le corps de la requête) en objet Java Cours.
    // Besoin "Ajouter".
    @PostMapping
    public ResponseEntity<Cours> createCours(@RequestBody Cours cours) {
        Cours nouveauCours = coursService.createCours(cours);
        // On renvoie le code 201 (CREATED), qui signifie "ressource créée avec succès",
        // accompagné du cours nouvellement créé (avec son id généré).
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCours);
    }

    // @PutMapping("/{id}") : répond aux requêtes PUT sur "/api/cours/3" par exemple.
    // Combine un id dans l'URL (le cours à modifier) et un corps JSON
    // (les nouvelles valeurs).
    // Besoin "Modifier".
    @PutMapping("/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours cours) {
        return ResponseEntity.ok(coursService.updateCours(id, cours));
    }

    // @DeleteMapping("/{id}") : répond aux requêtes DELETE sur "/api/cours/3".
    // Besoin "Supprimer".
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        // Code 204 (NO CONTENT) : signifie "succès, mais rien à renvoyer",
        // c'est la convention pour une suppression réussie.
        return ResponseEntity.noContent().build();
    }
}
