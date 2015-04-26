package server;

import setup.SetupSession;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SetupSessionImpl extends UnicastRemoteObject implements SetupSession {
    QuizServer server;
    UserFunction user;
    public SetupSessionImpl(QuizServer server) throws RemoteException {
        this.server = server;
        this.user = new UserFunction(server);
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getUser(String username) throws RemoteException,IllegalArgumentException {
        return user.getUser(username);
    }
    /**
     * Creates a new user either as a player or setup person
     * @param username of the user that you wish to create.
     * @param age The age of the user that you wish to create.
     * @param location The location of the user that you wish to create.
     * @return the id of the user
     */
    public int createUser(String username, int age, String location ) throws RemoteException,IllegalArgumentException {
        return user.createUser(username, age, location);
    }
    /**
     * Creates a new quiz
     * @param title The title of the quiz
     * @param id The creator's user id.
     * @return the id of the quiz
     */
    public int createQuiz(String title, int id) throws RemoteException{
        int quizId = server.getQuizzes().size();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setCreatorId(id);
        quiz.setId(quizId);
        quiz.setStatus("open");
        server.getQuizzes().add(quiz);
        return quizId;
    }
    /**
     * Adds a question to a quiz that already exists
     * @param quizId The quiz id
     * @param text The text of the question
     * @return the item number of the question in the arrayList
     */
    public int addQuestion( int quizId, String text) throws RemoteException{
        int questionId = server.getQuizzes().get(quizId).getQuestions().size();
        Question question = new Question();
        question.setText(text);

        server.getQuizzes().get(quizId).addQuestion(question);
        return questionId;
    }
    /**
     * Adds the answer to a question
     * @param quizId The quiz id
     * @param questionId  The id of the question
     * @param answer The option which is the answer.
     * @return the item number of the question in the arrayList
     */
    public void setAnswer( int quizId, int questionId, int answer) throws RemoteException{
        server.getQuizzes().get(quizId).getQuestions().get(questionId).setAnswer(answer);
    }
    /**
     * Adds a question to a quiz that already exists
     * @param quizId The quiz id
     * @param questionId The item number of the question in the array
     * @param text The text of the option
     * @return the item number of the option in the arrayList
     */
    public int addOption( int quizId, int questionId, String text) throws RemoteException{
        int optionId = server.getQuizzes().get(quizId).getQuestions().get(questionId).getOptions().size();
        QuestionOption option = new QuestionOption();
        option.setText(text);
        server.getQuizzes().get(quizId).getQuestions().get(questionId).addOption(option);
        return optionId;
    }
    /**
     * Simply saves everything.
     */
    public void saveAll() throws RemoteException{
        server.saveAll();
    }

}
