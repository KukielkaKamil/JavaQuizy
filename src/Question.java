import javax.swing.*;

public class Question {
    private final String text;
    private final Answer[] answers;
    private final QuestionType questionType;
    private final int answerCount;
    private int id;

    private JButton selectedButton;

    public Question(String text, Answer[] answers, QuestionType questionType) {
        this.text = text;
        this.answers = answers;
        this.questionType = questionType;
        answerCount = answers.length;
    }

    public String getText() {
        return text;
    }


    public Answer[] getAnswers() {
        return answers;
    }


    public QuestionType getQuestionType() {
        return questionType;
    }


    public int getQuestionCount() {
        return answerCount;
    }

    public JButton getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(JButton selectedButton) {
        this.selectedButton = selectedButton;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString(){
        return text;
    }

}
