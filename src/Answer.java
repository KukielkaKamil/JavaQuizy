public class Answer {
    public String text;
    public boolean isCorrect;
    private boolean isSelected = false;
    private int id;

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public boolean isSelected(){
        return isSelected;
    }
    public void setSelected(boolean selected){
        isSelected=selected;
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
