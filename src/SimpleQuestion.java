import javax.swing.*;


public class SimpleQuestion {
    private JTextPane questionTextPane;
    private JButton ans1Button;
    private JButton ans2Button;
    private JButton ans3Button;
    private JButton ans4Button;
    private JButton ansExtButton;
    private JButton nextButton;
    private JButton previousButton;
    private JPanel mainPanel;
    private JButton goBackButton;
    private Question qs;
    private int ansIndex;
    private int points =0;


    public SimpleQuestion() {
        ans1Button.addActionListener(e -> {
            ansIndex =0;
            select(ans1Button);
        });
        ans2Button.addActionListener(e -> {
            ansIndex =1;
            select(ans2Button);
        });
        ans3Button.addActionListener(e -> {
            ansIndex =2;
            select(ans3Button);
        });
        ans4Button.addActionListener(e -> {
            ansIndex =3;
            select(ans4Button);
        });
        ansExtButton.addActionListener(e -> {
            ansIndex =2;
            select(ansExtButton);
        });
    }

    public void unselectAll(){
        ans1Button.setEnabled(true);
        ans2Button.setEnabled(true);
        ans3Button.setEnabled(true);
        ans4Button.setEnabled(true);
        ansExtButton.setEnabled(true);
    }

    public void setQuestion(Question question){
        questionTextPane.setText(question.getText());
        ans3Button.setVisible(false);
        ans4Button.setVisible(false);
        ansExtButton.setVisible(false);
        qs=question;
        unselectAll();
        if(question.getSelectedButton() != null){
            question.getSelectedButton().setEnabled(false);
        }
        if(question.getQuestionType()==QuestionType.TrueFalseQuestion){
            ans1Button.setText("True");
            ans2Button.setText("False");
        }
        else{
            ans1Button.setText(question.getAnswers()[0].toString());
            ans2Button.setText(question.getAnswers()[1].toString());
            if(question.getQuestionCount()==3){
                ansExtButton.setText(question.getAnswers()[2].toString());
                ansExtButton.setVisible(true);
            }
            if(question.getQuestionCount()==4){
                ans3Button.setText(question.getAnswers()[2].toString());
                ans4Button.setText(question.getAnswers()[3].toString());
                ans3Button.setVisible(true);
                ans4Button.setVisible(true);
            }
            if(question.getQuestionCount()>4){
                throw new ArrayIndexOutOfBoundsException();
            }
        }

    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JButton getPreviousButton() {
        return previousButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getGoBackButton() {
        return goBackButton;
    }

    public void select(JButton button){
        unselectAll();
        qs.setSelectedButton(button);
        button.setEnabled(false);
        if(qs.getAnswers()[ansIndex].isCorrect){
            QuestionGUI.addScore();
        }
        else{
            QuestionGUI.removeScore();
        }

    }
}
