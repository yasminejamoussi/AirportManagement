package com.example.Airport_Management.forum;

import com.example.Airport_Management.forum.Forum;
import org.springframework.context.ApplicationEvent;

public class ForumCreatedEvent extends ApplicationEvent {

    private final Forum forum;

    public ForumCreatedEvent(Object source, Forum forum) {
        super(source);
        this.forum = forum;
    }

    public Forum getForum() {
        return forum;
    }
}