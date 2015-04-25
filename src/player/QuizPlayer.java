package player;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class QuizPlayer {
    Scanner sc;
    PlayerSession playerSession;
    int playerId;
    public static void main(String[] args) {
        QuizPlayer session = new QuizPlayer();

        session.begin();
    }
    /**
     * Boots everything up and welcomes the user to the program
     */
    private void begin(){
        sc = new Scanner(System.in);
        try {
            // fire to localhost port 1099
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            playerSession = (PlayerSession) reg.lookup("playerSession");
            //
            System.out.println("Welcome to the quiz program! Type exit to close at any time.");
            playerId = userNameCheck();
            System.out.println("/nThanks for joining us! Please select the quiz you would like to attempt from the list below");
            selectQuiz();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Asks the user whether they are already registered on the system. If not it registers them.
     */
    private int userNameCheck() throws RemoteException {

        System.out.println("Do you already have a username? Enter y if yes and n if no.");
        String input = sc.nextLine();
        int id;
        try{
            switch (input) {
                case "y" :
                    id = playerSession.getPlayer(getUserName(false));
                    break;
                case "n" :
                   System.out.println("Please register below.....");
                    String username = getUserName(true);
                    int age = getIntFromUser("Please enter your age");
                    String location = getStringFromUser("Please enter your location");
                    id = playerSession.createPlayer(username,age ,location);
                   break;
                default:
                    System.out.println("I didn't understand that. Please try again!");
                    id = userNameCheck();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            id = userNameCheck();
        }
        System.out.println(id);
        return id;
    }
    /**
     * Gets the username of the user (and the details if they are registering)
     * @param newUser Whether this is a new player or not
     * @return the players username
     */
    private String getUserName(boolean newUser) {
        return getStringFromUser("Please enter your "+((newUser)?"preferred ":"")+"username");
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
     * Prints the list of available quizzes and get's the user the select one.
     * @return the players username
     */
    public void selectQuiz() throws RemoteException {
        List<String> quizList = playerSession.getQuizList();
        quizList.stream().forEach(System.out::println);
    }
    /**
     * Gets a integer input from the user - does some checking to see if it is valid
     * @param output  The message to prompt the user
     * @return the input int from the user.
     */
    private int getIntFromUser(String output) {
        System.out.println(output);
        String tmpInput = sc.nextLine();
        if (isNumeric(tmpInput)){
            return Integer.parseInt(tmpInput);
        } else {
            System.out.println("That's not a valid integer. Please try again!");
            return getIntFromUser(output);
        }
    }
    /**
     * A simple method to check whether the user input is an integer or not
     * @param value The string that we are going to try to pass
     * @return whether it is a number or not
     */
    private static boolean isNumeric(String value) {
        try  {
            int test = Integer.parseInt(value);

        } catch(NumberFormatException nfe)  {
            return false;
        }
        return true;
    }

}