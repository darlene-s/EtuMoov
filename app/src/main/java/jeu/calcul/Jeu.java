package jeu.calcul;


import java.util.ArrayList;
import java.util.List;

public class Jeu {

    private List<Question> questions = new ArrayList<>();
    private int numberCorrect;
    private int numberIncorrect;
    private int totalQuestions;
    private int score;
    private Question currentQuestion;

    //Constructeur d'un jeu
    public Jeu() {
        numberCorrect = 0;
        numberIncorrect = 0;
        totalQuestions = 0;
        score = 0;
        currentQuestion = new Question(10);
    }

    // Fonction qui peret de générer une nouvelle question
    public void makeNewQuestion() {
        currentQuestion = new Question(totalQuestions * 2 + 5);
        totalQuestions++;
        questions.add(currentQuestion);
    }

    // Vérifie la réponse entre les 4 boutons, si c'est la réponse est correte. On augmente le score de 10 pts et si on a faux on perds 30 pts
    public boolean checkAnswer(int submittedAnswer) {
        boolean isCorrect;
        if (currentQuestion.getAnswer() == submittedAnswer) {
            numberCorrect++;
            isCorrect = true;
        } else {
            numberIncorrect++;
            isCorrect = false;
        }
        score = numberCorrect * 10 - numberIncorrect * 30;
        return isCorrect;
    }

    // Getter qui récupèrent les attributs d'un jeu
    public int getNumberCorrect() {
        return numberCorrect;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getScore() {
        return score;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }
}
