package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import player.PlayerSession;


public class PlayerSessionImpl extends UnicastRemoteObject implements PlayerSession {
    XmlFile file;
    Document quizData;
    ArrayList<User> users;
    ArrayList<Quiz> quizzes;
    public PlayerSessionImpl() throws RemoteException {
        //First load xml file with user and quiz data
        file = new XmlFile();
        users = new ArrayList<User>();
        quizzes = new ArrayList<Quiz>();
        loadFile("data");
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getPlayer(String username) throws RemoteException,IllegalArgumentException {

        Optional<User> user = users.stream().
                filter(p -> (p.getUsername().toLowerCase().equals(username.toLowerCase()))).
                findFirst();
        if(!(user.isPresent())) {
            throw new IllegalArgumentException("This username doesn't exist! Please try again.");
        }
        return user.get().getId();
    }

    public int createPlayer(String username, int age, String location ) throws RemoteException,IllegalArgumentException {
        Optional<User> user = users.stream().
                filter(p -> (p.getUsername().toLowerCase().equals(username.toLowerCase()))).
                findFirst();
        if(user.isPresent()) {
            throw new IllegalArgumentException("This username is taken. Please try again.");
        }
        try{
            User newUser = new User(users.size(), username, age, location);
            return newUser.getId();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    /**
     * Return a list of quizzes so the user can choose
     * @return quizList The full list of quizzes available.
     */
    public List<String> getQuizList() {
        List<String> quizList = this.quizzes.stream().map(item -> item.getId() + " - " + item.getTitle()).collect(Collectors.toList());
        for (int i = 0; i < quizList.size(); i++) {
            System.out.println(quizList.get(i));
        }
        return quizList;
    }

    /**
     * Return a list of questions for a particular quiz so the user can choose
     * @param id The id of the quiz that you are getting the questions for
     * @return questionList The full list of questions for that quiz
     */
    public ArrayList<Question> getQuizQuestions(int id) {
        ArrayList<Question> questionList = this.quizzes.get(id).getQuestions();
        return questionList;
    }
    /**
     * Load the file and populate the users, quizzes and results
     * @param fileName Enables you to specify a filename.
     */
    private void loadFile(String fileName) {
        quizData = file.readFile(fileName);
        loadUsers();
        loadQuizzes();
    }
    /**
     * From the file that has been loaded, take all the users and populate the user array list
     * */
    private void loadUsers() {
        NodeList userList = file.getItems("user", quizData);
        for (int i = 0; i < userList.getLength() ; i++) {
            int id = 0, age = 0;
            String username ="", location = "";
            NodeList user = userList.item(i).getChildNodes();
            for (int j = 0; j < user.getLength(); j++) {
                Node detail = user.item(j);
                switch (detail.getNodeName()) {
                    case "id":
                        id = Integer.parseInt(detail.getTextContent());
                        break;
                    case "age":
                        age = Integer.parseInt(detail.getTextContent());
                    case "username":
                        username = detail.getTextContent();
                        break;
                    case "location":
                        location = detail.getTextContent();
                }
            }
            users.add(new User(id,username,age,location));
        }
    }
    /**
     * From the file that has been loaded, take all the quizzes and populate the quiz array list
     * */
    private void loadQuizzes() {

        NodeList quizList = file.getItems("quiz", quizData);
        //first loop through each quiz
        for (int i = 0; i < quizList.getLength() ; i++) {
            NodeList quiz = quizList.item(i).getChildNodes();
            Quiz tmpQuiz = new Quiz();

            for (int j = 0; j < quiz.getLength(); j++) {
                //Create an empty quiz and populate as we go through
                Node detail = quiz.item(j);
                switch (detail.getNodeName()) {
                    case "id":
                        tmpQuiz.setId(Integer.parseInt(detail.getTextContent()));
                        break;
                    case "creatorId":
                        tmpQuiz.setCreatorId(Integer.parseInt(detail.getTextContent()));
                        break;
                    case "title":
                        tmpQuiz.setTitle(detail.getTextContent());
                        break;
                    case "questions":
                        //Loop through the questions
                        NodeList questionList = detail.getChildNodes();
                        for (int k = 0; k < questionList.getLength(); k++) {
                            //Loop through the question fields
                            NodeList questionFlds = questionList.item(k).getChildNodes();
                            Question tmpQuestion = new Question();

                            for (int l = 0; l < questionFlds.getLength(); l++) {
                                Node questionFld = questionFlds.item(l);
                                switch (questionFld.getNodeName()) {
                                    case "text":
                                        tmpQuestion.setText(questionFld.getTextContent());
                                        break;
                                    case "answer":
                                        tmpQuestion.setAnswer(Integer.parseInt(questionFld.getTextContent()));
                                        break;
                                    case "options":
                                        NodeList optionList = questionFld.getChildNodes();
                                        for (int m = 0; m < optionList.getLength(); m++) {
                                            QuestionOption tmpOption = new QuestionOption();
                                            Node option = optionList.item(m).getFirstChild();
                                            tmpOption.setText(option.getTextContent());
                                            tmpQuestion.addOption(tmpOption);
                                        }
                                        break;
                                }
                            }
                            tmpQuiz.addQuestion(tmpQuestion);
                        }
                }
            }
            this.quizzes.add(tmpQuiz);
        }
    }
    /**
     * Saves everything back to the xml file for persistence. This should be done after every new user is created, every new quiz is created and each result is stored.
     * */
    private void saveAll() {

    }
}