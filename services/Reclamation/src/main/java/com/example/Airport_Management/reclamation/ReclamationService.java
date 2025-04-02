package com.example.Airport_Management.reclamation;

import com.example.Airport_Management.feign.PassagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Service
public class ReclamationService {
    @Autowired
    private ReclamationRepository reclamationRepository;

    public Reclamation addReclamation(Reclamation reclamation) {
        if (reclamation.getDate_soumission() == null) {
            reclamation.setDate_soumission(LocalDate.now()); // Date actuelle
        }
        return reclamationRepository.save(reclamation);
    }

    public List<Reclamation> getAll() {return reclamationRepository.findAll();
    }
    public Reclamation getReclamationById(int id) {
        return reclamationRepository.findById(id).orElse(null);
    }

    public Reclamation updateReclamation(int id, Reclamation newReclamation) {
        if (reclamationRepository.findById(id).isPresent()) {

            Reclamation existingReclamation = reclamationRepository.findById(id).get();
            existingReclamation.setTypeReclamation(newReclamation.getTypeReclamation());
            existingReclamation.setDate_resolution(newReclamation.getDate_resolution());
            existingReclamation.setDate_soumission(newReclamation.getDate_soumission());
            existingReclamation.setStatut(newReclamation.getStatut());

            return reclamationRepository.save(existingReclamation);
        } else
            return null;
    }

    public String deleteReclamation(int id) {
        if (reclamationRepository.findById(id).isPresent()) {
            reclamationRepository.deleteById(id);
            return "Reclamation supprimé";
        } else
            return "Reclamation non supprimé";
    }
    public List<Reclamation> getReclamationByStatut(String statut) {
        return reclamationRepository.findByStatutContainingIgnoreCase(statut);

    }
}