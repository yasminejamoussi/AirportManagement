package com.example.Airport_Management.forum;

import com.example.Airport_Management.forum.ForumCreatedEvent;
import com.example.Airport_Management.feign.PassagerClient;
import feign.FeignException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
@RequestMapping("/forum")
public class ForumController {
    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);
    @Autowired
    private ForumService forumService;
    @Autowired
    private PassagerClient passagerClient;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    public ForumController(ForumService forumService, PassagerClient passagerClient, ApplicationEventPublisher eventPublisher) {
        this.forumService = forumService;
        this.passagerClient = passagerClient;
        this.eventPublisher = eventPublisher;
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Forum> createForum(@Valid @RequestBody Forum forum) {
        try {
            // Vérifier si la liste des IDs de passagers est vide ou nulle
            List<String> passagerIds = forum.getPassagerIds();
            if (passagerIds == null || passagerIds.isEmpty()) {
                logger.warn("Tentative de création d'un forum sans IDs de passagers");
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

            // Si tous les passagers existent, créer le forum
            Forum createdForum = forumService.addForum(forum);
            logger.info("Forum créé avec succès: ID {}", createdForum.getId());

            // Publier l'événement ForumCreatedEvent
            eventPublisher.publishEvent(new ForumCreatedEvent(this, createdForum));

            return ResponseEntity.status(HttpStatus.CREATED).body(createdForum);

        } catch (FeignException e) {
            logger.error("Erreur Feign lors de la vérification des passagers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création du forum: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ForumDTO>> getAll() {
        try {
            List<Forum> forums = forumService.getAll();
            System.out.println("Nombre de forums récupérées : " + forums.size());
            List<ForumDTO> dtos = forums.stream()
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
    public ResponseEntity<ForumDTO> getReclamationById(@PathVariable("id") int id) {
        Forum forum = forumService.getForumById(id);
        if (forum == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDTO(forum));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Forum> updateForum(@PathVariable(value = "id") int id,
                                                       @RequestBody Forum forum){
        return new ResponseEntity<>(forumService.updateForum(id, forum),
                HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> deleteForum(@PathVariable(value = "id") int id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", forumService.deleteForum(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private ForumDTO mapToDTO(Forum forum) {
        System.out.println("Mapping forum ID: " + forum.getId());
        List<PassagerDTO> passagers = (forum.getPassagerIds() != null && !forum.getPassagerIds().isEmpty())
                ? forum.getPassagerIds().stream()
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
        return new ForumDTO(forum, passagers);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Forum>> searchForums(@RequestParam String categorie) {
        return ResponseEntity.ok(forumService.getForumByCategorie(categorie));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Forum>> getAllForumsSorted(@RequestParam(defaultValue = "asc") String sort) {
        List<Forum> forums;
        if ("desc".equalsIgnoreCase(sort)) {
            forums = forumService.getAllSortedByCategorieDesc(); // Tri décroissant
        } else {
            forums = forumService.getAllSortedByCategorieAsc(); // Tri croissant (par défaut)
        }
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }
}
