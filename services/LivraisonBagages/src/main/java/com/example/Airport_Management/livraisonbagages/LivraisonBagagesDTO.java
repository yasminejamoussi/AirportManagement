package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.passager.Passager;

import java.util.List;

public class LivraisonBagagesDTO {
    private LivraisonBagages livraisonBagages;
    private List<ObjetPerduDTO> objetPerdus;
    private List<Passager> passagers;

    public LivraisonBagagesDTO(LivraisonBagages livraisonBagages, List<ObjetPerduDTO> objetPerdus, List<Passager> passagers) {
        this.livraisonBagages = livraisonBagages;
        this.objetPerdus = objetPerdus;
        this.passagers = passagers;
    }

    public List<Passager> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<Passager> passagers) {
        this.passagers = passagers;
    }

    public LivraisonBagages getLivraisonBagages() {
        return livraisonBagages;
    }

    public void setLivraisonBagages(LivraisonBagages livraisonBagages) {
        this.livraisonBagages = livraisonBagages;
    }

    public List<ObjetPerduDTO> getObjetPerdus() {
        return objetPerdus;
    }

    public void setObjetPerdus(List<ObjetPerduDTO> objetPerdus) {
        this.objetPerdus = objetPerdus;
    }
}
