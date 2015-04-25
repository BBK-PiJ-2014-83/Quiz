package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import player.PlayerSession;


public class PlayerSessionImpl extends UnicastRemoteObject implements PlayerSession {
    XmlFile file;
    Document quizData;
    ArrayList<User> users;
    public PlayerSessionImpl() throws RemoteException {
        //First load xml file with user and quiz data
        file = new XmlFile();
        users = new ArrayList<User>();
        loadFile("data");
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getPlayer(String username) throws RemoteException,IllegalArgumentException {
        //for now let's just return 5
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
     * Load the file and populate the users, quizzes and results
     * @param fileName Enables you to specify a filename.
     */
    private void loadFile(String fileName) {
        quizData = file.readFile(fileName);
        loadUsers();
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
     * Saves everything back to the xml file for persistence. This should be done after every new user is created, every new quiz is created and each result is stored.
     * */
    private void saveAll() {

    }
}