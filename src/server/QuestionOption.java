package server;

import java.io.Serializable;

public class QuestionOption implements Serializable {
    String text;
    public QuestionOption(){
    }
    public QuestionOption(String text){
        this.text = text;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText() {return this.text;}
}
