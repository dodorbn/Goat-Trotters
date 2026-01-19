package goat.trotters.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String description;

    // ✅ NOUVEAU : Le type de question (SCORE, CHOICE, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    // ✅ NOUVEAU : Les choix possibles séparés par des ";"
    // Ex: "Voiture;Vélo;Transports en commun"
    // Null si c'est une question de type SCORE ou TEXT
    @Column(length = 1000) // On prévoit de la place
    private String possibleAnswers;
}