import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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
                //sendMessage(messageFromClient);
                sendMessageWithDelay(messageFromClient);
            }catch (IOException e){
                closeEverything();
                break;
            } catch (Exception e) {
                sendMessage("Failure adding notification. Check the syntax");
            }
        }
    }
    public void sendMessageWithDelay(String rawMessage) throws Exception{

        Pattern pattern = Pattern.compile("^\\s?([0-9]+)\\s(.*)");
        Matcher decodedValues = pattern.matcher(rawMessage);
        decodedValues.find();

        int messagedelay = Integer.parseInt(decodedValues.group(1));
        String messageContent = decodedValues.group(2);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                sendMessage(messageContent);
                System.out.println("task for " + getClientUsername() + " has been sent");
                timer.cancel();
            }
        }, messagedelay);
    }
    public void sendMessage(String msgContent) {
        try {
            bufferedWriter.write(msgContent);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }
    public void removeClientHandler(){
        System.out.println(this.clientUsername +  " disconnected.");
        clientHandlers.remove(this);
    }

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
