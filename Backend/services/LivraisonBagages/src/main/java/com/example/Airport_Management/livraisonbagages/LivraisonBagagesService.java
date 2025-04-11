package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.feign.ObjetPerduClient;
import com.example.Airport_Management.feign.PassagerClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import com.itextpdf.text.DocumentException;
import com.example.Airport_Management.passager.Passager;


@Service
@Slf4j
public class LivraisonBagagesService {
    @Autowired
    private LivraisonBagagesRepository livraisonBagagesRepository;

    @Autowired
    private PassagerClient passagerClient;

    @Autowired
    private ObjetPerduClient objetPerduClient;

    @Autowired
    private PDFGenerator pdfGenerator;

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

    public LivraisonBagagesDTO mapToDTO(LivraisonBagages livraison) {
        List<Passager> passagers = Collections.emptyList();
        List<ObjetPerduDTO> objetPerdus = Collections.emptyList();

        try {
            // Récupération des passagers
            if (livraison.getPassagerIds() != null && !livraison.getPassagerIds().isEmpty()) {
                passagers = livraison.getPassagerIds().stream()
                        .map(id -> {
                            try {
                                log.debug("Récupération du passager ID: {}", id);
                                Passager passager = passagerClient.getPassagerById(id);
                                log.debug("Passager récupéré: {}", passager);
                                return passager;
                            } catch (FeignException.NotFound e) {
                                log.warn("Passager non trouvé pour ID: {}", id);
                                return null;
                            } catch (FeignException e) {
                                log.error("Erreur Feign pour passager ID {}: {}", id, e.getMessage());
                                return null;
                            }
                        })
                        .filter(passager -> passager != null)
                        .collect(Collectors.toList());
            }

            // Récupération des objets perdus
            if (livraison.getObjetPerduIds() != null && !livraison.getObjetPerduIds().isEmpty()) {
                objetPerdus = livraison.getObjetPerduIds().stream()
                        .map(id -> {
                            try {
                                log.debug("Récupération de l'objet perdu ID: {}", id);
                                ObjetPerduDTO objet = objetPerduClient.getObjetPerduById(id);
                                log.debug("Objet perdu récupéré: {}", objet);
                                return objet;
                            } catch (FeignException.NotFound e) {
                                log.warn("Objet perdu non trouvé pour ID: {}", id);
                                return null;
                            } catch (FeignException e) {
                                log.error("Erreur Feign pour objet perdu ID {}: {}", id, e.getMessage());
                                return null;
                            }
                        })
                        .filter(objet -> objet != null)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Erreur lors de la conversion en DTO pour livraison ID {}: {}", livraison.getId(), e.getMessage(), e);
        }

        return new LivraisonBagagesDTO(livraison, objetPerdus, passagers);
    }

    public byte[] generateDeliveryReceipt(String id) throws DocumentException {
        LivraisonBagages livraison = getLivraisonBagageById(id);
        if (livraison == null) {
            return null;
        }
        
        LivraisonBagagesDTO livraisonDTO = mapToDTO(livraison);
        return pdfGenerator.generateDeliveryReceipt(livraisonDTO);
    }
}