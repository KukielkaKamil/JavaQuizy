public class Quiz {
    private final  String name;
    private final String owner;
    private final Question[] questions;

    public Quiz(String name, String owner, Question[] questions) {
        this.name = name;
        this.owner = owner;
        this.questions = questions;
    }



    public Question[] getQuestions() {
        return questions;
    }


}
