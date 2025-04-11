package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.feign.PassagerClient;
import com.example.Airport_Management.feign.ObjetPerduClient;
import com.example.Airport_Management.passager.Passager;
import com.itextpdf.text.DocumentException;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            logger.info("Requête reçue: {}", livraisonBagages);
            if (livraisonBagages.getObjetPerduIds() != null && !livraisonBagages.getObjetPerduIds().isEmpty()) {
                logger.info("Vérification des objets perdus: {}", livraisonBagages.getObjetPerduIds());
                for (Integer id : livraisonBagages.getObjetPerduIds()) {
                    logger.debug("Appel à objetPerduClient pour ID: {}", id);
                    objetPerduClient.getObjetPerduById(id);
                }
            }
            logger.info("Création de la livraison...");
            LivraisonBagages created = livraisonBagagesService.addLivraisonBagage(livraisonBagages);
            logger.info("Livraison créée avec succès: ID {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (FeignException.NotFound e) {
            logger.error("Objet perdu non trouvé: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (FeignException e) {
            logger.error("Erreur Feign: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue: {}", e.getMessage(), e);
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
                .map(livraisonBagagesService::mapToDTO)
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
        LivraisonBagagesDTO dto = livraisonBagagesService.mapToDTO(livraison);
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
    public ResponseEntity<Map<String, String>> deleteLivraisonBagage(@PathVariable("id") String id) {
        Map<String, String> response = new HashMap<>();
        String result = livraisonBagagesService.deleteLivraisonBagage(id);
        response.put("message", result);
        if ("livraison bagages supprimée".equals(result)) {
            logger.info("Livraison supprimée avec succès: ID {}", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        logger.warn("Livraison non trouvée pour suppression, ID: {}", id);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/receipt", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateDeliveryReceipt(@PathVariable("id") String id) {
        try {
            byte[] pdfContent = livraisonBagagesService.generateDeliveryReceipt(id);
            if (pdfContent == null) {
                logger.warn("Livraison non trouvée pour la génération du reçu, ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "recu-livraison-" + id + ".pdf");
            
            logger.info("Reçu PDF généré avec succès pour la livraison ID: {}", id);
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            logger.error("Erreur lors de la génération du PDF pour la livraison ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}