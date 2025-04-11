package com.example.Airport_Management.passager;

import jakarta.persistence.*;

import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
//import com.example.Airport_Management.objets_perdus.ObjetsPerdus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "passager")
public class Passager {
    @org.springframework.data.annotation.Id
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private int numero;
    private String notifications;
    @Enumerated(EnumType.STRING)
    private Type  type;
    private String password;
    @OneToMany(mappedBy = "passager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Declaration> historiqueDeclarations = new ArrayList<>();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Declaration> getHistoriqueDeclarations() {
        return historiqueDeclarations;
    }

    public void setHistoriqueDeclarations(List<Declaration> historiqueDeclarations) {
        this.historiqueDeclarations = historiqueDeclarations;
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

}
