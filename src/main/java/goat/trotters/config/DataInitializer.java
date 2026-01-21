package goat.trotters.config;

import goat.trotters.model.Question;
import goat.trotters.model.QuestionType;
import goat.trotters.repository.QuestionRepository;
import goat.trotters.repository.ResponseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(QuestionRepository questionRepo, ResponseRepository responseRepo) {
        return args -> {
            responseRepo.deleteAll();
            questionRepo.deleteAll();

            // --- 1. BIEN-ÊTRE ---
            saveQuestion(questionRepo,
                    "Sur une échelle de 1 à 10, à quel point êtes-vous heureux de vivre dans ce pays ?",
                    "Bien-être", QuestionType.SCORE, null);

            saveQuestion(questionRepo,
                    "Si vous deviez vivre dans un autre pays européen, lequel choisiriez-vous ?",
                    "Bien-être", QuestionType.CHOICE,
                    "France;Allemagne;Italie;Espagne;Pologne;Roumanie;Pays-Bas;Belgique;Grèce;Portugal;Suède;Hongrie;Autriche;République tchèque;Irlande;Danemark;Finlande;Slovaquie;Norvège");

            // --- 2. POLITIQUE ---
            saveQuestion(questionRepo,
                    "Allez-vous voter lors des prochaines élections ?",
                    "Politique", QuestionType.CHOICE,
                    "Oui;Peut-être;Non;Je ne vote pas");

            saveQuestion(questionRepo,
                    "Avez-vous confiance en vos politiciens pour améliorer votre quotidien ?",
                    "Politique", QuestionType.CHOICE,
                    "Confiance;Mitigé;Aucune confiance");

            // --- 3. EUROPE ---
            saveQuestion(questionRepo,
                    "Globalement, l'Union Européenne est-elle...",
                    "Europe", QuestionType.CHOICE,
                    "Une chance;Une contrainte;Indifférent");

            // --- 4. CULTURE ---
            saveQuestion(questionRepo,
                    "Selon vous, la culture est-elle primordiale ?",
                    "Culture", QuestionType.CHOICE,
                    "Oui;Non;Indifférent");

            saveQuestion(questionRepo,
                    "Quelle place occupe le sport dans votre vie ?",
                    "Culture", QuestionType.CHOICE,
                    "Je pratique régulièrement;Je pratique occasionnellement;Je regarde seulement à la télé;Ça ne m'intéresse pas");

            // --- 5. ÉCOLOGIE ---
            saveQuestion(questionRepo,
                    "Au quotidien, votre engagement écologique est... ?",
                    "Écologie", QuestionType.CHOICE,
                    "Fort (actions concrètes);Modéré (petits gestes);Faible (ce n'est pas ma priorité)");

            saveQuestion(questionRepo,
                    "Votre pays en fait-il assez pour lutter contre le dérèglement climatique ?",
                    "Écologie", QuestionType.CHOICE,
                    "Oui;Non, pas assez;C'est aux autres pays d'agir");

            // --- 6. RÉSEAUX SOCIAUX ---
            saveQuestion(questionRepo,
                    "Combien de temps passez-vous par jour sur les réseaux sociaux ?",
                    "Réseaux sociaux", QuestionType.CHOICE,
                    "Moins d'1h;1h à 3h;Plus de 3h");

            saveQuestion(questionRepo,
                    "Globalement, les réseaux sociaux ont un impact...",
                    "Réseaux sociaux", QuestionType.CHOICE,
                    "Positif;Négatif");

            // --- 7. INTELLIGENCE ARTIFICIELLE ---
            saveQuestion(questionRepo,
                    "Utilisez-vous l'IA ? Dans quelles circonstances ?",
                    "IA", QuestionType.MULTIPLE_CHOICE,
                    "Oui (Pro);Oui (Perso);Non");

            saveQuestion(questionRepo,
                    "L'Intelligence Artificielle (IA) vous fait-elle peur ?",
                    "IA", QuestionType.CHOICE,
                    "Oui;Non");

            // --- 8. SOCIÉTÉ ---
            saveQuestion(questionRepo,
                    "Êtes-vous satisfait de votre pouvoir d’achat ?",
                    "Société", QuestionType.CHOICE,
                    "Oui;Moyennement;Non");

            saveQuestion(questionRepo,
                    "Dans la vie, quelle est votre priorité absolue ?",
                    "Société", QuestionType.CHOICE,
                    "Réussite Pro & Argent;Temps libre & Loisirs;Famille & Amis");

            // --- 9. VALEURS & INÉGALITÉS ---
            saveQuestion(questionRepo,
                    "Diriez-vous que l'égalité hommes-femmes est acquise ici ?",
                    "Valeurs & Inégalités", QuestionType.CHOICE,
                    "Oui, totalement;En progrès;Non, pas du tout");

            saveQuestion(questionRepo,
                    "Trouvez-vous que votre société est tolérante envers les minorités ?",
                    "Valeurs & Inégalités", QuestionType.CHOICE,
                    "Très tolérante;Plutôt tolérante;Pas assez tolérante");

            saveQuestion(questionRepo,
                    "Selon vous, est-il facile de trouver un emploi dans votre pays aujourd'hui ?",
                    "Valeurs & Inégalités", QuestionType.CHOICE,
                    "Oui, ça recrute;C'est compliqué;C'est impossible");

            // --- 10. RELIGION ---
            saveQuestion(questionRepo,
                    "Quelle place occupe la religion dans votre vie ?",
                    "Religion", QuestionType.CHOICE,
                    "Croyant et pratiquant;Croyant mais non pratiquant;Athée ou Agnostique");

            saveQuestion(questionRepo,
                    "Selon vous, la religion doit-elle rester strictement privée ?",
                    "Religion", QuestionType.CHOICE,
                    "Oui, sphère privée uniquement;Non, elle peut être visible");

            // --- 11. SANTÉ ---
            saveQuestion(questionRepo,
                    "Êtes-vous satisfait de votre système de santé public ?",
                    "Santé", QuestionType.CHOICE,
                    "Oui;Moyennement;Non");

            saveQuestion(questionRepo,
                    "Que pensez-vous de la légalisation de la mort assistée (Euthanasie) ?",
                    "Santé", QuestionType.CHOICE,
                    "Pour;Contre;Mitigé");

            // --- 12. BONUS ---
            saveQuestion(questionRepo,
                    "Quel est le cliché sur votre pays qui est totalement vrai ?",
                    "Cliché Pays", QuestionType.TEXT, null);

            saveQuestion(questionRepo,
                    "Un seul mot pour décrire l'Europe ?",
                    "Nuage de Mots", QuestionType.TEXT, null);

            System.out.println("✅ Base de données initialisée (24 questions, 0 réponses).");
        };
    }

    private void saveQuestion(QuestionRepository repo, String text, String desc, QuestionType type, String options) {
        Question q = new Question();
        q.setText(text);
        q.setDescription(desc);
        q.setType(type);
        q.setPossibleAnswers(options);
        repo.save(q);
    }
}