package com.example.Airport_Management.objetperdu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObjetPerduRepository extends JpaRepository<ObjetPerdu , Integer> {
    Optional<ObjetPerdu> findById(int id);

    void deleteById(int id);
}
