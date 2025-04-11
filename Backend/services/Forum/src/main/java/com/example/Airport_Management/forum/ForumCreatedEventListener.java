package com.example.Airport_Management.forum;

import com.example.Airport_Management.forum.ForumCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ForumCreatedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ForumCreatedEventListener.class);

    @EventListener
    public void handleForumCreatedEvent(ForumCreatedEvent event) {
        // Récupérer le forum à partir de l'événement
        var forum = event.getForum();

        // Journaliser une notification dans les logs
        logger.info("NOTIFICATION: Un nouveau forum a été créé avec l'ID: {}. Contenu: {}", forum.getId(), forum.getContenu());
    }
}