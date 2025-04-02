package com.example.Airport_Management.objetperdu;

import com.example.Airport_Management.feign.PassagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/objetperdu")
public class ObjetPerduController {

    @Autowired
    private ObjetPerduService objetPerduService;

    @Autowired
    private PassagerClient passagerClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ObjetPerdu> createObjetPerdu(@RequestBody ObjetPerdu objetPerdu) {
        return new ResponseEntity<>(objetPerduService.addObjetPerdu(objetPerdu), HttpStatus.OK);
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
    public ResponseEntity<String> deleteObjetPerdu(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(objetPerduService.deleteObjetPerdu(id), HttpStatus.OK);
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
}
