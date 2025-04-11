package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.passager.Passager;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class LivraisonBagages {
    @org.springframework.data.annotation.Id
    private String id;

    private String adresse;
    private Date date_prevue;


    @Enumerated(EnumType.STRING)
    private ModeLivraison  modeLivraison;
    @Enumerated(EnumType.STRING)
    private Statut  statut;


    private List<String> passagerIds = new ArrayList<>();
    private List<Integer> objetPerduIds = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getObjetPerduIds() {
        return objetPerduIds;
    }

    public void setObjetPerduIds(List<Integer> objetPerduIds) {
        this.objetPerduIds = objetPerduIds;
    }

    public List<String> getPassagerIds() {
        return passagerIds;
    }

    public void setPassagerIds(List<String> passagerIds) {
        this.passagerIds = passagerIds;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDate_prevue() {
        return date_prevue;
    }

    public void setDate_prevue(Date date_prevue) {
        this.date_prevue = date_prevue;
    }

    public ModeLivraison getModeLivraison() {
        return modeLivraison;
    }

    public void setModeLivraison(ModeLivraison modeLivraison) {
        this.modeLivraison = modeLivraison;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}
