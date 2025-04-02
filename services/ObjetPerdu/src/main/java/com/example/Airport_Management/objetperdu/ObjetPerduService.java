package com.example.Airport_Management.objetperdu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

}
