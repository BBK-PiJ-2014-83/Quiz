package server;

public class Result {
    int quizId;
    int userId;
    int result;
    public Result(int quizId, int userId, int result) {
        this.quizId = quizId;
        this.userId = userId;
        this.result = result;
    }
    public int getUserId(){
        return this.userId;
    }
    public int getQuizId(){
        return this.quizId;
    }
    public int getResult() { return this.result;}
}
