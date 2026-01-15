package goat.trotters.controller;

import goat.trotters.dto.CountryStatDTO;
import goat.trotters.model.Response;
import goat.trotters.repository.ResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/responses")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE
})
public class ResponseController {

    // Initialisation du Logger (remplace System.out.println)
    private static final Logger logger = LoggerFactory.getLogger(ResponseController.class);

    private final ResponseRepository repository;

    public ResponseController(ResponseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Response> getAll() {
        return repository.findAll();
    }

    @GetMapping("/stats/{questionId}")
    public ResponseEntity<?> getStatsByCountryForQuestion(
            @PathVariable Long questionId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge
    ) {
        try {
            logger.info("⚡️ Stats pour ID: {} | Filtres: Genre={}, Cat={}, Age={}-{}",
                    questionId, gender, category, minAge, maxAge);

            // 1. Récupérer les données brutes
            // Appel de la nouvelle méthode du repository
            List<Object[]> results = repository.findStatsWithFilters(questionId, gender, category, minAge, maxAge);
            logger.debug("📦 Lignes SQL trouvées : {}", results.size());

            // 2. Organiser par pays (Map temporaire)
            Map<String, Map<String, Long>> tempStats = new HashMap<>();
            for (Object[] row : results) {
                if (row[0] == null || row[1] == null) continue;
                String country = (String) row[0];
                String answer = (String) row[1];
                Long count = (Long) row[2];
                tempStats.computeIfAbsent(country, k -> new HashMap<>()).put(answer.toLowerCase(), count);
            }

            // 3. Calculer les pourcentages et remplir la liste finale
            List<CountryStatDTO> response = new ArrayList<>();
            for (Map.Entry<String, Map<String, Long>> entry : tempStats.entrySet()) {
                String country = entry.getKey();
                Map<String, Long> counts = entry.getValue();
                long yesCount = counts.getOrDefault("oui", 0L);
                long noCount = counts.getOrDefault("non", 0L);
                long total = yesCount + noCount;

                if (total > 0) {
                    double yesPercent = Math.round(((double) yesCount / total) * 100);
                    double noPercent = 100 - yesPercent;
                    response.add(new CountryStatDTO(country, yesPercent, noPercent));
                }
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Erreur stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Response response) {
        try {
            logger.info("📝 Nouvelle réponse reçue pour l'utilisateur : {}", response.getUserId());
            Response savedResponse = repository.save(response);
            return ResponseEntity.ok(savedResponse);
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'enregistrement", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'enregistrement de la réponse: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResponse(@PathVariable Long id) {
        try {
            if (!repository.existsById(id)) {
                logger.warn("⚠️ Tentative de suppression d'une réponse inexistante ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réponse non trouvée");
            }
            repository.deleteById(id);
            logger.info("🗑️ Réponse ID: {} supprimée avec succès", id);
            return ResponseEntity.ok("Réponse supprimée avec succès");
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la suppression ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la réponse");
        }
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteAllResponses() {
        try {
            long count = repository.count();
            if (count == 0) return ResponseEntity.ok("Aucune réponse à supprimer");

            repository.deleteAllResponses();
            logger.info("💥 Suppression totale de {} réponses", count);
            return ResponseEntity.ok("Toutes les réponses ont été supprimées (" + count + " réponses)");
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la suppression totale", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression des réponses : " + e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserResponses(@PathVariable String userId) {
        try {
            repository.deleteByUserId(userId);
            logger.info("👤 Suppression des réponses pour l'utilisateur : {}", userId);
            return ResponseEntity.ok("Toutes les réponses de l'utilisateur ont été supprimées");
        } catch (Exception e) {
            logger.error("❌ Erreur suppression utilisateur : {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression des réponses de l'utilisateur");
        }
    }
}