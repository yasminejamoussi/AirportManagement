package com.example.Airport_Management.objetperdu;

import java.util.List;

public class ObjetPerduDTO {


    private ObjetPerdu objetPerdu;
    private List<PassagerDTO> passagers;


    public ObjetPerduDTO(ObjetPerdu objetPerdu, List<PassagerDTO> passagers) {
        this.objetPerdu = objetPerdu;
        this.passagers = passagers;
    }

    public ObjetPerdu getObjetPerdu() {
        return objetPerdu;
    }

    public void setObjetPerdu(ObjetPerdu objetPerdu) {
        this.objetPerdu = objetPerdu;
    }

    public List<PassagerDTO> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<PassagerDTO> passagers) {
        this.passagers = passagers;
    }
}
