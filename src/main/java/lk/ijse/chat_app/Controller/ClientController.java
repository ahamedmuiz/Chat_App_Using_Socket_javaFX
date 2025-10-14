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
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML private TextArea chatDisplayArea;
    @FXML private TextField messageInput;
    @FXML private Button sendButton;

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(()->{
            try {
                socket = new Socket("127.0.0.1", 8080);
                 chatDisplayArea.appendText("Connected to Server!\n");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                while (true) {

                    final String message = dataInputStream.readUTF();
                    chatDisplayArea.appendText("Server: " + message + "\n");
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