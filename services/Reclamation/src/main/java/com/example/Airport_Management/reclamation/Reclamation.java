package com.example.Airport_Management.reclamation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Reclamation {
    @Id
    @GeneratedValue
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_resolution ;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_soumission ;

    @Enumerated(EnumType.STRING)
    private TypeReclamation  typeReclamation;

    @Enumerated(EnumType.STRING)
    private Statut  statut;

    @ElementCollection
    private List<String> passagerIds = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate_resolution() {
        return date_resolution;
    }

    public void setDate_resolution(LocalDate date_resolution) {
        this.date_resolution = date_resolution;
    }

    public LocalDate getDate_soumission() {
        return date_soumission;
    }

    public void setDate_soumission(LocalDate date_soumission) {
        this.date_soumission = date_soumission;
    }

    public TypeReclamation getTypeReclamation() {
        return typeReclamation;
    }

    public void setTypeReclamation(TypeReclamation typeReclamation) {
        this.typeReclamation = typeReclamation;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public List<String> getPassagerIds() {
        return passagerIds;
    }

    public void setPassagerIds(List<String> passagerIds) {
        this.passagerIds = passagerIds;
    }
}
