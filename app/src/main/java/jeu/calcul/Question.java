package jeu.calcul;

import java.util.Random;

public class Question {

    private int firstNumber;
    private int secondNumber;
    private int answer;
    private int[] answerArray;
    private int answerPosition;
    private int upperLimit;
    private String questionPhrase;

    /*

     */
    // Génère la question ainsi que les 4 réponses proposés.
    public Question(int upperLimit) {
        this.upperLimit = upperLimit;
        Random randomNumberMaker = new Random();

        this.firstNumber = randomNumberMaker.nextInt(upperLimit);
        this.secondNumber = randomNumberMaker.nextInt(upperLimit);
        this.answer = this.firstNumber + this.secondNumber;
        this.questionPhrase = firstNumber + " + " + secondNumber + " = ";

        this.answerPosition = randomNumberMaker.nextInt(4);
        this.answerArray = new int[]{0, 1, 2, 3};

        this.answerArray[0] = answer + 1;
        this.answerArray[1] = answer + 10;
        this.answerArray[2] = answer - 5;
        this.answerArray[3] = answer - 2;

        this.answerArray = shuffleArray(this.answerArray);

        // Appel de la fonction pour placer aléatoirement les réponses
        answerArray[answerPosition] = answer;
    }

    // Les positions des réponses sont placés aléatoirement.
    private int[] shuffleArray(int[] array) {
        int index, temp;
        Random randomNumberGenerator = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            index = randomNumberGenerator.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }

    // Getters qui récupèrent les attributs des réponses
    public int getAnswer() {
        return answer;
    }

    public int[] getAnswerArray() {
        return answerArray;
    }

    public String getQuestionPhrase() {
        return questionPhrase;
    }
}

