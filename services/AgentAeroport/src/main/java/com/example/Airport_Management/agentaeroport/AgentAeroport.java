package com.example.Airport_Management.agentaeroport;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class AgentAeroport {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String historique_dossiers;

    @Enumerated(EnumType.STRING)
    private Role  role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHistorique_dossiers() {
        return historique_dossiers;
    }

    public void setHistorique_dossiers(String historique_dossiers) {
        this.historique_dossiers = historique_dossiers;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
