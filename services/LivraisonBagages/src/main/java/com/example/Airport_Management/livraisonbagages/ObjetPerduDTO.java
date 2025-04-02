package com.example.Airport_Management.livraisonbagages;

import java.util.Date;
import java.util.List;

public class ObjetPerduDTO {
    private ObjetPerduDetails objetPerdu;
    private List<PassagerDTO> passagers;

    public ObjetPerduDTO() {}

    public ObjetPerduDTO(ObjetPerduDetails objetPerdu, List<PassagerDTO> passagers) {
        this.objetPerdu = objetPerdu;
        this.passagers = passagers;
    }

    public ObjetPerduDetails getObjetPerdu() {
        return objetPerdu;
    }

    public void setObjetPerdu(ObjetPerduDetails objetPerdu) {
        this.objetPerdu = objetPerdu;
    }

    public List<PassagerDTO> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<PassagerDTO> passagers) {
        this.passagers = passagers;
    }

    // Classe imbriquée pour les détails de l'objet perdu
    public static class ObjetPerduDetails {
        private int id;
        private String type_objet;
        private String description;
        private String photo;
        private String lieu_decouverte;
        private Date date_decouverte;
        private String statut;

        public ObjetPerduDetails() {}

        // Getters et Setters
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

        public String getStatut() {
            return statut;
        }

        public void setStatut(String statut) {
            this.statut = statut;
        }
    }
}