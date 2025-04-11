package com.example.Airport_Management.objetperdu;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class DeclarationDTO {
    private String description;
    private LocalDateTime dateDeclaration;
    private String statut; // ex: "EN COURS", "RÉSOLU"
    private String typeObjet;
}
