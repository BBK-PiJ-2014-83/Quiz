package server;

public class Result {
    int quizId;
    int userId;
    String result;
    public Result(int quizId, int userId, String result) {
        this.quizId = quizId;
        this.userId = userId;
        this.result = result;
    }
}
