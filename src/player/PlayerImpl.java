package player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PlayerImpl extends UnicastRemoteObject implements Player {
    public PlayerImpl() throws RemoteException {
    }

    public int getPlayer(String username) throws RemoteException {
        //for now let's just return 5
        System.out.println(username);
        return 5;
    }

    public int createPlayer(String username, int age, String location ) throws RemoteException {
        //for now let's just return 4
        return 4;
    }

}