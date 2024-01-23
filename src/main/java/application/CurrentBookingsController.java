package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class CurrentBookingsController {
    @FXML
    private TableColumn<ListInfo, Integer> cBooking;

    @FXML
    private TableColumn<ListInfo, LocalDate> cDate;

    @FXML
    private TableColumn<ListInfo, Integer> cDays;

    @FXML
    private TableColumn<ListInfo, String> cName;

    @FXML
    private TableColumn<ListInfo, Integer> cRooms;
    @FXML
    private Label lblTotal;

    @FXML
    private TableView<ListInfo> tvTable;


    @FXML
    public void initialize() {
        cBooking.setCellValueFactory(new PropertyValueFactory<>("id"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        cDays.setCellValueFactory(new PropertyValueFactory<>("numDays"));
        cName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        cRooms.setCellValueFactory(new PropertyValueFactory<>("numRooms"));

        ObservableList<ListInfo> bookingsList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
             Statement stmt = conn.createStatement()) {

            String query = "SELECT booking.id, booking.date, booking.num_days, client.fName, client.lName " +
                    "FROM booking " +
                    "JOIN client ON booking.client_id = client.id;";
            ResultSet rs = stmt.executeQuery(query);

            int totalBookings = 0;
            while (rs.next())
            {
                totalBookings++;
                int id = rs.getInt("id");
                LocalDate date = rs.getDate("date").toLocalDate();
                int numDays = rs.getInt("num_days");
                String clientName = rs.getString("fName") + " " + rs.getString("lName");

                int numRooms = 0;
                try (PreparedStatement stmtNumRooms = conn.prepareStatement("SELECT COUNT(*) FROM room_mapping WHERE booking_id = ?"))
                {
                    stmtNumRooms.setInt(1, id);
                    ResultSet rsNumRooms = stmtNumRooms.executeQuery();
                    if (rsNumRooms.next())
                    {
                        numRooms = rsNumRooms.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ListInfo lf = new ListInfo(id, date, numDays, clientName, numRooms);
                bookingsList.add(lf);
            }
            lblTotal.setText(String.valueOf(totalBookings));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvTable.setItems(bookingsList);
    }

}
