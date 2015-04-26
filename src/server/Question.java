package server;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
    ArrayList<QuestionOption> options;
    String text;
    int answer;

    public Question() {
        options = new ArrayList<QuestionOption>();

    }
    public Question(String text, int answer) {
        this();
        this.text = text;
        this.answer = answer;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {return this.text;}
    public void setAnswer(int answer) {
        this.answer = answer;
    }
    public int getAnswer() {return this.answer;}
    public boolean addOption(QuestionOption option){
        this.options.add(option);
        return true;
    }
    public ArrayList<QuestionOption> getOptions() {return this.options;}
}
