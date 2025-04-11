package com.example.Airport_Management.reclamation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
    Optional<Reclamation> findById(int id);

    void deleteById(int id);

    // Filtrage par statut exact
    @Query("SELECT r FROM Reclamation r WHERE r.statut = :statut")
    List<Reclamation> findByStatut(@Param("statut") Statut statut);

    // Méthodes de recherche avancée
    @Query("SELECT r FROM Reclamation r WHERE r.typeReclamation = :type AND r.statut = :statut AND r.date_soumission BETWEEN :startDate AND :endDate")
    List<Reclamation> findByTypeReclamationAndStatutAndDate_soumissionBetween(
            @Param("type") TypeReclamation type,
            @Param("statut") Statut statut,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Reclamation r WHERE r.typeReclamation = :type AND r.statut = :statut")
    List<Reclamation> findByTypeReclamationAndStatut(
            @Param("type") TypeReclamation type,
            @Param("statut") Statut statut);

    @Query("SELECT r FROM Reclamation r WHERE r.typeReclamation = :type")
    List<Reclamation> findByTypeReclamation(@Param("type") TypeReclamation type);

    @Query("SELECT r FROM Reclamation r WHERE r.date_soumission >= :startDate AND r.date_soumission <= :endDate")
    List<Reclamation> findByDate_soumissionBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Méthode de tri par date
    @Query("SELECT r FROM Reclamation r ORDER BY r.date_soumission DESC")
    List<Reclamation> findAllOrderByDateSoumissionDesc();
}