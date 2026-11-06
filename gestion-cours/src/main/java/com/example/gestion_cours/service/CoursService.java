package com.example.gestion_cours.service;

import com.example.gestion_cours.entity.Cours;
import com.example.gestion_cours.entity.Enseignant;
import com.example.gestion_cours.entity.Groupe;
import com.example.gestion_cours.entity.StatutCours;
import com.example.gestion_cours.repository.CoursRepository;
import com.example.gestion_cours.repository.EnseignantRepository;
import com.example.gestion_cours.repository.GroupeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursService {

    private final CoursRepository coursRepository;
    private final EnseignantRepository enseignantRepository;
    private final GroupeRepository groupeRepository;

    private static final List<String> HORAIRES_VALIDES = List.of(
            "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30",
            "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00",
            "08:30 - 10:30", "10:30 - 12:30", "14:00 - 16:00", "16:00 - 18:00"
    );

    private String traduireJourEnFrancais(DayOfWeek jourSemaine) {
        return switch (jourSemaine) {
            case MONDAY -> "Lundi";
            case TUESDAY -> "Mardi";
            case WEDNESDAY -> "Mercredi";
            case THURSDAY -> "Jeudi";
            case FRIDAY -> "Vendredi";
            case SATURDAY -> "Samedi";
            case SUNDAY -> "Dimanche";
        };
    }

    private LocalTime extraireDebut(String horaire) { return LocalTime.parse(horaire.split("-")[0].trim()); }
    private LocalTime extraireFin(String horaire) { return LocalTime.parse(horaire.split("-")[1].trim()); }

    private boolean horairesSeChevauchent(String horaire1, String horaire2) {
        LocalTime debut1 = extraireDebut(horaire1);
        LocalTime fin1 = extraireFin(horaire1);
        LocalTime debut2 = extraireDebut(horaire2);
        LocalTime fin2 = extraireFin(horaire2);
        return debut1.isBefore(fin2) && debut2.isBefore(fin1);
    }

    private void validerCours(Cours cours) {
        if (cours.getNom() == null || cours.getNom().isBlank()) {
            throw new RuntimeException("Le nom du cours est obligatoire");
        }
        if (cours.getCredits() <= 0) {
            throw new RuntimeException("Le nombre de crédits doit être supérieur à 0");
        }
        if (cours.getHoraire() == null || !HORAIRES_VALIDES.contains(cours.getHoraire())) {
            throw new RuntimeException(
                    "L'horaire est obligatoire et doit être l'un des créneaux suivants : " + HORAIRES_VALIDES);
        }
        if (cours.getDate() == null) {
            throw new RuntimeException("La date est obligatoire");
        }
        if (cours.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("La date ne peut pas être dans le passé : " + cours.getDate());
        }
        if (cours.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RuntimeException("La date ne peut pas être un dimanche : " + cours.getDate());
        }

        if (cours.getEnseignant() == null || cours.getEnseignant().getTelephone() == null
                || cours.getEnseignant().getTelephone().isBlank()) {
            throw new RuntimeException("Le téléphone de l'enseignant est obligatoire");
        }
        Enseignant enseignantTrouve = enseignantRepository.findById(cours.getEnseignant().getTelephone())
                .orElseThrow(() -> new RuntimeException(
                        "Aucun enseignant trouvé avec le téléphone " + cours.getEnseignant().getTelephone()));
        cours.setEnseignant(enseignantTrouve);

        if (cours.getGroupe() == null || cours.getGroupe().getId() == null) {
            throw new RuntimeException("Le groupe est obligatoire");
        }
        Groupe groupeTrouve = groupeRepository.findById(cours.getGroupe().getId())
                .orElseThrow(() -> new RuntimeException("Groupe introuvable avec l'id " + cours.getGroupe().getId()));
        cours.setGroupe(groupeTrouve);

        cours.setJour(traduireJourEnFrancais(cours.getDate().getDayOfWeek()));
    }

    private void verifierConflitHoraire(Cours cours, Long idActuel) {
        if (cours.getGroupe() == null || cours.getJour() == null || cours.getHoraire() == null) return;
        List<Cours> coursDuGroupeCeJour = coursRepository.findByGroupeAndJour(cours.getGroupe(), cours.getJour());
        boolean conflit = coursDuGroupeCeJour.stream()
                .filter(c -> !c.getId().equals(idActuel))
                .anyMatch(c -> horairesSeChevauchent(c.getHoraire(), cours.getHoraire()));
        if (conflit) {
            throw new RuntimeException("Ce groupe a déjà un cours le " + cours.getJour()
                    + " qui chevauche le créneau " + cours.getHoraire());
        }
    }

    private void verifierConflitEnseignant(Cours cours, Long idActuel) {
        if (cours.getEnseignant() == null || cours.getJour() == null || cours.getHoraire() == null) return;
        List<Cours> coursDeLEnseignantCeJour = coursRepository.findByEnseignantAndJour(cours.getEnseignant(), cours.getJour());
        boolean conflit = coursDeLEnseignantCeJour.stream()
                .filter(c -> !c.getId().equals(idActuel))
                .anyMatch(c -> horairesSeChevauchent(c.getHoraire(), cours.getHoraire()));
        if (conflit) {
            throw new RuntimeException("L'enseignant " + cours.getEnseignant().getNom() + " a déjà un cours le "
                    + cours.getJour() + " qui chevauche le créneau " + cours.getHoraire());
        }
    }

    // Vérifie que le cours appartient bien au professeur connecté,
    // avant de le laisser confirmer ou refuser.
    private void verifierProprietaire(Cours cours, String telephoneConnecte) {
        if (cours.getEnseignant() == null || !cours.getEnseignant().getTelephone().equals(telephoneConnecte)) {
            throw new RuntimeException("Ce cours ne vous appartient pas.");
        }
    }
    // Récupère tous les cours d'un professeur donné (son emploi du temps).
    public List<Cours> getMonEmploiDuTemps(String telephoneConnecte) {
        return coursRepository.findByEnseignant_Telephone(telephoneConnecte);
    }
    public List<Cours> getAllCours() { return coursRepository.findAll(); }

    public Cours getCoursById(Long id) {
        return coursRepository.findById(id).orElseThrow(() -> new RuntimeException("Cours introuvable avec l'id " + id));
    }

    public Cours createCours(Cours cours) {
        validerCours(cours);
        verifierConflitHoraire(cours, null);
        verifierConflitEnseignant(cours, null);
        cours.setStatut(StatutCours.EN_ATTENTE);
        return coursRepository.save(cours);
    }

    public Cours updateCours(Long id, Cours coursDetails) {
        validerCours(coursDetails);
        verifierConflitHoraire(coursDetails, id);
        verifierConflitEnseignant(coursDetails, id);
        Cours cours = getCoursById(id);
        cours.setNom(coursDetails.getNom());
        cours.setEnseignant(coursDetails.getEnseignant());
        cours.setGroupe(coursDetails.getGroupe());
        cours.setCredits(coursDetails.getCredits());
        cours.setJour(coursDetails.getJour());
        cours.setHoraire(coursDetails.getHoraire());
        cours.setDate(coursDetails.getDate());
        return coursRepository.save(cours);
    }

    public void deleteCours(Long id) {
        coursRepository.delete(getCoursById(id));
    }

    public List<Cours> rechercherParNom(String nom) {
        return coursRepository.findByNomContainingIgnoreCase(nom);
    }

    // Le professeur confirme un de ses cours.
    public Cours confirmerCours(Long id, String telephoneConnecte) {
        Cours cours = getCoursById(id);
        verifierProprietaire(cours, telephoneConnecte);
        cours.setStatut(StatutCours.CONFIRME);
        return coursRepository.save(cours);
    }

    // Le professeur refuse un de ses cours.
    public Cours refuserCours(Long id, String telephoneConnecte) {
        Cours cours = getCoursById(id);
        verifierProprietaire(cours, telephoneConnecte);
        cours.setStatut(StatutCours.REFUSE);
        return coursRepository.save(cours);
    }
}