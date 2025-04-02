package com.example.Airport_Management.passager;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PassagerRepository extends MongoRepository<Passager , String> {
    Passager save(Passager passager);

    Optional<Passager> findById(String  id);

    void deleteById(String id);
    List<Passager> findByNomContainingIgnoreCase(String nom);
}
