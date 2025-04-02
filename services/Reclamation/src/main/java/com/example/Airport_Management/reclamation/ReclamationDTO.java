package com.example.Airport_Management.reclamation;

import com.example.Airport_Management.reclamation.PassagerDTO;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class ReclamationDTO {
    private Reclamation reclamation;
    private List<PassagerDTO> passagers;

    public ReclamationDTO(Reclamation reclamation, List<PassagerDTO> passagers) {
        this.reclamation = reclamation;
        this.passagers = passagers;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    public List<PassagerDTO> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<PassagerDTO> passagers) {
        this.passagers = passagers;
    }
}