package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class BookRoomController {

    @FXML
    private Button btnSelectRoom;

    @FXML
    private DatePicker dateSelected;

    @FXML
    private Slider sldGuests;

    @FXML
    private TextField txtDays;

    @FXML
    private TextField txtRooms;


    @FXML
    public void initialize()
    {
        sldGuests.valueProperty().addListener((observable, oldValue, newValue) -> {
            int guests = newValue.intValue();
            int rooms = (guests + 1) / 2;

            txtRooms.setText(String.valueOf(rooms));
        });

        txtRooms.setOnAction(event -> {
            int guests = (int) sldGuests.getValue();
            int calculatedRooms = (guests + 1) / 2;
            int enteredRooms = Integer.parseInt(txtRooms.getText());

            if (enteredRooms < calculatedRooms)
            {
                txtRooms.setText(String.valueOf(calculatedRooms));
            }
        });
    }
    @FXML
    void selectRoom(ActionEvent event) throws IOException
    {
        int numRooms = Integer.parseInt(txtRooms.getText());
        int numDays = Integer.parseInt(txtDays.getText());
        LocalDate date = dateSelected.getValue();

        if(numRooms <= 0 || numDays <= 0 || date == null || date.isBefore(LocalDate.now()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields with valid data");
            alert.showAndWait();
        }
        else
        {

            Stage stage = (Stage) txtRooms.getScene().getWindow();
            stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("roomsFXML.fxml"));
            Parent root = fxmlLoader.load();

            RoomController RM = fxmlLoader.getController();
            RM.initialize(numRooms, numDays, date);

            stage = new Stage();
            stage.setTitle("Select rooms");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }


}
