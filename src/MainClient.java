import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    static int port = 2001;
    public static String readUserName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        return username;
    }

    private static void startClientService(String username) throws IOException {
        Socket socket = new Socket("localhost", port);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }

    public static void main(String[] args){
        String username = readUserName();

            try {
                startClientService(username);
            } catch (Exception e) {
                System.out.println("Connection to server failed.");
            }

        System.out.println("the program has finished its work");
    }

}
