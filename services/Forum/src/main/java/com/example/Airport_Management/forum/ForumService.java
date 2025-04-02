package com.example.Airport_Management.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;
    public Forum addForum(Forum forum) {
        return forumRepository.save(forum);
    }

    public List<Forum> getAll() {
        return forumRepository.findAll();
    }

    public Forum updateForum(int id, Forum newForum) {
        if (forumRepository.findById(id).isPresent()) {

            Forum existingForum = forumRepository.findById(id).get();
            existingForum.setCategorie(newForum.getCategorie());
            existingForum.setContenu(newForum.getContenu());
            existingForum.setDate_publication(newForum.getDate_publication());
            existingForum.setDerniere_mise_a_jour(newForum.getDerniere_mise_a_jour());
            existingForum.setNombre_vue(newForum.getNombre_vue());
            existingForum.setNombre_reponses(newForum.getNombre_reponses());

            return forumRepository.save(existingForum);
        } else
            return null;
    }

    public String deleteForum(int id) {
        if (forumRepository.findById(id).isPresent()) {
            forumRepository.deleteById(id);
            return "objet perdu supprimé";
        } else
            return "objet perdu non supprimé";
    }
    }

