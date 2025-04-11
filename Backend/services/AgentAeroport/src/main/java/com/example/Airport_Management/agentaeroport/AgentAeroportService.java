package com.example.Airport_Management.agentaeroport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AgentAeroportService {
    @Autowired
    private AgentAeroportRepository agentAeroportRepository;
    public AgentAeroport addAgentAeroport(AgentAeroport agentAeroport) {
        return agentAeroportRepository.save(agentAeroport);

    }

    public List<AgentAeroport> getAll() {
        return agentAeroportRepository.findAll();
    }

    public AgentAeroport updateAgentAeroport(int id, AgentAeroport newAgentAeroport) {
        if (agentAeroportRepository.findById(id).isPresent()) {

            AgentAeroport existingAgentAeroport = agentAeroportRepository.findById(id).get();
            existingAgentAeroport.setNom(newAgentAeroport.getNom());
            existingAgentAeroport.setPrenom(newAgentAeroport.getPrenom());
            existingAgentAeroport.setEmail(newAgentAeroport.getEmail());
            existingAgentAeroport.setHistorique_dossiers(newAgentAeroport.getHistorique_dossiers());
            existingAgentAeroport.setRole(newAgentAeroport.getRole());

            return agentAeroportRepository.save(existingAgentAeroport);
        } else
            return null;
    }

    public String deleteAgentAeroport(int id) {
        if (agentAeroportRepository.findById(id).isPresent()) {
            agentAeroportRepository.deleteById(id);
            return "objet perdu supprimé";
        } else
            return "objet perdu non supprimé";
    }

    public List<AgentAeroport> getAgentAeroportByNom(String nom) {
        return agentAeroportRepository.findByNomContainingIgnoreCase(nom);
    }
    public List<AgentAeroport> getAllSortedByNomAsc() {
        return agentAeroportRepository.findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    public List<AgentAeroport> getAllSortedByNomDesc() {
        return agentAeroportRepository.findAll(Sort.by(Sort.Direction.DESC, "nom"));
    }
}
