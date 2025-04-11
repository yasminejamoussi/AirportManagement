package com.example.Airport_Management.passager;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/passager")
@RequiredArgsConstructor
public class PassagerController {
    @Autowired
    private PassagerService passagerService;
    @Autowired
    private JwtService jwtService;
    public PassagerController(PassagerService passagerService, JwtService jwtService) {
        this.passagerService = passagerService;
        this.jwtService = jwtService;
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> createPassager(@RequestBody @Valid Passager passager) {
        Passager savedPassager = passagerService.addPassager(passager);

        // Générer le token pour le passager
        String token = jwtService.generateToken(savedPassager.getEmail());

        // Retourner le passager et le token
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passager ajouté avec succès");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            // Récupérer l'email et le mot de passe depuis le corps de la requête
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Vérifier que les champs ne sont pas null
            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email et mot de passe sont requis"));
            }

            // Récupérer le passager
            Optional<Passager> passagerOpt = passagerService.getPassagerByEmail(email);
            if (!passagerOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Utilisateur non trouvé"));
            }

            Passager passager = passagerOpt.get();
            // Comparaison du mot de passe
            if (!password.equals(passager.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Mot de passe incorrect"));
            }

            // Générer un nouveau token pour ce passager
            String token = jwtService.generateToken(passager.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login réussi");
            response.put("token", token); // Ajouter le token à la réponse
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'erreur dans les logs pour diagnostic
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur interne : " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Passager>> getAll() {
        return new ResponseEntity<>(passagerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passager> getPassagerById(@PathVariable("id") String id) {
        Passager passager = passagerService.getPassagerById(id); // Assurez-vous que cette méthode existe dans PassagerService
        return passager != null ? ResponseEntity.ok(passager) : ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Passager> updatePassager(@PathVariable("id") String id, @RequestBody Passager passager) {
        return new ResponseEntity<>(passagerService.updatePassager(id, passager), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePassager(@PathVariable("id") String id) {
        return new ResponseEntity<>(passagerService.deletePassager(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Passager>> searchPassagers(@RequestParam String nom) {
        return ResponseEntity.ok(passagerService.getPassagerByNom(nom));
    }

    @GetMapping("/me")
    public ResponseEntity<Passager> getCurrentPassager(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7); // Enlever "Bearer "
        try {
            Claims claims = jwtService.validateToken(token); // Vérifie la validité du token
            String email = claims.getSubject(); // Récupère l’email du token
            Optional<Passager> passagerOpt = passagerService.getPassagerByEmail(email);
            return passagerOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }

    @PutMapping(value = "/update-profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Passager updatedPassager) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token requis"));
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtService.validateToken(token);
            String email = claims.getSubject();
            Optional<Passager> passagerOpt = passagerService.getPassagerByEmail(email);

            if (!passagerOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Utilisateur non trouvé"));
            }

            Passager existingPassager = passagerOpt.get();
            // Mettre à jour les champs autorisés
            existingPassager.setNom(updatedPassager.getNom());
            existingPassager.setPrenom(updatedPassager.getPrenom());
            existingPassager.setNumero(updatedPassager.getNumero());
            existingPassager.setEmail(updatedPassager.getEmail());
            if (updatedPassager.getPassword() != null && !updatedPassager.getPassword().isEmpty()) {
                existingPassager.setPassword(updatedPassager.getPassword()); // À hacher si nécessaire
            }

            passagerService.updatePassager(existingPassager); // Appelle la nouvelle méthode
            return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token invalide ou expiré"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur interne : " + e.getMessage()));
        }
    }

}