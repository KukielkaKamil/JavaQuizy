import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class AddQuestionGUI extends JFrame {
    private JTextField questionField;
    private JComboBox questionTypeComboBox;
    private JButton addQuestionButton;
    private JButton cancleButton;
    private JTextField answer1Field;
    private JTextField answer2Field;
    private JTextField answer3Field;
    private JTextField answer4Field;
    private JComboBox trueFalseCombo;
    private JPanel mainPanel;
    private JPanel trueFalsePanel;
    private JPanel answ3Panel;
    private JPanel answ2Panel;
    private JPanel answ4Panel;
    private JPanel answ1Panel;
    private JPanel answ1Panel2;
    private JPanel answ4Panel2;
    private JPanel answ2Panel2;
    private JPanel answ3Panel2;
    private JPanel trueFalsePanel2;
    private JPanel multiSelectionPanel;
    private JTextArea goodAnswers;
    private JTextArea otherAnswers;


    private final ArrayList<Answer> answers = new ArrayList<>();

    public AddQuestionGUI(DefaultTableModel tableModel,JTable table,boolean edit){
        super("Question Manager");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300,400);

        if(edit){
            int x = table.getSelectedRow();
            Question curQuestion = (Question) tableModel.getValueAt(x,0);
            questionField.setText(curQuestion.getText());
            switch (curQuestion.getQuestionType()){
                case SimpleQuestion -> {
                    questionTypeComboBox.setSelectedIndex(0);
                    answer1Field.setText(curQuestion.getAnswers()[0].text);
                    answer2Field.setText(curQuestion.getAnswers()[1].text);
                    if(curQuestion.getAnswers().length>2)
                        answer3Field.setText(curQuestion.getAnswers()[2].text);
                    if(curQuestion.getAnswers().length>3)
                        answer4Field.setText(curQuestion.getAnswers()[3].text);
                }
                case TrueFalseQuestion ->{
                    questionTypeComboBox.setSelectedIndex(1);
                    if(curQuestion.getAnswers()[0].isCorrect){
                        trueFalseCombo.setSelectedIndex(0);
                    }
                    else{
                        trueFalseCombo.setSelectedIndex(1);
                    }
                }
                case MultiSelectionQuestion ->{
                    questionTypeComboBox.setSelectedIndex(2);
                    StringBuilder good = new StringBuilder();
                    StringBuilder other = new StringBuilder();
                    for(int i = 0; i<curQuestion.getAnswers().length;i++){
                        if(curQuestion.getAnswers()[i].isCorrect){
                            good.append(curQuestion.getAnswers()[i].text).append("\n");
                        }
                        else{
                            other.append(curQuestion.getAnswers()[i].text).append("\n");
                        }
                    }
                    goodAnswers.setText(good.toString());
                    otherAnswers.setText(other.toString());
                }
            }

        }
        setLayout();
        questionTypeComboBox.addActionListener(e -> setLayout());
        cancleButton.addActionListener(e -> dispose());
        addQuestionButton.addActionListener(e -> {
            int x = questionTypeComboBox.getSelectedIndex();
            if (questionField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "You can't create empty question");
            } else {
                switch (x) {
                    case 0 -> {
                        if (answer1Field.getText().equals("") || answer2Field.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "You must add at least 2 answers");
                        } else {
                            int index = table.getSelectedRow();
                            answers.add(new Answer(answer1Field.getText(), true));
                            answers.add(new Answer(answer2Field.getText(), false));
                            if (!answer3Field.getText().equals(""))
                                answers.add(new Answer(answer3Field.getText(), false));
                            if (!answer4Field.getText().equals(""))
                                answers.add(new Answer(answer4Field.getText(), false));
                            Question question = new Question(questionField.getText(), answers.toArray(new Answer[0]), QuestionType.SimpleQuestion);
                            if (edit) {
                                tableModel.removeRow(index);
                                tableModel.insertRow(index, new Question[]{question});
                            } else tableModel.addRow(new Question[]{question});
                            table.clearSelection();
                            dispose();
                        }

                    }
                    case 1 -> {
                            int index = table.getSelectedRow();
                            Answer[] answers = new Answer[2];
                            if (trueFalseCombo.getSelectedIndex() == 0) {
                                answers[0] = new Answer("True", true);
                                answers[1] = new Answer("False", false);
                            } else {
                                answers[0] = new Answer("True", false);
                                answers[1] = new Answer("False", true);
                            }
                            Question question = new Question(questionField.getText(), answers, QuestionType.TrueFalseQuestion);

                            if(edit) {
                                tableModel.removeRow(index);
                                tableModel.insertRow(index, new Question[]{question});
                            }
                            else{
                                tableModel.addRow(new Question[]{question});
                            }
                        table.clearSelection();
                        dispose();

                    }
                    case 2 -> {
                        int index = table.getSelectedRow();
                        String[] answers1 = goodAnswers.getText().split("\\r?\\n");
                        String[] answers2 = otherAnswers.getText().split("\\r?\\n");
                        Answer[] answers = new Answer[answers1.length+answers2.length];
                        int j = 0;
                        for(int i =0; i<answers.length;i++){
                            if(i<answers1.length){
                                answers[i] = new Answer(answers1[i],true);
                            }
                            else{
                                answers[i] = new Answer(answers2[j],false);
                                j++;
                            }
                        }
                        Question question = new Question(questionField.getText(),answers,QuestionType.MultiSelectionQuestion);
                        if(edit){
                            tableModel.removeRow(index);
                            tableModel.insertRow(index, new Question[]{question});
                        }
                        else{
                            tableModel.addRow(new Question[]{question});
                        }
                        table.clearSelection();
                        dispose();

                    }
                }
            }
        });
    }

    public void setLayout(){
        int x=questionTypeComboBox.getSelectedIndex();

        switch (x){
            case 0 ->{
                answ1Panel.setVisible(true);
                answ1Panel2.setVisible(true);
                answ2Panel.setVisible(true);
                answ2Panel2.setVisible(true);
                answ3Panel.setVisible(true);
                answ3Panel2.setVisible(true);
                answ4Panel.setVisible(true);
                answ4Panel2.setVisible(true);
                trueFalsePanel.setVisible(false);
                trueFalsePanel2.setVisible(false);
                multiSelectionPanel.setVisible(false);
            }
            case 1 ->{
                answ1Panel.setVisible(false);
                answ1Panel2.setVisible(false);
                answ2Panel.setVisible(false);
                answ2Panel2.setVisible(false);
                answ3Panel.setVisible(false);
                answ3Panel2.setVisible(false);
                answ4Panel.setVisible(false);
                answ4Panel2.setVisible(false);
                multiSelectionPanel.setVisible(false);
                trueFalsePanel.setVisible(true);
                trueFalsePanel2.setVisible(true);
            }
            case 2 ->{
                answ1Panel.setVisible(false);
                answ1Panel2.setVisible(false);
                answ2Panel.setVisible(false);
                answ2Panel2.setVisible(false);
                answ3Panel.setVisible(false);
                answ3Panel2.setVisible(false);
                answ4Panel.setVisible(false);
                answ4Panel2.setVisible(false);
                trueFalsePanel.setVisible(false);
                trueFalsePanel2.setVisible(false);
                multiSelectionPanel.setVisible(true);
            }
        }
    }

    public JButton getAddQuestionButton() {
        return addQuestionButton;
    }
}
