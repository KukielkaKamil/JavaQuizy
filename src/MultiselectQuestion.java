import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MultiselectQuestion {
    private JButton previousButton;
    private JButton nextButton;
    private JPanel mainPanel;
    private JTextPane questionTextPane;
    private JPanel contentPanel;
    private JButton goBackButton;
    private int goodCount= 0;
    int good =0;
    int bad = 0;



    public void setQuestion(Question question){
        questionTextPane.setText(question.getText());
        Answer[] elements = new Answer[question.getAnswers().length];
        for (int i = 0; i<question.getAnswers().length;i++){
            elements[i]= question.getAnswers()[i];
        }
        for (Answer element : elements) {
            if (element.isCorrect) goodCount++;
        }
        JList list = new JList(elements);
        list.setCellRenderer(new ChecklistRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                Answer item = (Answer) list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                if(item.isSelected()){
                    if(item.isCorrect) good++;
                    else bad++;
                }
                else{
                    if(item.isCorrect) good--;
                    else bad--;
                }
                if(good==goodCount && bad<=0){
                    QuestionGUI.addScore();
                }
                else QuestionGUI.removeScore();
                list.repaint(list.getCellBounds(index,index));
            }
        });



        contentPanel.add(new JScrollPane(list));

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public JButton getPreviousButton() {
        return previousButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JButton getGoBackButton() {
        return goBackButton;
    }

}
