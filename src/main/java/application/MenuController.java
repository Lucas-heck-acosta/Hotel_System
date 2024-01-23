package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MenuController {

    @FXML
    private Button btnLogIn;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUser;

    @FXML
    private Button btnAvailable;

    @FXML
    private Button btnBill;

    @FXML
    private Button btnBook;

    @FXML
    private Button btnBookings;

    @FXML
    private Button btnExit;

    @FXML
    void logIn(ActionEvent event)
    {
        String username = txtUser.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty())
        {
            showWarning("Please enter both username and password");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db"))
        {
            String query = "SELECT * FROM log_in WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query))
            {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        try
                        {
                            FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("menuFXML.fxml"));
                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Main menu");
                            stage.setScene(new Scene(root));
                            stage.show();
                        }
                        catch (IOException err)
                        {
                            err.printStackTrace();
                        }
                    }
                    else
                    {
                        showWarning("User not found. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showWarning(String s)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }



    @FXML
    void available(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("availableRoomsFXML.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Current Bookings");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void bill(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("billServiceFXML.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Bill Service");
        stage.setScene(new Scene(root));
        stage.show();
    }

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
    void bookings(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("currentBookingsFXML.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Current Bookings");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void exit(ActionEvent event)
    {
        System.exit(0);
    }

}
