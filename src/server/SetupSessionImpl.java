package server;

import setup.SetupSession;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class SetupSessionImpl extends UnicastRemoteObject implements SetupSession {
    QuizServer server;
    public SetupSessionImpl(QuizServer server) throws RemoteException {
        this.server = server;
    }
    /**
     * gets the user id
     * @param username of the user that you wish to load.
     * @return the id of the user
     */
    public int getUser(String username) throws RemoteException,IllegalArgumentException {
        return 5;
    }
}
