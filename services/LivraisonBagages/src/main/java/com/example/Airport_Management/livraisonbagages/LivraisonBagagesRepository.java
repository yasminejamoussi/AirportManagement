package com.example.Airport_Management.livraisonbagages;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LivraisonBagagesRepository extends MongoRepository<LivraisonBagages , String> {
    LivraisonBagages save(LivraisonBagages livraisonBagages);
    Optional<LivraisonBagages> findById(String id);

    void deleteById(String id);
}
