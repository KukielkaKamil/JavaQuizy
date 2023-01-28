import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class QuestionGUI extends JFrame {

    private JPanel mainPanel;
    private static JPanel contentPanel;
    private final CardLayout cl = new CardLayout();

    private static int score;
    private static int curQuestion = 0;
    static boolean wasSelected = true;

    public QuestionGUI(Quiz quiz){
        super("Quiz");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        contentPanel = new JPanel();
        contentPanel.setLayout(cl);
        score =0;
        Random rand = new Random();


        for(int i =0; i< quiz.getQuestions().length;i++){
            for(int j = quiz.getQuestions()[i].getAnswers().length-1;j>0;j--){
                if(quiz.getQuestions()[i].getQuestionType()!=QuestionType.TrueFalseQuestion) {
                    int index = rand.nextInt(j + 1);
                    Answer a = quiz.getQuestions()[i].getAnswers()[index];
                    quiz.getQuestions()[i].getAnswers()[index] = quiz.getQuestions()[i].getAnswers()[j];
                    quiz.getQuestions()[i].getAnswers()[j] = a;
                }
            }
        }
        for (int i = quiz.getQuestions().length -1;i>0;i--){
            int index =rand.nextInt(i+1);
            Question a = quiz.getQuestions()[index];
            quiz.getQuestions()[index]=quiz.getQuestions()[i];
            quiz.getQuestions()[i]=a;
        }
        SimpleQuestion sq1 = new SimpleQuestion();
        contentPanel.add(sq1.getMainPanel(),"1");

        MultiselectQuestion msq = new MultiselectQuestion();
        contentPanel.add(msq.getMainPanel(),"2");

        ActionListener back = e -> {
            QuizMenu menu = new QuizMenu(Login.user);
            menu.setLocation(this.getLocation());
            dispose();
            menu.setVisible(true);
        };

        sq1.getGoBackButton().addActionListener(back);
        msq.getGoBackButton().addActionListener(back);

        curQuestion =0;
        ActionListener next = e -> {
            wasSelected=true;
            curQuestion++;
            if(curQuestion == quiz.getQuestions().length){
                JOptionPane.showMessageDialog(null,"Your score is: "+score+"/"+quiz.getQuestions().length);
                openMenu();
            }
            else {
                if (quiz.getQuestions()[curQuestion].getQuestionType() != QuestionType.MultiSelectionQuestion) {
                    updateSimpleQuestion(quiz, sq1, cl, 1);
                }
                if (quiz.getQuestions()[curQuestion].getQuestionType() == QuestionType.MultiSelectionQuestion) {
                    updateMultiQuestion(quiz, msq, cl, 1);
                }
            }
        };
        ActionListener prev = e -> {
            wasSelected=true;
            curQuestion--;
            if(quiz.getQuestions()[curQuestion].getQuestionType()!=QuestionType.MultiSelectionQuestion){
                updateSimpleQuestion(quiz,sq1,cl,-1);
            }
            if(quiz.getQuestions()[curQuestion].getQuestionType()==QuestionType.MultiSelectionQuestion){
                updateMultiQuestion(quiz,msq,cl,-1);
            }
        };
        sq1.getNextButton().addActionListener(next);
        sq1.getPreviousButton().addActionListener(prev);
        msq.getNextButton().addActionListener(next);
        msq.getPreviousButton().addActionListener(prev);

        if(quiz.getQuestions()[0].getQuestionType()==QuestionType.MultiSelectionQuestion){
            msq.getPreviousButton().setVisible(false);
            msq.setQuestion(quiz.getQuestions()[0]);
            cl.show(contentPanel,"2");
        }
        else{
            sq1.getPreviousButton().setVisible(false);
            sq1.setQuestion(quiz.getQuestions()[0]);
            cl.show(contentPanel,"1");
        }



        mainPanel.add(contentPanel);


    }

    public void updateSimpleQuestion(Quiz quiz, SimpleQuestion sq,CardLayout cl,int move){
            sq.getPreviousButton().setVisible(curQuestion != 0);
        if(curQuestion == quiz.getQuestions().length-1){
            sq.getNextButton().setText("Finish");
        }
        else{
            sq.getNextButton().setText("Next");
        }
            sq.setQuestion(quiz.getQuestions()[curQuestion]);
            if(quiz.getQuestions()[curQuestion-move].getQuestionType()==QuestionType.MultiSelectionQuestion)
                cl.show(contentPanel,"1");
    }

    public void updateMultiQuestion(Quiz quiz, MultiselectQuestion msq, CardLayout cl, int move){
        msq.getPreviousButton().setVisible(curQuestion !=0);
        //msq.getNextButton().setVisible(curQuestion != quiz.getQuestions().length-1);
        if(curQuestion == quiz.getQuestions().length-1){
            msq.getNextButton().setText("Finish");
        }
        else{
            msq.getNextButton().setText("Next");
        }
        msq.setQuestion(quiz.getQuestions()[curQuestion]);
        if(quiz.getQuestions()[curQuestion-move].getQuestionType()!=QuestionType.MultiSelectionQuestion)
            cl.show(contentPanel,"2");
    }

    public static void addScore() {
        score++;
        wasSelected=false;
    }
    public static void removeScore(){
        if(!wasSelected){
            score--;
            wasSelected=true;
        }
    }

    private void openMenu(){
        QuizMenu menu = new QuizMenu(Login.user);
        menu.setLocation(this.getLocation());
        dispose();
        menu.setVisible(true);
    }


}
