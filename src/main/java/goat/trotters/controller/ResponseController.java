package goat.trotters.controller;

import goat.trotters.dto.CountryStatDTO;
import goat.trotters.model.Response;
import goat.trotters.repository.ResponseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {
    private final ResponseRepository repository;

    public ResponseController(ResponseRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> createResponse(@RequestBody Response response) {
        try {
            Response savedResponse = repository.save(response);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur sauvegarde");
        }
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
            List<Object[]> results = repository.findStatsWithFilters(questionId, gender, category, minAge, maxAge);

            Map<String, Map<String, Long>> tempStats = new HashMap<>();

            for (Object[] row : results) {
                if (row[0] == null || row[1] == null) continue;

                String country = (String) row[0];
                String rawAnswer = (String) row[1];
                Long count = (Long) row[2];

                String[] answers = rawAnswer.split(";");

                for (String answer : answers) {
                    String cleanAnswer = answer.trim();
                    if (!cleanAnswer.isEmpty()) {
                        tempStats.computeIfAbsent(country, k -> new HashMap<>())
                                .merge(cleanAnswer, count, Long::sum);
                    }
                }
            }

            List<CountryStatDTO> response = new ArrayList<>();
            for (Map.Entry<String, Map<String, Long>> entry : tempStats.entrySet()) {
                response.add(new CountryStatDTO(entry.getKey(), entry.getValue()));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserResponses(@PathVariable String userId) {
        try {
            repository.deleteByUserId(userId);
            return ResponseEntity.ok("Toutes les réponses de l'utilisateur ont été supprimées");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression des réponses de l'utilisateur");
        }
    }
}