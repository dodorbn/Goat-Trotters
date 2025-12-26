package goat.trotters.repository;

import goat.trotters.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    // Vérifie si un utilisateur a déjà répondu
    boolean existsByUserIdAndQuestionId(String userId, Long questionId);

    // 🎯 C'est la méthode clé pour ta Heatmap
    // Requête dynamique avec filtres optionnels
    @Query("SELECT r.country, r.answer, COUNT(r) " +
            "FROM Response r " +
            "WHERE r.question.id = :questionId " +
            "AND (:gender IS NULL OR r.gender = :gender) " +
            "AND (:category IS NULL OR r.category = :category) " +
            "AND (:minAge IS NULL OR r.age >= :minAge) " +
            "AND (:maxAge IS NULL OR r.age <= :maxAge) " +
            "GROUP BY r.country, r.answer")
    List<Object[]> findStatsWithFilters(
            @Param("questionId") Long questionId,
            @Param("gender") String gender,
            @Param("category") String category,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge
    );

    // Suppression totale (Optimisé en JPQL)
    @Modifying
    @Transactional
    @Query("DELETE FROM Response r") // Plus sûr que le nativeQuery
    void deleteAllResponses();

    // Suppression par utilisateur (Ajout de @Transactional obligatoire ici)
    @Modifying
    @Transactional
    void deleteByUserId(String userId);

    // Stats globales pour d'autres graphiques
    @Query("SELECT r.country, COUNT(r) FROM Response r GROUP BY r.country")
    List<Object[]> countByCountry();

    @Query("SELECT r.question, r.answer, COUNT(r) FROM Response r GROUP BY r.question, r.answer")
    List<Object[]> countByQuestionAndAnswer();
}