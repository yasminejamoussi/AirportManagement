package com.example.Airport_Management.objetperdu;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ObjetPerdu {
    @Id
    @GeneratedValue
    private int  id;
    private String type_objet;
    private String  description;
    private String photo;
    private String lieu_decouverte;
    private Date date_decouverte;
    @Enumerated(EnumType.STRING)
    private Statut  statut;

    @ElementCollection
    private List<String> passagerIds = new ArrayList<>();


    public List<String> getPassagerIds() {
        return passagerIds;
    }

    public void setPassagerIds(List<String> passagerIds) {
        this.passagerIds = passagerIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_objet() {
        return type_objet;
    }

    public void setType_objet(String type_objet) {
        this.type_objet = type_objet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLieu_decouverte() {
        return lieu_decouverte;
    }

    public void setLieu_decouverte(String lieu_decouverte) {
        this.lieu_decouverte = lieu_decouverte;
    }

    public Date getDate_decouverte() {
        return date_decouverte;
    }

    public void setDate_decouverte(Date date_decouverte) {
        this.date_decouverte = date_decouverte;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}
