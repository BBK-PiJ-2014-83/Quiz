package player;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote {
    //Used to get the player at the beginning of the session. returns a player id.
     int getPlayer(String username) throws RemoteException;
    //Used to create a player if one doesn't exist. Returns a player id.
    int createPlayer(String username, int age, String location) throws RemoteException;

}