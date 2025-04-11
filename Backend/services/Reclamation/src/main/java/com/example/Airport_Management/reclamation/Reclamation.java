package com.example.Airport_Management.reclamation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(length = 1000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_soumission;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_resolution;

    @Enumerated(EnumType.STRING)
    private TypeReclamation typeReclamation;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.en_cours;

    @ElementCollection
    private List<String> passagerIds = new ArrayList<>();

    @Lob // Pour stocker une image en base64 ou bytes
    private String image;

    // Getters et setters
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate_soumission() {
        return date_soumission;
    }

    public void setDate_soumission(LocalDate date_soumission) {
        this.date_soumission = date_soumission;
    }

    public LocalDate getDate_resolution() {
        return date_resolution;
    }

    public void setDate_resolution(LocalDate date_resolution) {
        this.date_resolution = date_resolution;
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