import javax.swing.*;
public class QuizMenu extends JFrame {
    private JPanel mainPanel;
    private JButton browseQuizesButton;
    private JButton manageYourQuizesButton;
    private JButton logOutButton;
    private JLabel header;

    public QuizMenu(User user){
        super("Java Quiz");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(300,400);
        header.setText("Welcome "+user.getLogin());
        browseQuizesButton.addActionListener(e -> openPublicBrowser());
        manageYourQuizesButton.addActionListener(e -> openPersonalBrowser());
        logOutButton.addActionListener(e -> logout());
    }
    private void openPublicBrowser(){
        QuizBrowser qb = new QuizBrowser(BrowserMode.BrowseQuiz);
        qb.setLocation(this.getLocation());
        dispose();
        qb.setVisible(true);
    }

    private void openPersonalBrowser(){
        QuizBrowser qb = new QuizBrowser(BrowserMode.ManageQuiz);
        qb.setLocation(this.getLocation());
        dispose();
        qb.setVisible(true);
    }

    private void logout(){
        Login.user=null;
        Login login = new Login();
        login.setLocation(this.getLocation());
        dispose();
        login.setVisible(true);
    }

}
