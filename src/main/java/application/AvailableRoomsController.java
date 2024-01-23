package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvailableRoomsController {

    @FXML
    private TableColumn<Room, Integer> cId;

    @FXML
    private TableColumn<Room, String> cType;

    @FXML
    private TableColumn<Room, Double> cRate;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Room> tvTable;


    @FXML
    void initialize() {
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

        ObservableList<Room> roomList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE booked = 0;"))
        {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double rate = rs.getDouble("rate");

                Room room = new Room(id, type, rate, false);
                roomList.add(room);
            }

            tvTable.setItems(roomList);
            lblTotal.setText(String.valueOf(roomList.size()));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
