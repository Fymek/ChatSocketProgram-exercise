import java.io.*;
import java.net.Socket;
import java.net.SocketOption;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String clientUsername;

    public String getClientUsername() {
        return clientUsername;
    }

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.clientUsername = bufferedReader.readLine();
            System.out.println("New user \"" + clientUsername + "\" joined.");
            clientHandlers.add(this);
        }catch (IOException e){
            closeEverything();
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient.equals("")) continue;
                System.out.println("New task request from user: "+ this.clientUsername);
            }catch (IOException e){
                closeEverything();
                break;
            }
        }
    }
    public void removeClientHandler(){clientHandlers.remove(this);}

    public void closeEverything(){
        removeClientHandler();
        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
