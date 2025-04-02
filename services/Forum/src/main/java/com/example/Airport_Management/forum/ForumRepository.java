package com.example.Airport_Management.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum , Integer> {
    Optional<Forum> findById(int id);

    void deleteById(int id);
}
