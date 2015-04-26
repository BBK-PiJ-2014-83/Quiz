package server;

import java.util.Optional;

public class UserFunction {
    QuizServer server;

    public UserFunction(QuizServer server) {
        this.server = server;
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getUser(String username) throws IllegalArgumentException {
        Optional<User> user = server.getUsers().stream().
                filter(p -> (p.getUsername().toLowerCase().equals(username.toLowerCase()))).
                findFirst();
        if(!(user.isPresent())) {
            throw new IllegalArgumentException("This username doesn't exist! Please try again.");
        }
        return user.get().getId();
    }
    /**
     * Creates a new user either as a player or setup person
     * @param username of the user that you wish to create.
     * @param age The age of the user that you wish to create.
     * @param location The location of the user that you wish to create.
     * @return the id of the user
     */
    public int createUser(String username, int age, String location ) throws IllegalArgumentException {
        Optional<User> user = server.getUsers().stream().
                filter(p -> (p.getUsername().toLowerCase().equals(username.toLowerCase()))).
                findFirst();
        if(user.isPresent()) {
            throw new IllegalArgumentException("This username is taken. Please try again.");
        }
        try{
            User newUser = new User(server.getUsers().size(), username, age, location);
            server.getUsers().add(newUser);
            //Now save it.
            server.saveAll();
            return newUser.getId();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
