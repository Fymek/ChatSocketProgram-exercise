import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(2001);
        Server server = new Server(serverSocket);
        System.out.println("Server is listening at port: 2001" );
        server.startServer();
    }
}
