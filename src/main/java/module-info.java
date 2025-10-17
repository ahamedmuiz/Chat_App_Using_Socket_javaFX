module lk.ijse.chat_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens lk.ijse.chat_app to javafx.fxml;
    exports lk.ijse.chat_app;
    exports lk.ijse.chat_app.Controller;
    opens lk.ijse.chat_app.Controller to javafx.fxml;

}