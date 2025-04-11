package com.example.Airport_Management.objetperdu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObjetPerduRepository extends JpaRepository<ObjetPerdu , Integer> {

    Optional<ObjetPerdu> findById(int id);

    void deleteById(int id);
    @Query("SELECT o FROM ObjetPerdu o  WHERE LOWER(o.type_objet) LIKE LOWER(CONCAT('%', :type_objet, '%'))")
    List<ObjetPerdu> findByType_objetContainingIgnoreCase(@Param("type_objet") String type_objet);
}
