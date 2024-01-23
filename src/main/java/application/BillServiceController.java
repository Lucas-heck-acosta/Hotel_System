package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class BillServiceController {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDiscount;

    @FXML
    private Button btnSeach;

    @FXML
    private TableColumn<Room, Double> cRate;

    @FXML
    private TableColumn<Room, Integer> cRoom;

    @FXML
    private TableColumn<Room, String> cType;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblName;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Room> tvTable;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtID;

    private static final String DATABASE = "jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db";

    double original = 0;
    @FXML
    void addDiscount(ActionEvent event)
    {
        double discount = Double.parseDouble(txtDiscount.getText());

        if ( discount >= 0 && discount <= 25)
        {
            lblTotal.setText(String.valueOf(original * (1 - discount/100)));
        }
        else
        {
            showAlert("Error", "Invalid discount", "Discount has to be between 0 and 25%");
        }

    }

    @FXML
    void btnClear(ActionEvent event)
    {
        tvTable.getItems().clear();
        lblName.setText("-");
        lblDate.setText("-");
        lblTotal.setText("-");
        txtID.clear();
        txtDiscount.clear();
    }

    @FXML
    void close(ActionEvent event)
    {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void search(ActionEvent event)
    {
        String bookingIDStr = txtID.getText();

        if (bookingIDStr.isEmpty())
        {
            showAlert("Error", "Invalid Input", "Please enter a valid Booking ID.");
            return;
        }
        else
        {
            try (Connection conn = DriverManager.getConnection(DATABASE);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM booking WHERE id = ?;"))
            {

                int bookingID = Integer.parseInt(bookingIDStr);
                stmt.setInt(1, bookingID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next())
                {
                    int clientID = rs.getInt("client_id");
                    LocalDate bookingDate = rs.getDate("date").toLocalDate();


                    String name = "-";

                    try (PreparedStatement stmtClient = conn.prepareStatement("SELECT fName, lName FROM client WHERE id = ?;"))
                    {

                        stmtClient.setInt(1, clientID);
                        ResultSet rsClient = stmtClient.executeQuery();

                        if (rsClient.next())
                        {
                            String fName = rsClient.getString("fName");
                            String lName = rsClient.getString("lName");
                            name = fName + " " + lName;
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    lblName.setText(name);
                    lblDate.setText(bookingDate.toString());


                    try (PreparedStatement stmtRooms = conn.prepareStatement(
                            "SELECT rooms.id, rooms.type, rooms.rate " +
                                    "FROM room_mapping " +
                                    "JOIN rooms ON room_mapping.room_id = rooms.id " +
                                    "WHERE room_mapping.booking_id = ?"))
                    {

                        stmtRooms.setInt(1, bookingID);
                        ResultSet roomsRs = stmtRooms.executeQuery();

                        ObservableList<Room> roomsList = FXCollections.observableArrayList();
                        double total = 0;
                        while (roomsRs.next())
                        {
                            int roomId = roomsRs.getInt("id");
                            String roomType = roomsRs.getString("type");
                            double roomRate = roomsRs.getDouble("rate");
                            total += roomRate;
                            original = total;

                            roomsList.add(new Room(roomId, roomType, roomRate, false));
                        }

                        cRoom.setCellValueFactory(new PropertyValueFactory<>("id"));
                        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
                        cRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

                        tvTable.setItems(roomsList);
                        lblTotal.setText(String.valueOf(total));

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    showAlert("Error", "Booking Not Found", "No booking found.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database Error", "An error occurred searching for the booking.");
            }
        }
    }

    private void showAlert(String title, String hearder, String body)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(hearder);
        alert.setContentText(body);
        alert.showAndWait();
    }

}
