package com.example.Airport_Management.reclamation;

import com.example.Airport_Management.reclamation.PassagerDTO;
import com.example.Airport_Management.feign.PassagerClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reclamation")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private PassagerClient passagerClient;

    private String title = "Hello, i'm the reclamation Micro-Service";

    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Reclamation> createReclamation(@Valid @RequestBody Reclamation reclamation) {
        return new ResponseEntity<>(reclamationService.addReclamation(reclamation), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReclamationDTO>> getAll() {
        try {
            List<Reclamation> reclamations = reclamationService.getAll();
            System.out.println("Nombre de réclamations récupérées : " + reclamations.size());
            List<ReclamationDTO> dtos = reclamations.stream()
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
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable("id") int id) {
        Reclamation reclamation = reclamationService.getReclamationById(id);
        if (reclamation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDTO(reclamation));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable(value = "id") int id,
                                                         @RequestBody Reclamation reclamation) {
        Reclamation updated = reclamationService.updateReclamation(id, reclamation);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteReclamation(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(reclamationService.deleteReclamation(id), HttpStatus.OK);
    }

    private ReclamationDTO mapToDTO(Reclamation reclamation) {
        System.out.println("Mapping reclamation ID: " + reclamation.getId());
        List<PassagerDTO> passagers = (reclamation.getPassagerIds() != null && !reclamation.getPassagerIds().isEmpty())
                ? reclamation.getPassagerIds().stream()
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
        return new ReclamationDTO(reclamation, passagers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Reclamation>> searchPassagers(@RequestParam String statut) {
        return ResponseEntity.ok(reclamationService.getReclamationByStatut(statut));
    }
}