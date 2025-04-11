package com.example.Airport_Management.agentaeroport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentAeroportRepository extends JpaRepository<AgentAeroport , Integer> {
    Optional<AgentAeroport> findById(int id);

    void deleteById(int id);
    List<AgentAeroport> findByNomContainingIgnoreCase(String nom);
}
