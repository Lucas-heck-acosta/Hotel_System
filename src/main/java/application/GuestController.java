package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class GuestController {

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtPhone;


    private ObservableList<Room> bookedRooms;
    private int numRooms;
    private int numDays;
    private LocalDate date;


    public void initialize(ObservableList<Room> rooms, int numRooms, int numDays, LocalDate date)
    {
        this.bookedRooms = rooms;
        this.numRooms = numRooms;
        this. numDays = numDays;
        this.date = date;
    }

    @FXML
    void saveGuest(ActionEvent event)
    {
        String fName = txtFName.getText();
        String lName = txtLName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || !validEmail(email))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields with valid data");
            alert.showAndWait();
        }
        else
        {

            Guest guest = new Guest(fName, lName, address, phone, email);

            guest.saveGuest();


            Stage stage = (Stage) txtAddress.getScene().getWindow();
            stage.close();

            Booking book = new Booking(0, guest, bookedRooms, numRooms, numDays, date);


            book.book();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking complete");
            alert.setHeaderText(null);
            alert.setContentText("The booking for client: " + fName + " is complete.");
            alert.showAndWait();

        }

    }

    private boolean validEmail(String email)
    {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

}
