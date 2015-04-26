package player;
import server.Question;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface PlayerSession extends Remote {
    //Used to get the player at the beginning of the session. returns a player id.
     int getPlayer(String username) throws RemoteException;
    //Used to create a player if one doesn't exist. Returns a player id.
    int createPlayer(String username, int age, String location) throws RemoteException;
    //Used to get a list of the quizzes available
    List<String> getQuizList() throws RemoteException;
    //Used to get the questions for a quiz
    ArrayList<Question> getQuizQuestions(int id) throws RemoteException;
    //Used to record scores
    void recordScore(int quizId,int userId, int score) throws RemoteException;
}