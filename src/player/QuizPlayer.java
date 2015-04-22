package player;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class QuizPlayer {
    Scanner sc;
    Player player;
    int playerId;
    public static void main(String[] args) {
        QuizPlayer session = new QuizPlayer();
        session.begin();
    }

    private void begin(){
        sc = new Scanner(System.in);
        try {
            // fire to localhost port 1099
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            player = (Player) reg.lookup("player");
            playerId = userNameCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Asks the user whether they are already registered on the system. If not it registers them.
     */
    private int userNameCheck() throws RemoteException {
        System.out.println("Do you already have a username? Enter y if yes and n if no");
        String input = sc.nextLine();
        int id;
        switch (input) {
            case "y" :
                id = player.getPlayer(getUserName(false));
                break;
            case "n" :
                System.out.println("Please register below.....");
                id = player.createPlayer(getUserName(true), getIntFromUser("Please enter your age"), getStringFromUser("Please enter your location"));
                break;
            default:
                System.out.println("I didn't understand that. Please try again!");
                id = userNameCheck();
        }
        return id;
    }
    /**
     * Gets the username of the user (and the details if they are registering)
     * @param newUser Whether this is a new player or not
     * @return the players username
     */
    private String getUserName(boolean newUser) {
        String username = getStringFromUser("Please enter your "+((newUser)?"preferred":"")+" username");
        System.out.println("Please enter your "+((newUser)?"preferred":"")+" username");
        return username;
    }
    /**
     * Gets a string input from the user - does some checking to see if it is valid
     * @param output  The message to prompt the user
     * @return the input string from the user.
     */
    private String getStringFromUser(String output) {
        String input;
        System.out.println(output);
        input = sc.nextLine();
        if ((input == null) || (input == "")) {
            System.out.println("Please enter something!");
            input = getStringFromUser(output);
        } else if (input.length() > 20) {
            System.out.println("That's too long, I'm afraid. Needs to be under 20 characters.");
            input = getStringFromUser(output);
        }
        return input;
    }
    /**
     * Gets a integer input from the user - does some checking to see if it is valid
     * @param output  The message to prompt the user
     * @return the input int from the user.
     */
    private int getIntFromUser(String output) {
        int input;
        System.out.println(output);
        input = sc.nextInt();
        return input;
    }

}