package com.example.Airport_Management.objetperdu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ObjetPerduService {
    @Autowired
    private ObjetPerduRepository objetPerduRepository;
    public ObjetPerdu addObjetPerdu(ObjetPerdu objetPerdu) {
        return objetPerduRepository.save(objetPerdu);
    }

    public List<ObjetPerdu> getAll() {
        return objetPerduRepository.findAll();
    }

    public ObjetPerdu updateObjetPerdu(int id, ObjetPerdu newObjetPerdu) {
        if (objetPerduRepository.findById(id).isPresent()) {

            ObjetPerdu existingObjetPerdu = objetPerduRepository.findById(id).get();
            existingObjetPerdu.setType_objet(newObjetPerdu.getType_objet());
            existingObjetPerdu.setDescription(newObjetPerdu.getDescription());
            existingObjetPerdu.setPhoto(newObjetPerdu.getPhoto());
            existingObjetPerdu.setStatut(newObjetPerdu.getStatut());
            existingObjetPerdu.setLieu_decouverte(newObjetPerdu.getLieu_decouverte());
            existingObjetPerdu.setDate_decouverte(newObjetPerdu.getDate_decouverte());

            return objetPerduRepository.save(existingObjetPerdu);
        } else
            return null;
    }

    public String deleteObjetPerdu(int id) {
        if (objetPerduRepository.findById(id).isPresent()) {
            objetPerduRepository.deleteById(id);
            return "objet perdu supprimé";
        } else
            return "objet perdu non supprimé";
    }

   public ObjetPerdu getObjetPerduById(int id) {return objetPerduRepository.findById(id).orElse(null);
    }

    public List<ObjetPerdu> getObjetPerduByTypeObjet(String type_objet) {
        return objetPerduRepository.findByType_objetContainingIgnoreCase(type_objet);
    }

    public List<ObjetPerdu> getAllSortedByTypeAsc() {
        return objetPerduRepository.findAll(Sort.by(Sort.Direction.ASC, "statut"));
    }

    public List<ObjetPerdu> getAllSortedByTypeDesc() {
        return objetPerduRepository.findAll(Sort.by(Sort.Direction.DESC, "statut"));
    }
    public Map<String, Long> getStatisticsByTypeObjet() {
        List<ObjetPerdu> objetsPerdus = objetPerduRepository.findAll();
        return objetsPerdus.stream()
                .collect(Collectors.groupingBy(
                        ObjetPerdu::getType_objet, // Regrouper par type_objet
                        Collectors.counting()      // Compter le nombre d'objets par type
                ));
    }
}
