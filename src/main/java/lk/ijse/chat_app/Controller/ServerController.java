package lk.ijse.chat_app.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML private TextArea chatDisplayArea;
    @FXML private TextField messageInput;
    @FXML private Button sendButton;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ServerSocket serverSocket;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8080);

                socket = serverSocket.accept();
                 chatDisplayArea.appendText("Client Connected!\n");

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                readMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void readMessages() {
        new Thread(() -> {
            try {
                while (true) {
                    final String message = dataInputStream.readUTF();
                     chatDisplayArea.appendText("Client: " + message + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String message = messageInput.getText();
        if (socket != null && !message.trim().isEmpty()) {
            try {
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                chatDisplayArea.appendText("You: " + message + "\n");
                messageInput.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/*

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server Started");

            Socket localSocket = serverSocket.accept();
            System.out.println("Client Connected");

            DataInputStream dataInputStream = new DataInputStream(localSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(localSocket.getOutputStream());

            String message = dataInputStream.readUTF();
            System.out.println("Message from client: " + message);


            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.print("Enter message to Client : ");
                String messageToClient = scanner.nextLine();
                dataOutputStream.writeUTF(messageToClient);

                dataOutputStream.flush();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
===========================================================

public class Client {
    public static void main(String[] args) {

        try {
            Socket remoteSocket = new Socket("127.0.0.1", 8080);
            DataOutputStream dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(remoteSocket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.print("Enter message to Server : ");
                String message = scanner.nextLine();


                dataOutputStream.writeUTF(message);

                dataOutputStream.flush();
                String message2 = dataInputStream.readUTF();
                System.out.println("Message from client: " + message2);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

 */