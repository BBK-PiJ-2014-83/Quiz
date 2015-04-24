package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class QuizServer {

    private void begin(){

        try {
            // port 1099 is default. Create on there.
            Registry registry = LocateRegistry.createRegistry(1099);
            //Create a new player session!
            registry.rebind("playerSession", new PlayerSessionImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer();
        server.begin();
    }
}