package com.example.Airport_Management.passager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passager")
@RequiredArgsConstructor
public class PassagerController {
    @Autowired
    private PassagerService passagerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Passager> createPassager(@RequestBody @Valid Passager passager) {
        return ResponseEntity.ok(passagerService.addPassager(passager));
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
}