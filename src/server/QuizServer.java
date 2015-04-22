package server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import player.PlayerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class QuizServer {
    XmlFile file;
    Document quizData;
    ArrayList<User> users;
    private void begin(){
        //First load xml file with user and quiz data
        file = new XmlFile();
        loadFile("data.xml");
        try {
            // port 1099 is default. Create on there.
            Registry registry = LocateRegistry.createRegistry(1099);
            //Create a new player!
            registry.rebind("player", new PlayerImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Load the file and populate the users, quizzes and results
     * @param fileName Enables you to specify a filename.
     */
    public void loadFile(String fileName) {
        quizData = file.readFile(fileName);
        loadUsers();

    }
    public void loadUsers() {
        NodeList userList = file.getItems("user", quizData);
        for (int i = 0; i < userList.getLength() ; i++) {
            int id, age;
            String username, location;

            for (int j = 0; j < userList.item(i).getChildNodes().getLength() ; j++) {
                switch
            }

        }
    }
    public static void main(String[] args) {
        QuizServer server = new QuizServer();
        server.begin();
    }
}