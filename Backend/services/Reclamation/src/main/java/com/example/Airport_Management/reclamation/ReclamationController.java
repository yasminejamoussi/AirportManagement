package com.example.Airport_Management.reclamation;

import com.example.Airport_Management.reclamation.PassagerDTO;
import com.example.Airport_Management.feign.PassagerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
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

    //@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@ResponseStatus(HttpStatus.CREATED)
   // public ResponseEntity<Reclamation> createReclamation(@Valid @RequestBody Reclamation reclamation) {
    //    return new ResponseEntity<>(reclamationService.addReclamation(reclamation), HttpStatus.OK);
    //}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Reclamation> createReclamation(
            @RequestPart("reclamation") String reclamationJson, // Reclamation en JSON sous forme de String
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convertir le JSON en objet Reclamation
            ObjectMapper objectMapper = new ObjectMapper();
            Reclamation reclamation = objectMapper.readValue(reclamationJson, Reclamation.class);

            // Gérer l’image si présente
            if (image != null && !image.isEmpty()) {
                // Convertir l’image en base64 (ou stocker sur disque/cloud selon ton choix)
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                reclamation.setImage(base64Image); // Suppose un champ 'image' dans Reclamation
            }

            // Sauvegarder la réclamation
            Reclamation savedReclamation = reclamationService.addReclamation(reclamation);
            return new ResponseEntity<>(savedReclamation, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la réclamation : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReclamation(@PathVariable("id") int id) {
        String result = reclamationService.deleteReclamation(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        if (result.equals("Reclamation supprimé")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Filtrage par statut
    @GetMapping("/filter/statut/{statut}")
    public ResponseEntity<List<ReclamationDTO>> getReclamationByStatut(@PathVariable("statut") String statutStr) {
        try {
            System.out.println("Statut reçu: " + statutStr);
            // Convertir la chaîne en minuscules et remplacer les tirets par des underscores
            String normalizedStatut = statutStr.toLowerCase().replace("-", "_");
            System.out.println("Statut normalisé: " + normalizedStatut);
            Statut statut = Statut.valueOf(normalizedStatut);
            System.out.println("Statut converti: " + statut);
            
            List<Reclamation> reclamations = reclamationService.getReclamationByStatut(statut);
            System.out.println("Nombre de réclamations trouvées: " + reclamations.size());
            
            List<ReclamationDTO> dtos = reclamations.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            System.err.println("Statut invalide : " + statutStr);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Tri par date
    @GetMapping("/sorted/date")
    public ResponseEntity<List<ReclamationDTO>> getAllSortedByDate() {
        List<Reclamation> reclamations = reclamationService.getAllSortedByDate();
        List<ReclamationDTO> dtos = reclamations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

@GetMapping("/search")
public ResponseEntity<Object> searchReclamations(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    try {
        System.out.println("Paramètres reçus - Type: " + type + ", Statut: " + statut + ", StartDate: " + startDate + ", EndDate: " + endDate);

        // Vérifier qu'au moins un paramètre est fourni
        if (type == null && statut == null && startDate == null && endDate == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Aucun paramètre de recherche fourni");
            System.err.println("Aucun paramètre de recherche fourni");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Conversion du type
        TypeReclamation typeReclamation = null;
        if (type != null && !type.trim().isEmpty()) {
            String normalizedType = type.toLowerCase().replace("-", "_"); // Normaliser en minuscules et remplacer - par _
            typeReclamation = TypeReclamation.valueOf(normalizedType); // Pas de toUpperCase()
            System.out.println("Type converti: " + typeReclamation);
        }

        // Conversion du statut
        Statut statutEnum = null;
        if (statut != null && !statut.trim().isEmpty()) {
            String normalizedStatut = statut.toLowerCase().replace("-", "_");
            statutEnum = Statut.valueOf(normalizedStatut); // Doit correspondre aux valeurs de l'enum (ex. "en_cours")
            System.out.println("Statut converti: " + statutEnum);
        }

        // Validation des dates
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La date de début ne peut pas être après la date de fin");
            System.err.println("La date de début ne peut pas être après la date de fin");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        List<Reclamation> reclamations = reclamationService.searchReclamations(typeReclamation, statutEnum, startDate, endDate);
        System.out.println("Nombre de réclamations trouvées: " + reclamations.size());

        List<ReclamationDTO> dtos = reclamations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Paramètre invalide (type ou statut) : " + e.getMessage());
        System.err.println("Paramètre invalide (type ou statut) : " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur interne lors de la recherche : " + e.getMessage());
        System.err.println("Erreur lors de la recherche : " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
                        return null;
                    }
                })
                .filter(passager -> passager != null)
                .collect(Collectors.toList())
                : Collections.emptyList();
        return new ReclamationDTO(reclamation, passagers);
    }
}