package setup;

import server.Question;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizSetup {
    Scanner sc;
    SetupSession setupSession;
    int userId;
    String username;
    public static void main(String[] args) {
        QuizSetup session = new QuizSetup();
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
            setupSession = (SetupSession) reg.lookup("setupSession");
            System.out.println("Welcome to the quiz program! You are in the setup client.\nType exit to close at any time.");
            userId = userNameCheck();
            System.out.println("\nThanks for joining us, "+username+"! ");
            giveOptions();
            createQuiz();
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
                    id = setupSession.getUser(getUserName(false));
                    break;
                case "n" :
                    System.out.println("Please register below.....");
                    username = getUserName(true);
                    int age = getIntFromUser("Please enter your age");
                    String location = getStringFromUser("Please enter your location");
                    id = setupSession.createUser(username, age, location);
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
        username = getStringFromUser("Please enter your "+((newUser)?"preferred ":"")+"username");
        return username;
    }
    private void giveOptions() throws RemoteException{
        System.out.println("Please select what you would like to do from the list below :");
        System.out.println("1 - Create a new quiz.");
        System.out.println("2 - Close a quiz");
        int option = getIntFromUser("Enter number below :");
        switch (option) {
            case 1 :
                createQuiz();
                break;
            case 2 :
               // closeQuiz();
                break;
            default:
                System.out.println("I didn't understand that! Let's try again.");
                giveOptions();
        }

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
        if (input.equals("exit")) {
            System.out.println("Thanks for stopping by!");
            System.exit(0);
            return "";
        } else if ((input == null) || (input == "")) {
            System.out.println("Please enter something!");
            input = getStringFromUser(output);
        } else if (input.length() > 100) {
            System.out.println("That's too long, I'm afraid. Needs to be under 20 characters.");
            input = getStringFromUser(output);
        }
        return input;
    }
    /**
     * Prints the list of available quizzes and gets the user the select one. Once they have selected one they go to doQuiz where they attempt a quiz.
     */
    public void createQuiz() throws RemoteException {
        System.out.println("\n This is the console where you can create a new quiz!");
        String title = getStringFromUser("Please enter the name of your quiz below:");
        //Now we have the title we can create the quiz
        int quizId = setupSession.createQuiz(title,userId);
        //Get the number of questions from the user
        int quizLength = getIntFromUser("That quiz sounds great! It has an id of : " + quizId + ". How many questions will it contain?");
        for (int i = 0; i < quizLength; i++) {
            String questionText = getStringFromUser("Please enter the text of question "+(i + 1)+" below (eg. How high is mount everest?) :");
            //Now we can create the question and assign it to the quiz
            int questionId = setupSession.addQuestion(quizId,questionText);
            int optionLength = getIntFromUser("How many choices will this question have?");
            for (int j = 0; j < optionLength; j++) {
                String optionText= getStringFromUser("Please enter the text of option "+(j+1)+" below (eg. 39940ft) :");
                setupSession.addOption(quizId,questionId,optionText);
            }
            int answer = getIntFromUser("Please select which option is the correct answer for this question : ");
            setupSession.setAnswer(quizId,questionId,answer);
        }

        setupSession.saveAll();
        System.out.println("\nGreat! Your quiz has been saved!");
        giveOptions();
    }
    /**
     * Gets a integer input from the user - does some checking to see if it is valid
     * @param output  The message to prompt the user
     * @return the input int from the user.
     */
    private int getIntFromUser(String output) {
        System.out.println(output);
        String tmpInput = sc.nextLine();
        if (tmpInput.equals("exit")) {
            System.out.println("Thanks for stopping by!");
            System.exit(0);
            return 1;
        }else if (isNumeric(tmpInput)){
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
