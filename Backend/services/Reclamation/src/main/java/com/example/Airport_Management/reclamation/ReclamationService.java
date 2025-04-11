package com.example.Airport_Management.reclamation;

import com.example.Airport_Management.feign.PassagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class ReclamationService {
    private final ReclamationRepository reclamationRepository;

    @Autowired
    private final JavaMailSender mailSender;

    public ReclamationService(ReclamationRepository reclamationRepository, JavaMailSender mailSender) {
        this.reclamationRepository = reclamationRepository;
        this.mailSender = mailSender;
    }

    @Autowired
    private PassagerClient passagerClient;

    @Transactional
    public Reclamation addReclamation(Reclamation reclamation) {
        try {
            // Initialisation des valeurs par défaut
            if (reclamation.getDate_soumission() == null) {
                reclamation.setDate_soumission(LocalDate.now());
            }

            // Force le statut initial à "en_cours" et date_resolution à null
            reclamation.setStatut(Statut.en_cours);
            reclamation.setDate_resolution(null);

            validateReclamation(reclamation);

            if (reclamation.getPassagerIds() != null) {
                for (String passagerId : reclamation.getPassagerIds()) {
                    try {
                        passagerClient.getPassagerById(passagerId);
                    } catch (Exception e) {
                        throw new RuntimeException("Passager avec l'ID " + passagerId + " n'existe pas");
                    }
                }
            }

            Reclamation savedReclamation = reclamationRepository.save(reclamation);

            //  Envoyer un email au passager concerné
            sendNotificationEmail(savedReclamation, "Confirmation de votre réclamation");

            return savedReclamation;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la réclamation: " + e.getMessage());
        }
    }

    private void validateReclamation(Reclamation reclamation) {
        if (reclamation == null) {
            throw new RuntimeException("La réclamation ne peut pas être null");
        }
        if (reclamation.getTypeReclamation() == null) {
            throw new RuntimeException("Le type de réclamation est obligatoire");
        }
        if (reclamation.getStatut() == null) {
            throw new RuntimeException("Le statut est obligatoire");
        }
        if (reclamation.getDescription() == null || reclamation.getDescription().trim().isEmpty()) {
            throw new RuntimeException("La description est obligatoire");
        }
        if (reclamation.getDate_resolution() != null &&
                reclamation.getDate_soumission() != null &&
                reclamation.getDate_resolution().isBefore(reclamation.getDate_soumission())) {
            throw new RuntimeException("La date de résolution ne peut pas être antérieure à la date de soumission");
        }
    }

    public List<Reclamation> getAll() {
        return reclamationRepository.findAll();
    }

    public Reclamation getReclamationById(int id) {
        return reclamationRepository.findById(id).orElse(null);
    }

    public Reclamation updateReclamation(int id, Reclamation newReclamation) {
        if (reclamationRepository.findById(id).isPresent()) {
            Reclamation existingReclamation = reclamationRepository.findById(id).get();
            existingReclamation.setTypeReclamation(newReclamation.getTypeReclamation());
            existingReclamation.setDescription(newReclamation.getDescription());
            existingReclamation.setDate_soumission(newReclamation.getDate_soumission());

            // Gestion automatique de la date de résolution selon le statut
            if (newReclamation.getStatut() != existingReclamation.getStatut()) {
                if (newReclamation.getStatut() == Statut.traité || newReclamation.getStatut() == Statut.refusé) {
                    existingReclamation.setDate_resolution(LocalDate.now());
                } else if (newReclamation.getStatut() == Statut.en_cours) {
                    existingReclamation.setDate_resolution(null);
                }
            }

            existingReclamation.setStatut(newReclamation.getStatut());

            Reclamation updatedReclamation = reclamationRepository.save(existingReclamation);
            try {
                sendNotificationEmail(updatedReclamation, "Réclamation mise à jour");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            }
            return updatedReclamation;
        }
        return null;
    }

    public String deleteReclamation(int id) {
        if (reclamationRepository.findById(id).isPresent()) {
            reclamationRepository.deleteById(id);
            return "Reclamation supprimé";
        }
        return "Reclamation non supprimé";
    }

    // Filtrage par statut
    public List<Reclamation> getReclamationByStatut(Statut statut) {
        System.out.println("Recherche par statut - Statut: " + statut);
        List<Reclamation> result = reclamationRepository.findByStatut(statut);
        System.out.println("Nombre de réclamations trouvées: " + result.size());
        result.forEach(r -> System.out.println("Réclamation trouvée - ID: " + r.getId() +
                ", Type: " + r.getTypeReclamation() +
                ", Statut: " + r.getStatut()));
        return result;
    }

    // Méthode de tri par date
    public List<Reclamation> getAllSortedByDate() {
        return reclamationRepository.findAllOrderByDateSoumissionDesc();
    }

    // Méthodes de recherche avancée
    public List<Reclamation> searchReclamations(TypeReclamation type, Statut statut, LocalDate startDate, LocalDate endDate) {
        System.out.println("Recherche avec paramètres - Type: " + type + ", Statut: " + statut + ", StartDate: " + startDate + ", EndDate: " + endDate);

        List<Reclamation> result;

        if (type != null && statut != null && startDate != null && endDate != null) {
            result = reclamationRepository.findByTypeReclamationAndStatutAndDate_soumissionBetween(type, statut, startDate, endDate);
            System.out.println("Résultat recherche complète: " + result.size() + " réclamations");
        } else if (type != null && statut != null) {
            result = reclamationRepository.findByTypeReclamationAndStatut(type, statut);
            System.out.println("Résultat recherche type et statut: " + result.size() + " réclamations");
        } else if (type != null) {
            result = reclamationRepository.findByTypeReclamation(type);
            System.out.println("Résultat recherche type: " + result.size() + " réclamations");
        } else if (statut != null) {
            result = reclamationRepository.findByStatut(statut);
            System.out.println("Résultat recherche statut: " + result.size() + " réclamations");
        } else if (startDate != null && endDate != null) {
            // Vérifier que les dates sont valides
            if (startDate.isAfter(endDate)) {
                System.out.println("La date de début est après la date de fin");
                return Collections.emptyList();
            }
            result = reclamationRepository.findByDate_soumissionBetween(startDate, endDate);
            System.out.println("Résultat recherche date: " + result.size() + " réclamations");
        } else {
            result = reclamationRepository.findAll();
            System.out.println("Résultat recherche sans filtre: " + result.size() + " réclamations");
        }

        // Afficher les détails des réclamations trouvées
        result.forEach(r -> System.out.println("Réclamation trouvée - ID: " + r.getId() +
                ", Type: " + r.getTypeReclamation() +
                ", Statut: " + r.getStatut() +
                ", Date soumission: " + r.getDate_soumission() +
                ", Date résolution: " + r.getDate_resolution()));

        return result;
    }

    private void sendNotificationEmail(Reclamation reclamation, String subject) {
        try {
            if (reclamation.getPassagerIds() == null || reclamation.getPassagerIds().isEmpty()) {
                System.err.println("Aucun passager associé à cette réclamation.");
                return;
            }

            // Récupérer le premier passager lié à la réclamation via Feign Client
            String passagerId = reclamation.getPassagerIds().get(0);
            PassagerDTO passager = passagerClient.getPassagerById(passagerId);

            if (passager == null || passager.getEmail() == null || passager.getEmail().isEmpty()) {
                System.err.println("Impossible d'envoyer l'email : Passager introuvable ou sans email.");
                return;
            }

            // Préparer l'email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(passager.getEmail()); // ✅ Envoi au passager
            message.setSubject(subject);
            message.setText("Bonjour " + passager.getPrenom() + ",\n\n" +
                    "Votre réclamation a bien été enregistrée.\n\n" +
                    "Détails de la réclamation :\n" +
                    "ID: " + reclamation.getId() + "\n" +
                    "Type: " + reclamation.getTypeReclamation() + "\n" +
                    "Statut: " + reclamation.getStatut() + "\n" +
                    "Date de soumission: " + reclamation.getDate_soumission() + "\n\n" +
                    "Nous vous contacterons dès que possible.\n\nCordialement,\nL'équipe de gestion des réclamations.");

            mailSender.send(message);
            System.out.println("✅ Email envoyé à " + passager.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

}