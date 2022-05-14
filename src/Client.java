import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;


    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            closeEverything();
        }
    }

    public void sendMessage() {
        try {

            System.out.println("Please put a message and delay after which the message should arrive: ");
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything();
                    }
                }
            }
        }).start();
    }
    public void closeEverything(){
        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Connection has been lost");
        exit(1);
    }
    boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;

        }catch (NumberFormatException e)
        {
            return false;
        }
    }
}
