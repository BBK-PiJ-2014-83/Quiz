package server;

import java.util.ArrayList;

public class Quiz {
    ArrayList<Question> questions;
    int id;
    int creatorId;
    String title;

    public Quiz() {
        questions = new ArrayList<Question>();
    }
    public Quiz(int id, int creatorId, String title) {
        this();
        this.id = id;
        this.creatorId = creatorId;
        this.title = title;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setCreatorId(int id){
        this.creatorId = id;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
    /**
     * Add a question to the quiz
     * @param  of the user that you wish to load.
     * @return the id of the user
     */
    public boolean addQuestion(Question question) {
        this.questions.add(question);
        return true;
    }

}
