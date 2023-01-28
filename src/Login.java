import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField loginField;
    private JButton loginButton;
    private JButton exitButton;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JLabel header;
    public static User user;

    private boolean register = false;
    private final static String driver = "org.mariadb.jdbc.Driver";
    private final static String url = "jdbc:mariadb://localhost:3306/java_quizy_testbase";
    private static Connection connection;
    private static Statement statement;

    public Login(){
        super("Java Quiz");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(300,400);
        registerButton.addActionListener(e -> {
            header.setText("Create new account");
            register = true;
            registerButton.setVisible(false);
            exitButton.setText("Back");
            loginButton.setText("Make account");
        });

        exitButton.addActionListener(e -> {
            if(register){
                header.setText("Welcome to Java Quizy");
                register = false;
                registerButton.setVisible(true);
                exitButton.setText("Exit");
                loginButton.setText("Sing In");
            }
            else{
                dispose();
            }
        });
        loginButton.addActionListener(e -> {
            if (loginField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Login cannot be empty");
            }
             else if(passwordField.getPassword().length == 0){
                    JOptionPane.showMessageDialog(null, "Password cannot be empty");
                }
             else {
                try {
                    Class.forName(driver);
                    System.out.println("1. Driver found");
                    connection = DriverManager.getConnection(url, "root", "");
                    System.out.println("2. Conection Established");
                    statement = connection.createStatement();

                    if (register) {
                        String sql = "SELECT login FROM users WHERE login = '"+loginField.getText()+"'";
                        ResultSet rs = statement.executeQuery(sql);
                        if(rs.isBeforeFirst()){
                            JOptionPane.showMessageDialog(null, "Username is already taken!");
                        }
                        else{
                            sql = "INSERT INTO users values ('','"+loginField.getText()+"','"+new String(passwordField.getPassword())+"')";
                            statement.executeUpdate(sql);
                            JOptionPane.showMessageDialog(null, "Account has been succesfully created");
                            loginField.setText("");
                            passwordField.setText("");
                        }

                    } else {
                       String sql = "SELECT * FROM users WHERE login = '"+loginField.getText()+"'";
                        ResultSet rs = statement.executeQuery(sql);
                        if(rs.first()) {
                            if (rs.getString("passw").equals(new String(passwordField.getPassword()))) {
                                user = new User(rs.getString(2), rs.getString(3), rs.getLong(1));
                                openMenu(user);

                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid login or password");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Invalid login or password");
                        }


                    }
                    connection.close();

                } catch (ClassNotFoundException ce) {
                    System.err.println("Bad or invalid driver");
                } catch (SQLException sqle) {
                    System.err.println("Error with connecting to database");
                    JOptionPane.showMessageDialog(null,"Error has occured while trying to connect do database");
                    sqle.printStackTrace();
                }

            }
        });
    }
    private void openMenu(User user){
        QuizMenu quizMenu = new QuizMenu(user);
        quizMenu.setLocation(this.getLocation());
        dispose();
        quizMenu.setVisible(true);

    }
}
