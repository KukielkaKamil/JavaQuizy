import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizBrowser extends JFrame {
    private JButton solveQuizButton;
    private JButton addQuizButton;
    private JButton deleteQuizButton;
    private JButton editQuizButton;
    private JTable table1;
    private JPanel mainPanel;
    private JLabel label;
    private JButton goBackButton;
    private final static String driver = "org.mariadb.jdbc.Driver";
    private final static String url = "jdbc:mariadb://localhost:3306/java_quizy_example_database";
    private static Connection connection;
    private static Statement statement;
    private final BrowserMode currMode;
    private final static ArrayList<Integer> ids = new ArrayList<>();
    public static int curQuizID = 0;
    private static String quizName;
    private final static ArrayList<Question> questions =  new ArrayList<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public QuizBrowser(BrowserMode browserMode){
        super("Java Quiz");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(600,500);
        currMode=browserMode;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, "root", "");
            statement = connection.createStatement();

            switch (browserMode) {
                case BrowseQuiz -> {
                    label.setText("Browse Quizzes");
                    addQuizButton.setVisible(false);
                    deleteQuizButton.setVisible(false);
                    editQuizButton.setVisible(false);
                    String sql ="SELECT name, login FROM quizes JOIN users USING(id_user);";
                    ResultSet rs = statement.executeQuery(sql);
                    tableModel.addColumn("quiz name");
                    tableModel.addColumn("author");
                    while (rs.next()){
                        tableModel.addRow(new String[]{rs.getString(1),rs.getString(2)});
                    }
                    table1.setCellSelectionEnabled(false);
                    table1.setRowSelectionAllowed(true);
                    table1.setModel(tableModel);
                }
                case ManageQuiz->{
                    label.setText("Manage your quizzes");
                    ids.clear();
                    solveQuizButton.setVisible(false);
                    String sql ="SELECT name, id_quiz FROM quizes JOIN users USING(id_user) WHERE login = '"+Login.user.getLogin()+"';";
                    ResultSet rs = statement.executeQuery(sql);
                    tableModel.addColumn("quiz name");
                    while (rs.next()){
                        tableModel.addRow(new String[]{rs.getString(1)});
                        ids.add(rs.getInt(2));
                    }
                    table1.setCellSelectionEnabled(false);
                    table1.setRowSelectionAllowed(true);
                    table1.setModel(tableModel);

                }
                case AddQuiz -> {
                    label.setText("Quiz manager");
                    solveQuizButton.setText("Save");
                    addQuizButton.setText("Add Question");
                    deleteQuizButton.setText("Delete Question");
                    editQuizButton.setText("Edit question");
                    tableModel.addColumn("Question");
                    table1.setModel(tableModel);

                }
                case EditQuiz -> {
                    label.setText("Quiz manager");
                    solveQuizButton.setText("Save");
                    addQuizButton.setText("Add Question");
                    deleteQuizButton.setText("Delete Question");
                    editQuizButton.setText("Edit question");
                    questions.clear();
                    tableModel.addColumn("Question");
                    int id = ids.get(curQuizID);
                    String sql ="SELECT * FROM quizes JOIN questions USING(id_quiz) JOIN answers USING(id_question) WHERE id_quiz="+id+"";
                    ResultSet quizzes = statement.executeQuery(sql);
                    while (quizzes.next()){
                        int answersCount = quizzes.getInt("answer_count");
                        Answer[] answers = new Answer[answersCount];
                        for(int i=0; i<answersCount;i++){

                            answers[i]= new Answer(quizzes.getString("answer"),quizzes.getBoolean("is_correct"));
                            answers[i].setId(quizzes.getInt("id_answer"));
                            quizzes.next();
                        }
                        quizzes.previous();
                        Question question = new Question(quizzes.getString("question"),answers,getType(quizzes.getInt("id_type")));
                        question.setId(quizzes.getInt("id_question"));
                        tableModel.addRow(new Question[]{question});
                        questions.add(question);
                    }
                    table1.setModel(tableModel);

                }


            }

        }catch (ClassNotFoundException ce) {
            System.err.println("Bad or invalid driver");
        } catch (SQLException sqle) {
            System.err.println("Error with connecting to database");
            sqle.printStackTrace();
        }
        solveQuizButton.addActionListener(e -> {

            if(tableModel.getRowCount()<=0){
                JOptionPane.showMessageDialog(null,"Quiz must contain at least one question");
                return;
            }


            switch (currMode){
                case BrowseQuiz -> solveQuiz(); //Solve seleted quiz
                case AddQuiz -> {
                    try {
                    //Save quiz in database
                    int length = tableModel.getRowCount();
                    String sql1 = "INSERT INTO quizes (name, id_user) values('"+quizName+"',"+Login.user.getUser_id()+")";

                        PreparedStatement statement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                        statement1.execute();
                        ResultSet rs1 = statement1.getGeneratedKeys();
                        rs1.first();
                        int quizId = rs1.getInt(1);
                        String sql2 = "INSERT INTO questions (question, id_type, id_quiz,answer_count)" +
                                "VALUES(?, ? ,"+quizId+",?)";
                        PreparedStatement statement2 = connection.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
                        String sql3 = "INSERT INTO answers (answer,id_question,is_correct) VALUES(?,?,?)";
                        PreparedStatement statement3 = connection.prepareStatement(sql3);

                        Question currQuestion;
                        int currQuestionIndex;

                        for(int i = 0; i < length;i++){
                            currQuestion = (Question) tableModel.getValueAt(i,0);
                            statement2.setString(1,currQuestion.getText());
                            statement2.setInt(2,typeToInt(currQuestion.getQuestionType()));
                            statement2.setInt(3,currQuestion.getAnswers().length);
                            statement2.execute();
                            ResultSet question = statement2.getGeneratedKeys();
                            question.first();
                            currQuestionIndex=question.getInt(1);
                            for(int j = 0;j<currQuestion.getAnswers().length;j++){
                                statement3.setString(1,currQuestion.getAnswers()[j].text);
                                statement3.setInt(2,currQuestionIndex);
                                statement3.setBoolean(3,currQuestion.getAnswers()[j].isCorrect);
                                statement3.execute();
                            }

                        }
                        openMenu(Login.user);
                    }catch (SQLException sqlException){
                        JOptionPane.showMessageDialog(null,"Error has occured while trying to connect to database");
                    }
                }
                case EditQuiz -> {
                    //save changes to existing quiz
                    //"UPDATE answers SET answer = '"+answer.text+"', is_correct ="+answer.isCorrect+" WHERE"
                    int oldLength = questions.size();
                    Question test = questions.get(0);
                    Question test2 = (Question) tableModel.getValueAt(0,0);
                    int newLength = tableModel.getRowCount();
                    try {
                        int i = 0;
                        while (i < oldLength && i < newLength) {
                            Question oldQuestion = questions.get(i);
                            Question newQuestion = (Question) tableModel.getValueAt(i, 0);
                            if (!oldQuestion.getText().equals(newQuestion.getText())) {
                                String sql1 = "UPDATE questions SET question = ? WHERE id_question = " + oldQuestion.getId();
                                PreparedStatement stm1 = connection.prepareStatement(sql1);
                                stm1.setString(1,newQuestion.getText());
                                stm1.execute();
                            }
                            if (oldQuestion.getQuestionType() != newQuestion.getQuestionType()) {
                                String sql2 = "UPDATE questions SET id_type = ? WHERE id_question = " + oldQuestion.getId();
                                PreparedStatement stm2 = connection.prepareStatement(sql2);
                                stm2.setInt(1,typeToInt(newQuestion.getQuestionType()));
                                stm2.execute();
                            }
                            if(oldQuestion.getQuestionCount() != newQuestion.getQuestionCount()){
                                String sql2 = "UPDATE questions SET answer_count = ? WHERE id_question = " + oldQuestion.getId();
                                PreparedStatement stm2 = connection.prepareStatement(sql2);
                                stm2.setInt(1,newQuestion.getQuestionCount());
                                stm2.execute();
                            }
                            int j = 0;
                            while (j < oldQuestion.getQuestionCount() && j < newQuestion.getQuestionCount()) {
                                if (!oldQuestion.getAnswers()[j].text.equals(newQuestion.getAnswers()[j].text)) {
                                    String sql3 = "UPDATE answers SET answer = ? WHERE id_answer = " + oldQuestion.getAnswers()[j].getId();
                                    PreparedStatement stm3 = connection.prepareStatement(sql3);
                                    stm3.setString(1,newQuestion.getAnswers()[j].text);
                                    stm3.execute();
                                }
                                if (oldQuestion.getAnswers()[j].isCorrect != newQuestion.getAnswers()[j].isCorrect) {
                                    String sql4 = "UPDATE answers SET is_correct = ? WHERE id_answer = " + oldQuestion.getAnswers()[j].getId();
                                    PreparedStatement stm4 =connection.prepareStatement(sql4);
                                    stm4.setBoolean(1,newQuestion.getAnswers()[j].isCorrect);
                                    stm4.execute();
                                }
                                j++;
                            }
                            while (j < newQuestion.getQuestionCount()) {
                                String sql5 = "INSERT INTO answers (answer, id_question, is_correct) VALUES (?,?,?)";
                                PreparedStatement stm5 = connection.prepareStatement(sql5);
                                stm5.setString(1,newQuestion.getAnswers()[j].text);
                                stm5.setInt(2,oldQuestion.getId());
                                stm5.setBoolean(3,newQuestion.getAnswers()[j].isCorrect);
                                stm5.execute();
                                j++;
                            }
                            while (j<oldQuestion.getQuestionCount()){
                                String sql6 = "DELETE FROM answers WHERE id_answer = "+oldQuestion.getAnswers()[j].getId();
                                PreparedStatement stm6 = connection.prepareStatement(sql6);
                                stm6.execute();
                                j++;
                            }
                            i++;
                        }
                        while(i < newLength){
                            Question question = (Question) tableModel.getValueAt(i, 0);
                            String sql7 = "INSERT INTO questions (question,id_type,id_quiz,answer_count) VALUES (?,?,?,?)";
                            PreparedStatement stm7 = connection.prepareStatement(sql7,Statement.RETURN_GENERATED_KEYS);
                            stm7.setString(1,question.getText());
                            stm7.setInt(2,typeToInt(question.getQuestionType()));
                            stm7.setInt(3,ids.get(curQuizID));
                            stm7.setInt(4,question.getQuestionCount());
                            stm7.execute();
                            ResultSet rs = stm7.getGeneratedKeys();
                            rs.first();
                            int quest_id = rs.getInt(1);
                            String sql8 = "INSERT INTO answers (answer, id_question, is_correct) VALUES (?,"+quest_id+",?)";
                            PreparedStatement stm8 =connection.prepareStatement(sql8);
                            for(int k=0 ;k<question.getQuestionCount();k++){
                                stm8.setString(1,question.getAnswers()[k].text);
                                stm8.setBoolean(2,question.getAnswers()[k].isCorrect);
                                stm8.execute();
                            }
                            i++;
                        }
                        while (i<oldLength){
                            Question question = questions.get(i);
                            String sql9 = "DELETE FROM questions WHERE id_question = "+question.getId();
                            PreparedStatement stm9 = connection.prepareStatement(sql9);
                            stm9.execute();
                            i++;
                        }
                        openMenu(Login.user);

                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                }
            }

        });
        goBackButton.addActionListener(e -> openMenu(Login.user));
        addQuizButton.addActionListener(e -> {
            switch (currMode){
                case ManageQuiz -> {
                    quizName = JOptionPane.showInputDialog("Name your quiz");
                    if(!quizName.isBlank()) addQuiz();
                    else JOptionPane.showMessageDialog(null,"Quiz name cannot be empty");
                }
                case AddQuiz, EditQuiz -> addQuestion(false);


            }
        });
        table1.addComponentListener(new ComponentAdapter() {
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = table1.getSelectedRow();
                if(x>=0){
                    deleteQuizButton.setEnabled(true);
                    editQuizButton.setEnabled(true);
                }
                else{
                    deleteQuizButton.setEnabled(false);
                    editQuizButton.setEnabled(false);
                }
            }
        });
        deleteQuizButton.addActionListener(e -> {
            switch (currMode){
                case AddQuiz, EditQuiz -> {
                    int x = table1.getSelectedRow();
                    tableModel.removeRow(x);
                    deleteQuizButton.setEnabled(false);
                    editQuizButton.setEnabled(false);
                }
                case ManageQuiz -> {
                    try {
                        String delete = "DELETE FROM quizes WHERE id_quiz = ?";
                        PreparedStatement deleteStm = connection.prepareStatement(delete);
                        deleteStm.setInt(1,ids.get(table1.getSelectedRow()));
                        ids.remove(table1.getSelectedRow());
                        deleteStm.execute();
                        tableModel.removeRow(table1.getSelectedRow());
                    }catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                }
            }
        });
        editQuizButton.addActionListener(e -> {
            switch (currMode){
                case ManageQuiz -> {
                    //editing existing quizzes
                    curQuizID = table1.getSelectedRow();
                    editQuiz();
                }
                case AddQuiz, EditQuiz -> {
                    addQuestion(true);
                    deleteQuizButton.setEnabled(false);
                    editQuizButton.setEnabled(false);
                }
            }
        });
    }
    public void solveQuiz(){
        int x =table1.getSelectedRow();
        if(x<0) JOptionPane.showMessageDialog(null,"Choose quiz first");
        else{
            try {
                String sql1 = "SELECT id_quiz FROM quizes JOIN users USING(id_user) " +
                        "WHERE name='"+tableModel.getValueAt(x,0)+"' AND " +
                        "login = '"+tableModel.getValueAt(x,1)+"'";
                ResultSet getID = statement.executeQuery(sql1);
                getID.next();
                int id = getID.getInt(1);
                String sql2 = "SELECT * FROM quizes JOIN questions USING(id_quiz) JOIN answers USING(id_question) WHERE id_quiz='"+id+"'";
                ResultSet quizSetup = statement.executeQuery(sql2);
                int curQuestion =0;
                ArrayList<Answer> curAnswers = new ArrayList<>();
                ArrayList<Question> questions= new ArrayList<>();
                Quiz quiz;
                while (quizSetup.next()){
                    if(quizSetup.isFirst()){
                        curQuestion = quizSetup.getInt("id_question");
                        curAnswers.add(new Answer(quizSetup.getString("answer"),quizSetup.getBoolean("is_correct")));
                        continue;
                    }
                    if(quizSetup.isLast()){
                        curAnswers.add(new Answer(quizSetup.getString("answer"),quizSetup.getBoolean("is_correct")));
                        String question = quizSetup.getString("question");
                        questions.add(new Question(question,curAnswers.toArray(new Answer[0]),getType(quizSetup.getInt("id_type"))));
                        quiz = new Quiz(tableModel.getValueAt(x,0).toString(),tableModel.getValueAt(x,1).toString(),questions.toArray(new Question[0]));
                        QuestionGUI qui = new QuestionGUI(quiz);
                        qui.setVisible(true);
                        break;
                    }
                    if(quizSetup.getInt("id_question")!=curQuestion){
                        quizSetup.previous();
                        String question = quizSetup.getString("question");
                        questions.add(new Question(question,curAnswers.toArray(new Answer[0]),getType(quizSetup.getInt("id_type"))));
                        quizSetup.next();
                        curQuestion = quizSetup.getInt("id_question");
                        curAnswers.clear();
                        curAnswers.add(new Answer(quizSetup.getString("answer"),quizSetup.getBoolean("is_correct")));
                    }
                    else{
                        curAnswers.add(new Answer(quizSetup.getString("answer"),quizSetup.getBoolean("is_correct")));
                    }

                }
                connection.close();
                dispose();
            }
            catch (SQLException sqlException){
                System.err.println("Error with connecting to database");
                JOptionPane.showMessageDialog(null,"Error has occured while trying to connect do database");
            }
        }
    }

    public void addQuiz(){
        try{
            String sqlCheck = "SELECT name FROM quizes";
            ResultSet checkSet = statement.executeQuery(sqlCheck);
            while(checkSet.next()){
                if(checkSet.getString(1).equals(quizName)){
                    JOptionPane.showMessageDialog(null,"Quiz with that name alerady exists");
                    return;
                }
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        QuizBrowser qb = new QuizBrowser(BrowserMode.AddQuiz);
        qb.setLocation(this.getLocation());
        dispose();
        qb.setVisible(true);
    }
    public void editQuiz(){
        QuizBrowser qb = new QuizBrowser(BrowserMode.EditQuiz);
        qb.setLocation(this.getLocation());
        dispose();
        qb.setVisible(true);
    }
    public QuestionType getType(int x){
        return switch (x) {
            case 2 -> QuestionType.TrueFalseQuestion;
            case 3 -> QuestionType.MultiSelectionQuestion;
            default -> QuestionType.SimpleQuestion;
        };
    }
    public int typeToInt(QuestionType qt){
        switch (qt){
            case TrueFalseQuestion -> {
                return 2;
            }
            case MultiSelectionQuestion -> {
                return 3;
            }
            default -> {
                return 1;
            }
        }
    }
    private void openMenu(User user){
        QuizMenu quizMenu = new QuizMenu(user);
        quizMenu.setLocation(this.getLocation());
        dispose();
        quizMenu.setVisible(true);

    }
    public void addQuestion(boolean edit){
        AddQuestionGUI addGUI = new AddQuestionGUI(tableModel,table1, edit);
        if(edit) addGUI.getAddQuestionButton().setText("Edit question");
        addGUI.setLocation(this.getLocation());
        addGUI.setVisible(true);
    }

}
