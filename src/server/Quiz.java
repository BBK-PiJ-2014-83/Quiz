package server;

import java.util.ArrayList;

public class Quiz {
    ArrayList<Question> questions;
    int id;
    int creatorId;
    String title;
    String status;

    public Quiz() {
        questions = new ArrayList<Question>();
    }
    public Quiz(int id, int creatorId, String title) {
        this();
        this.id = id;
        this.creatorId = creatorId;
        this.title = title;
        this.status = "open";
    }
    public void setId(int id){
        this.id = id;
    }
    public void setCreatorId(int id){
        this.creatorId = id;
    }
    public int getCreatorId(){
        return this.creatorId;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public int getId() { return this.id;}
    public String getTitle() {
        return this.title;
    }
    public ArrayList<Question> getQuestions() {return this.questions;}
    public void setStatus(String status) {this.status = status;}
    public String getStatus() { return status;}

    /**
     * Add a question to the quiz
     * @param question that is going to be added to the list of questions
     * @return Whether the operation has been successful
     */
    public boolean addQuestion(Question question) {
        this.questions.add(question);
        return true;
    }

}
