package com.example.Airport_Management.livraisonbagages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LivraisonBagagesService {
    @Autowired
    private LivraisonBagagesRepository livraisonBagagesRepository;

    public LivraisonBagages addLivraisonBagage(LivraisonBagages livraisonBagages) {
        return livraisonBagagesRepository.save(livraisonBagages);
    }

    public List<LivraisonBagages> getAll() {
        return livraisonBagagesRepository.findAll();
    }

    public LivraisonBagages getLivraisonBagageById(String id) {
        return livraisonBagagesRepository.findById(id).orElse(null);
    }

    public LivraisonBagages updateLivraisonBagage(String id, LivraisonBagages livraisonBagages) {
        if (livraisonBagagesRepository.findById(id).isPresent()) {
            LivraisonBagages existingLivraisonBagages = livraisonBagagesRepository.findById(id).get();
            existingLivraisonBagages.setAdresse(livraisonBagages.getAdresse());
            existingLivraisonBagages.setDate_prevue(livraisonBagages.getDate_prevue());
            existingLivraisonBagages.setModeLivraison(livraisonBagages.getModeLivraison());
            existingLivraisonBagages.setStatut(livraisonBagages.getStatut());
            existingLivraisonBagages.setPassagerIds(livraisonBagages.getPassagerIds()); // Ajout pour mettre à jour les IDs
            return livraisonBagagesRepository.save(existingLivraisonBagages);
        } else {
            return null;
        }
    }

    public String deleteLivraisonBagage(String id) {
        if (livraisonBagagesRepository.findById(id).isPresent()) {
            livraisonBagagesRepository.deleteById(id);
            return "livraison bagages supprimée";
        } else {
            return "livraison bagages non supprimée";
        }
    }
}