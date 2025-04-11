package com.example.Airport_Management.forum;

import java.util.List;

public class ForumDTO {
    private Forum forum;
    private List<PassagerDTO> passagers;

    public ForumDTO(Forum forum, List<PassagerDTO> passagers) {
        this.forum = forum;
        this.passagers = passagers;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public List<PassagerDTO> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<PassagerDTO> passagers) {
        this.passagers = passagers;
    }
}
