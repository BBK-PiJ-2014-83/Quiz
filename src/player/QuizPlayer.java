package player;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class QuizPlayer {
    public static void main(String[] args) {
        QuizPlayer session = new QuizPlayer();
        session.begin();
    }

    private void begin(){
        try {
            // fire to localhost port 1099
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            Scanner sc = new Scanner(System.in);
            Player player = (Player) reg.lookup("player");
            while(sc.hasNext()) {
                String s1 = sc.next();
                if(s1.equals("exit")) {
                    break;
                }

                int playerId = player.getPlayer(s1);
                System.out.println(playerId);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}