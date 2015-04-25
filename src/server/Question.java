package server;

import java.util.ArrayList;

public class Question {
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

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean addOption(QuestionOption option){
        this.options.add(option);
        return true;
    }
}
