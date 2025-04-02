package com.example.Airport_Management.reclamation;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class DeclarationDTO {
    private String description;
    private LocalDateTime dateDeclaration;
    private String statut; // ex: "EN COURS", "RÉSOLU"
    private String typeObjet;
}
