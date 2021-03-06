package server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class QuizServer {
    XmlFile file;
    Document quizData;
    ArrayList<User> users;
    ArrayList<Quiz> quizzes;
    ArrayList<Result> results;
    public QuizServer() {
        //First load xml file with user and quiz data
        file = new XmlFile();
        users = new ArrayList<User>();
        quizzes = new ArrayList<Quiz>();
        results = new ArrayList<Result>();
        loadFile("data");
    }
    //Start of getters and setters
    public ArrayList<User> getUsers() {return this.users;}
    public ArrayList<Quiz> getQuizzes() {return this.quizzes;}
    public ArrayList<Result> getResults() {return this.results;}

    private void begin(){

        try {
            // port 1099 is default. Create on there.
            Registry registry = LocateRegistry.createRegistry(1099);
            //Create a new player session!
            registry.rebind("playerSession", new PlayerSessionImpl(this));
            registry.rebind("setupSession", new SetupSessionImpl(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer();
        server.begin();
    }
    /**
     * Load the file and populate the users, quizzes and results
     * @param fileName Enables you to specify a filename.
     */
    private void loadFile(String fileName) {
        quizData = file.readFile(fileName);
        loadUsers();
        loadQuizzes();
        loadResults();
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
                        break;
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
                    case "status":
                        tmpQuiz.setStatus(detail.getTextContent());
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
     * From the file that has been loaded, take all the results and populate the user array list
     * */
    private void loadResults() {
        NodeList resultList = file.getItems("result", quizData);
        for (int i = 0; i < resultList.getLength() ; i++) {
            int userId = 0, quizId = 0, result=0;
            NodeList resultItems = resultList.item(i).getChildNodes();
            for (int j = 0; j < resultItems.getLength(); j++) {
                Node detail = resultItems.item(j);
                switch (detail.getNodeName()) {
                    case "userid":
                        userId = Integer.parseInt(detail.getTextContent());
                        break;
                    case "quizid":
                        quizId = Integer.parseInt(detail.getTextContent());
                    case "score":
                        result = Integer.parseInt(detail.getTextContent());
                        break;
                }
            }
            results.add(new Result(quizId,userId,result));
        }
    }
    /**
     * Saves everything back to the xml file for persistence. This should be done after every new user is created, every new quiz is created and each result is stored.
     * */
    public void saveAll() {
        try {
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = docBuilder.newDocumentBuilder();
            Document quizData = dbuilder.newDocument();
            Element rootElement = quizData.createElement("quizData");
            //Add the root element
            quizData.appendChild(rootElement);
            Element usersXML =  quizData.createElement("users");
            //Loop through each user adding it to the document.
            for (User user : users) {
                Element tmpUser=  quizData.createElement("user");
                file.createNode(quizData,"id",Integer.toString(user.getId()), tmpUser);
                file.createNode(quizData,"username",user.getUsername(),tmpUser);
                file.createNode(quizData,"location",user.getLocation(),tmpUser);
                file.createNode(quizData,"age",Integer.toString(user.getAge()), tmpUser);
                usersXML.appendChild(tmpUser);
            }
            rootElement.appendChild(usersXML);
            //now deal with the quizzes themselves. Urgh.....
            Element quizzesXML =  quizData.createElement("quizzes");
            for (Quiz quiz : quizzes) {
                Element tmpQuiz =  quizData.createElement("quiz");
                file.createNode(quizData,"id",Integer.toString(quiz.getId()),tmpQuiz);
                file.createNode(quizData,"status",quiz.getStatus(),tmpQuiz);
                file.createNode(quizData,"creatorId",Integer.toString(quiz.getCreatorId()), tmpQuiz);
                file.createNode(quizData,"title",quiz.getTitle(),tmpQuiz);

                Element questions = quizData.createElement("questions");
                //Loop through to get each question
                for(Question question: quiz.getQuestions()) {
                    Element tmpQuestion = quizData.createElement("question");
                    file.createNode(quizData,"text", question.getText(), tmpQuestion);
                    file.createNode(quizData,"answer", Integer.toString(question.getAnswer()), tmpQuestion);
                    Element options = quizData.createElement("options");
                    //Now a final loop to get each option for the question
                    for(QuestionOption option : question.getOptions()) {
                        file.createNode(quizData,"option", option.getText(), options);
                    }
                    tmpQuestion.appendChild(options);
                    questions.appendChild(tmpQuestion);
                }
                tmpQuiz.appendChild(questions);
                quizzesXML.appendChild(tmpQuiz);
            }
            rootElement.appendChild(quizzesXML);
            //Now past meetings
            Element resultsXML =  quizData.createElement("results");
            for (Result result : results) {
                Element tmpResult =  quizData.createElement("result");
                file.createNode(quizData,"userid",Integer.toString(result.getUserId()),tmpResult);
                file.createNode(quizData,"quizid",Integer.toString(result.getQuizId()), tmpResult);
                file.createNode(quizData,"score",Integer.toString(result.getResult()), tmpResult);
                resultsXML.appendChild(tmpResult);
            }
            rootElement.appendChild(resultsXML);
            System.out.println((file.saveFile("data",quizData)) ? "File saved!" : "File save didn't work!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}