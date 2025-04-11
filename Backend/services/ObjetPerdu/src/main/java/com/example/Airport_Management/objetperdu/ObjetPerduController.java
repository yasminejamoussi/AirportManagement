package com.example.Airport_Management.objetperdu;

import com.example.Airport_Management.feign.PassagerClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/objetperdu")
public class ObjetPerduController {
    private static final Logger logger = LoggerFactory.getLogger(ObjetPerduController.class);
    @Autowired
    private ObjetPerduService objetPerduService;

    @Autowired
    private PassagerClient passagerClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjetPerdu> createObjetPerdu(@Valid @RequestBody ObjetPerdu objetPerdu) {
        try {
            // Vérifier si la liste des IDs de passagers est vide ou nulle
            List<String> passagerIds = objetPerdu.getPassagerIds();
            if (passagerIds == null || passagerIds.isEmpty()) {
                logger.warn("Tentative de création d'un objet perdu sans IDs de passagers");
                return ResponseEntity.badRequest().body(null);
            }

            // Vérifier l'existence de chaque passager dans la liste
            for (String passagerId : passagerIds) {
                if (passagerId == null || passagerId.isEmpty()) {
                    logger.warn("ID de passager invalide (null ou vide) dans la liste");
                    return ResponseEntity.badRequest().body(null);
                }

                logger.debug("Vérification de l'existence du passager avec ID: {}", passagerId);
                try {
                    PassagerDTO passager = passagerClient.getPassagerById(passagerId);
                    if (passager == null) {
                        logger.warn("Passager avec ID {} n'existe pas", passagerId);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                } catch (FeignException.NotFound e) {
                    logger.warn("Passager avec ID {} n'existe pas: {}", passagerId, e.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }

            // Si tous les passagers existent, créer l'objet perdu
            ObjetPerdu createdObjetPerdu = objetPerduService.addObjetPerdu(objetPerdu);
            logger.info("Objet perdu créé avec succès: ID {}", createdObjetPerdu.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObjetPerdu);

        } catch (FeignException e) {
            logger.error("Erreur Feign lors de la vérification des passagers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de l'objet perdu: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
   /* @RequestMapping
    public ResponseEntity<List<ObjetPerdu>> getAll() {
        return new ResponseEntity<>(objetPerduService.getAll(), HttpStatus.OK);
    }*/
   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<ObjetPerduDTO>> getAll() {
       try {
           List<ObjetPerdu> objetPerdus = objetPerduService.getAll();
           System.out.println("Nombre de réclamations récupérées : " + objetPerdus.size());
           List<ObjetPerduDTO> dtos = objetPerdus.stream()
                   .map(this::mapToDTO)
                   .collect(Collectors.toList());
           System.out.println("DTOs générés : " + dtos.size());
           return new ResponseEntity<>(dtos, HttpStatus.OK);
       } catch (Exception e) {
           System.err.println("Erreur dans getAll : " + e.getMessage());
           e.printStackTrace();
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
    @GetMapping("/{id}")
    public ResponseEntity<ObjetPerduDTO> getObjetPerduById(@PathVariable("id") int id) {
        ObjetPerdu objetPerdu = objetPerduService.getObjetPerduById(id);
        if (objetPerdu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDTO(objetPerdu));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ObjetPerdu> updateObjetPerdu(@PathVariable(value = "id") int id,
                                                   @RequestBody ObjetPerdu objetPerdu){
        return new ResponseEntity<>(objetPerduService.updateObjetPerdu(id, objetPerdu),
                HttpStatus.OK);
    }
@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<Map<String, String>> deleteObjetPerdu(@PathVariable(value = "id") int id) {
    Map<String, String> response = new HashMap<>();
    String result = objetPerduService.deleteObjetPerdu(id);
    response.put("message", result);
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    private ObjetPerduDTO mapToDTO(ObjetPerdu objetPerdu) {
        System.out.println("Mapping reclamation ID: " + objetPerdu.getId());
        List<PassagerDTO> passagers = (objetPerdu.getPassagerIds() != null && !objetPerdu.getPassagerIds().isEmpty())
                ? objetPerdu.getPassagerIds().stream()
                .map(id -> {
                    try {
                        System.out.println("Appel Feign pour passager ID: " + id);
                        PassagerDTO passager = passagerClient.getPassagerById(id);
                        System.out.println("Passager récupéré: " + (passager != null ? passager.toString() : "null"));
                        return passager;
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la récupération du passager " + id + ": " + e.getMessage());
                        return null; // Retourne null pour cet ID, continue avec les autres
                    }
                })
                .filter(passager -> passager != null)
                .collect(Collectors.toList())
                : Collections.emptyList();
        return new ObjetPerduDTO(objetPerdu, passagers);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ObjetPerdu>> searchObjetsPerdus(@RequestParam String type_objet) {
        return ResponseEntity.ok(objetPerduService.getObjetPerduByTypeObjet(type_objet));
    }
    @GetMapping("/sorted")
    public ResponseEntity<List<ObjetPerdu>> getAllObjetPerdusSorted(@RequestParam(defaultValue = "asc") String sort) {
        List<ObjetPerdu> objetPerdus;
        if ("desc".equalsIgnoreCase(sort)) {
            objetPerdus = objetPerduService.getAllSortedByTypeDesc();
        } else {
            objetPerdus = objetPerduService.getAllSortedByTypeAsc();
        }
        return new ResponseEntity<>(objetPerdus, HttpStatus.OK);
    }
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatisticsByTypeObjet() {
        try {
            Map<String, Long> statistics = objetPerduService.getStatisticsByTypeObjet();
            if (statistics.isEmpty()) {
                logger.info("Aucune statistique disponible : aucune donnée d'objets perdus trouvée.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.info("Statistiques par type d'objet récupérées avec succès : {}", statistics);
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques : {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
