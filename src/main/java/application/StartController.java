package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {

    @FXML
    private Button btnBook;

    @FXML
    private Button btnLogIn;

    @FXML
    void bookRoom(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("bookRoomFXML.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Book room");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void logIn(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("logInFXML.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Log In");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
