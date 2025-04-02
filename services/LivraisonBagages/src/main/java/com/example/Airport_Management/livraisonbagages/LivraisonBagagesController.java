package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.feign.PassagerClient;
import com.example.Airport_Management.feign.ObjetPerduClient;
import com.example.Airport_Management.passager.Passager;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livraisonbagage")
@RequiredArgsConstructor
public class LivraisonBagagesController {
    private static final Logger logger = LoggerFactory.getLogger(LivraisonBagagesController.class);
    @Autowired
    private LivraisonBagagesService livraisonBagagesService;

    @Autowired
    private PassagerClient passagerClient;

    @Autowired
    private ObjetPerduClient objetPerduClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LivraisonBagages> createLivraisonBagage(@RequestBody @Valid LivraisonBagages livraisonBagages) {
        try {
            // Vérification des objets perdus avant création
            if (livraisonBagages.getObjetPerduIds() != null && !livraisonBagages.getObjetPerduIds().isEmpty()) {
                for (Integer id : livraisonBagages.getObjetPerduIds()) {
                    objetPerduClient.getObjetPerduById(id); // Vérifie l'existence
                }
            }
            LivraisonBagages created = livraisonBagagesService.addLivraisonBagage(livraisonBagages);
            logger.info("Livraison créée avec succès: ID {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (FeignException.NotFound e) {
            logger.error("Objet perdu non trouvé lors de la création: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (FeignException e) {
            logger.error("Erreur Feign lors de la création: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LivraisonBagagesDTO>> getAll() {
        List<LivraisonBagages> livraisons = livraisonBagagesService.getAll();
        if (livraisons.isEmpty()) {
            logger.info("Aucune livraison trouvée");
            return ResponseEntity.noContent().build();
        }
        List<LivraisonBagagesDTO> dtos = livraisons.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        logger.info("Nombre de livraisons récupérées: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LivraisonBagagesDTO> getLivraisonBagageById(@PathVariable("id") String id) {
        LivraisonBagages livraison = livraisonBagagesService.getLivraisonBagageById(id);
        if (livraison == null) {
            logger.warn("Livraison non trouvée pour ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        LivraisonBagagesDTO dto = mapToDTO(livraison);
        logger.info("Livraison récupérée avec succès pour ID: {}", id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LivraisonBagages> updateLivraisonBagage(
            @PathVariable("id") String id,
            @RequestBody @Valid LivraisonBagages livraisonBagages) {
        try {
            if (livraisonBagages.getObjetPerduIds() != null) {
                for (Integer objetId : livraisonBagages.getObjetPerduIds()) {
                    objetPerduClient.getObjetPerduById(objetId); // Vérifie l'existence
                }
            }
            LivraisonBagages updated = livraisonBagagesService.updateLivraisonBagage(id, livraisonBagages);
            if (updated == null) {
                logger.warn("Livraison non trouvée pour mise à jour, ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Livraison mise à jour avec succès: ID {}", id);
            return ResponseEntity.ok(updated);
        } catch (FeignException.NotFound e) {
            logger.error("Objet perdu non trouvé lors de la mise à jour: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (FeignException e) {
            logger.error("Erreur Feign lors de la mise à jour: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteLivraisonBagage(@PathVariable("id") String id) {
        String result = livraisonBagagesService.deleteLivraisonBagage(id);
        if ("livraison bagages supprimée".equals(result)) {
            logger.info("Livraison supprimée avec succès: ID {}", id);
            return ResponseEntity.ok(result);
        }
        logger.warn("Livraison non trouvée pour suppression, ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    private LivraisonBagagesDTO mapToDTO(LivraisonBagages livraison) {
        List<Passager> passagers = Collections.emptyList();
        List<ObjetPerduDTO> objetPerdus = Collections.emptyList();

        try {
            // Récupération des passagers
            if (livraison.getPassagerIds() != null && !livraison.getPassagerIds().isEmpty()) {
                passagers = livraison.getPassagerIds().stream()
                        .map(id -> {
                            try {
                                logger.debug("Récupération du passager ID: {}", id);
                                Passager passager = passagerClient.getPassagerById(id);
                                logger.debug("Passager récupéré: {}", passager);
                                return passager;
                            } catch (FeignException.NotFound e) {
                                logger.warn("Passager non trouvé pour ID: {}", id);
                                return null;
                            } catch (FeignException e) {
                                logger.error("Erreur Feign pour passager ID {}: {}", id, e.getMessage());
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
                                logger.debug("Récupération de l'objet perdu ID: {}", id);
                                ObjetPerduDTO objet = objetPerduClient.getObjetPerduById(id);
                                logger.debug("Objet perdu récupéré: {}", objet);
                                return objet;
                            } catch (FeignException.NotFound e) {
                                logger.warn("Objet perdu non trouvé pour ID: {}", id);
                                return null;
                            } catch (FeignException e) {
                                logger.error("Erreur Feign pour objet perdu ID {}: {}", id, e.getMessage());
                                return null;
                            }
                        })
                        .filter(objet -> objet != null)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la conversion en DTO pour livraison ID {}: {}", livraison.getId(), e.getMessage(), e);
        }

        return new LivraisonBagagesDTO(livraison, objetPerdus, passagers);
    }
}