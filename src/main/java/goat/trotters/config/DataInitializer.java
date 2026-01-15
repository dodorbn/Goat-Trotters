package goat.trotters.config;

import goat.trotters.model.Question;
import goat.trotters.model.Response;
import goat.trotters.repository.QuestionRepository;
import goat.trotters.repository.ResponseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(QuestionRepository questionRepo, ResponseRepository responseRepo) {
        return args -> {
            // On vérifie si la base est vide pour ne pas dupliquer à chaque redémarrage
            if (questionRepo.count() == 0) {
                System.out.println("🚀 Initialisation de la base de données...");

                // 1. Création des Questions
                Question q1 = new Question();
                q1.setText("Pensez-vous que l'écologie est une priorité en Europe ?");
                q1.setDescription("Cette question concerne les politiques environnementales de l'UE");
                q1 = questionRepo.save(q1); // Important : on récupère l'objet avec son ID généré

                Question q2 = new Question();
                q2.setText("Croyez-vous que les jeunes ont assez de pouvoir politique ?");
                q2.setDescription("Impact de la jeunesse sur les décisions politiques européennes");
                q2 = questionRepo.save(q2);

                Question q3 = new Question();
                q3.setText("L'Union Européenne aide-t-elle suffisamment les citoyens ?");
                q3.setDescription("Évaluation des programmes d'aide et de soutien de l'UE");
                q3 = questionRepo.save(q3);

                // 2. Création des Réponses (liées aux questions ci-dessus)
                List<Response> responses = new ArrayList<>();

                // --- FRANCE (Mixte : 50% Oui / 50% Non) ---
                responses.add(createResponse("user1", 25, "France", "Oui", q1));
                responses.add(createResponse("user2", 30, "France", "Non", q1));
                responses.add(createResponse("user3", 22, "France", "Oui", q1));
                responses.add(createResponse("user4", 45, "France", "Non", q1));

                // --- GERMANY (Sceptique : Beaucoup de Non) ---
                // "Germany" et pas "Allemagne" pour matcher le GeoJSON
                responses.add(createResponse("user5", 35, "Germany", "Non", q1));
                responses.add(createResponse("user6", 40, "Germany", "Non", q1));
                responses.add(createResponse("user7", 28, "Germany", "Non", q1));
                responses.add(createResponse("user8", 55, "Germany", "Oui", q1));

                // --- SPAIN (Optimiste : Beaucoup de Oui) ---
                responses.add(createResponse("user9", 25, "Spain", "Oui", q1));
                responses.add(createResponse("user10", 29, "Spain", "Oui", q1));
                responses.add(createResponse("user11", 32, "Spain", "Oui", q1));

                // --- ITALY (Mitigé) ---
                responses.add(createResponse("user12", 24, "Italy", "Non", q1));
                responses.add(createResponse("user13", 26, "Italy", "Oui", q1));

                // --- Réponses pour la Question 2 (Jeunesse) ---
                responses.add(createResponse("user1", 25, "France", "Oui", q2));
                responses.add(createResponse("user5", 35, "Germany", "Non", q2));
                responses.add(createResponse("user9", 25, "Spain", "Oui", q2));

                // Sauvegarde en masse (optimisé)
                responseRepo.saveAll(responses);

                System.out.println("✅ Base de données initialisée avec succès !");
                System.out.println("📊 " + responses.size() + " réponses ajoutées pour tester la Heatmap.");
            } else {
                System.out.println("ℹ️ La base contient déjà des données, pas d'initialisation.");
            }
        };
    }

    // Méthode utilitaire pour créer une réponse proprement
    private Response createResponse(String userId, int age, String country, String answer, Question q) {
        Response r = new Response();
        r.setUserId(userId);
        r.setAge(age);
        r.setCountry(country);
        r.setAnswer(answer);
        r.setQuestion(q);
        // Valeurs par défaut pour les champs obligatoires mais moins importants ici
        r.setCategory("TestUser");
        r.setGender("Other");
        return r;
    }
}