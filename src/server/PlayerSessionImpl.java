package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
    public int getPlayer(String username) throws RemoteException {
        //for now let's just return 5
        System.out.println(username);
        return 5;
    }

    public int createPlayer(String username, int age, String location ) throws RemoteException {
        //for now let's just return 4
        return 4;
    }
    /**
     * Load the file and populate the users, quizzes and results
     * @param fileName Enables you to specify a filename.
     */
    private void loadFile(String fileName) {
        quizData = file.readFile(fileName);
        loadUsers();
    }

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

}