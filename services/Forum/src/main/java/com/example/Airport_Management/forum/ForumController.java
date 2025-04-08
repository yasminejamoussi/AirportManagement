package com.example.Airport_Management.forum;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Forum> createForum(@RequestBody Forum forum) {
        return new ResponseEntity<>(forumService.addForum(forum), HttpStatus.OK);
    }
    @RequestMapping
    public ResponseEntity<List<Forum>> getAll() {
        return new ResponseEntity<>(forumService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Forum> updateForum(@PathVariable(value = "id") int id,
                                                       @RequestBody Forum forum){
        return new ResponseEntity<>(forumService.updateForum(id, forum),
                HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteForum(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(forumService.deleteForum(id), HttpStatus.OK);
    }
}
