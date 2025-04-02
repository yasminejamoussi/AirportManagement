package com.example.Airport_Management.passager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassagerService {
    @Autowired
    private PassagerRepository passagerRepository;

    public Passager addPassager(Passager passager) {
        return passagerRepository.save(passager);
    }

    public Passager updatePassager(String id, Passager passager) {
        if (passagerRepository.findById(id).isPresent()) {
            Passager existingPassager = passagerRepository.findById(id).get();
            existingPassager.setNom(passager.getNom());
            existingPassager.setPrenom(passager.getPrenom());
            existingPassager.setEmail(passager.getEmail());
            existingPassager.setNumero(passager.getNumero());
            existingPassager.setNotifications(passager.getNotifications());
            existingPassager.setHistoriqueDeclarations(passager.getHistoriqueDeclarations());
            return passagerRepository.save(existingPassager);
        } else {
            return null;
        }
    }

    public String deletePassager(String id) {
        if (passagerRepository.findById(id).isPresent()) {
            passagerRepository.deleteById(id);
            return "passager supprimé";
        } else {
            return "passager non supprimé";
        }
    }

    public List<Passager> getAll() {
        return passagerRepository.findAll();
    }

    public Passager getPassagerById(String id) {
        return passagerRepository.findById(id).orElse(null);
    }

    public List<Passager> getPassagerByNom(String nom) {
        return passagerRepository.findByNomContainingIgnoreCase(nom);

    }
}