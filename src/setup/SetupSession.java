package setup;
import server.Question;
import server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SetupSession extends Remote {
    //Used to get the user at the beginning of the session. returns a user id.
    public int getUser(String username) throws RemoteException,IllegalArgumentException;
}