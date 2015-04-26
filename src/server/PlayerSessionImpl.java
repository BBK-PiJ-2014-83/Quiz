package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import player.PlayerSession;

public class PlayerSessionImpl extends UnicastRemoteObject implements PlayerSession {
    QuizServer server;
    UserFunction user;
    public PlayerSessionImpl(QuizServer server) throws RemoteException {
        //First load xml file with user and quiz data
        this.server = server;
        this.user = new UserFunction(server);
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getPlayer(String username) throws RemoteException,IllegalArgumentException {
        return user.getUser(username);
    }
    /**
     * Creates a new user  as a player
     * @param username of the user that you wish to create.
     * @param age The age of the user that you wish to create.
     * @param location The location of the user that you wish to create.
     * @return the id of the user
     */
    public int createPlayer(String username, int age, String location ) throws RemoteException,IllegalArgumentException {
        return user.createUser(username,age,location);
    }
    /**
     * Return a list of quizzes that are open so the user can choose one
     * @return quizList The full list of quizzes available.
     */
    public List<String> getQuizList() {
        //Uses collections to bring back a list of quizzes that are open.
        List<String> quizList = server.getQuizzes().stream().filter(quiz -> quiz.getStatus() == "open").map(item -> item.getId() + " - " + item.getTitle()).collect(Collectors.toList());
        return quizList;
    }

    /**
     * Return a list of questions for a particular quiz so the user can choose
     * @param id The id of the quiz that you are getting the questions for
     * @return questionList The full list of questions for that quiz
     */
    public ArrayList<Question> getQuizQuestions(int id) {
        ArrayList<Question> questionList = server.getQuizzes().get(id).getQuestions();
        return questionList;
    }
    /**
     * Record a score after they finish a quiz. Then it saves everything.
     * @param quizId The id of the quiz that the user has attempted
     * @param userId The id of the user who attempted the quiz
     * @param score The final score of the attempt
     */
    public void recordScore(int quizId, int userId, int score) throws RemoteException {
        Result result = new Result(quizId,userId, score);
        server.getResults().add(result);
        server.saveAll();
    }

}