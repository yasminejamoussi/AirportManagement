package com.example.Airport_Management.agentaeroport;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/agentaeroport")
public class AgentAeroportController {
    @Autowired
    private AgentAeroportService agentAeroportService;
    private String title = "Hello, i'm the AgentAeroport Micro-Service";

    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AgentAeroport> createAgentAeroport(@Valid @RequestBody AgentAeroport agentAeroport) {
        return new ResponseEntity<>(agentAeroportService.addAgentAeroport(agentAeroport), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgentAeroport>> getAll() {
        return new ResponseEntity<>(agentAeroportService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AgentAeroport> updateAgentAeroport(@PathVariable(value = "id") int id,
                                                         @RequestBody AgentAeroport agentAeroport) {
        return new ResponseEntity<>(agentAeroportService.updateAgentAeroport(id, agentAeroport), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteAgentAeroport(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(agentAeroportService.deleteAgentAeroport(id), HttpStatus.OK);
    }
}
