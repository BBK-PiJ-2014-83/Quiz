package setup;
import server.Question;
import server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SetupSession extends Remote {
    //Used to get the user at the beginning of the session. returns a user id.
    public int getUser(String username) throws RemoteException,IllegalArgumentException;
    //Used to create a user if one doesn't exist. Returns a user id.
    int createUser(String username, int age, String location) throws RemoteException;
    //Used to create a quiz
    int createQuiz(String title, int id) throws RemoteException;
    //Used to add a question to a quiz that already exists
    int addQuestion(int id, String text) throws RemoteException;
    //Used to add a question to a quiz that already exists
    void setAnswer(int quizId, int questionId, int answer) throws RemoteException;
    //Used to add an option to a question that already exists
    int addOption( int quizId, int questionId, String text) throws RemoteException;
    //Save a quiz
    void saveAll() throws RemoteException;
}