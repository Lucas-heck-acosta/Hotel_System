package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class RoomController {

    @FXML
    private Button btnBook;

    @FXML
    private TableColumn<Room, Double> cRate;

    @FXML
    private TableColumn<Room, String> cType;

    @FXML
    private Label lblRooms;

    @FXML
    private TableView<Room> tvTable;
    private int numRooms;
    private int numDays;
    private LocalDate date;

    private ObservableList<Room> bookedRoomsList = FXCollections.observableArrayList();
    private ObservableList<Room> roomObservableList = FXCollections.observableArrayList();


    public void initialize(int rooms, int days, LocalDate date)
    {
        this.numRooms = rooms;
        this.numDays = days;
        this.date = date;

        lblRooms.setText(String.valueOf(numRooms));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
             Statement stm = conn.createStatement();)
        {
            String query = "SELECT id, type, rate, booked FROM rooms WHERE booked = 0;";
            ResultSet rs = stm.executeQuery(query);

            while (rs.next())
            {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double rate = rs.getDouble("rate");
                boolean booked = rs.getBoolean("booked");

                Room room = new Room(id, type, rate, booked);
                roomObservableList.add(room);
            }

            cType.setCellValueFactory(new PropertyValueFactory<>("type"));
            cRate.setCellValueFactory(new PropertyValueFactory<>("rate"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        tvTable.setItems(roomObservableList);

    }

    @FXML
    void book(ActionEvent event) throws IOException {
        Room selectedRoom = tvTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null)
        {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
                 Statement stm = conn.createStatement();)
            {

                String updateQuery = "UPDATE rooms SET booked = 1 WHERE id = " + selectedRoom.getId() + ";";
                stm.executeUpdate(updateQuery);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            bookedRoomsList.add(selectedRoom);
            roomObservableList.remove(selectedRoom);

            numRooms--;
            lblRooms.setText(String.valueOf(numRooms));

            if (numRooms == 0)
            {
                Stage stage = (Stage) lblRooms.getScene().getWindow();
                stage.close();



                FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("guestInfoFXML.fxml"));
                Parent root = fxmlLoader.load();

                GuestController guestController = fxmlLoader.getController();
                guestController.initialize(bookedRoomsList, numRooms, numDays, date);

                stage = new Stage();
                stage.setTitle("Guest Information");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }



}
