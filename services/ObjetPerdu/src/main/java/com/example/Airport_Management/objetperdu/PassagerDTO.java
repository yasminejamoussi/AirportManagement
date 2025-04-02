package com.example.Airport_Management.objetperdu;

import java.util.ArrayList;
import java.util.List;

public class PassagerDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private int numero;
    private String notifications;
    private List<DeclarationDTO> historiqueDeclarations = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public List<DeclarationDTO> getHistoriqueDeclarations() {
        return historiqueDeclarations;
    }

    public void setHistoriqueDeclarations(List<DeclarationDTO> historiqueDeclarations) {
        this.historiqueDeclarations = historiqueDeclarations;
    }
}
