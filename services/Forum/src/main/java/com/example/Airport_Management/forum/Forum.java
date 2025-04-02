package com.example.Airport_Management.forum;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Forum {
    @Id
    @GeneratedValue
    private int id;
    private String contenu ;
    private String categorie ;
    private Date date_publication ;
    private Date derniere_mise_a_jour ;
    private int nombre_vue ;
    private int nombre_reponses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Date getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(Date date_publication) {
        this.date_publication = date_publication;
    }

    public Date getDerniere_mise_a_jour() {
        return derniere_mise_a_jour;
    }

    public void setDerniere_mise_a_jour(Date derniere_mise_a_jour) {
        this.derniere_mise_a_jour = derniere_mise_a_jour;
    }

    public int getNombre_vue() {
        return nombre_vue;
    }

    public void setNombre_vue(int nombre_vue) {
        this.nombre_vue = nombre_vue;
    }

    public int getNombre_reponses() {
        return nombre_reponses;
    }

    public void setNombre_reponses(int nombre_reponses) {
        this.nombre_reponses = nombre_reponses;
    }
}
