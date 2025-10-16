package lk.ijse.chat_app.Controller;

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
        new Thread(()->{
            try {
               serverSocket = new ServerSocket(8080);
               chatDisplayArea.appendText("Server Started\n");

               socket = serverSocket.accept();

                 chatDisplayArea.appendText("Client Connected!\n");

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while(true) {
                    String message = dataInputStream.readUTF();
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
        if (socket != null && !message.isEmpty()) {
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
