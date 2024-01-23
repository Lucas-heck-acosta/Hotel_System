package application;

import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Booking
{

    private int id;
    private Guest guest;
    private ObservableList<Room> rooms;
    private int numRooms;
    private int numDays;
    private LocalDate date;


    Booking(int id, Guest guest, ObservableList<Room> rooms, int numRooms, int numDays, LocalDate date)
    {
        this.id = id;
        this.guest = guest;
        this.rooms = rooms;
        this.numRooms = numRooms;
        this.numDays = numDays;
        this.date = date;
    }


    void book()
    {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db"))
        {
            conn.setAutoCommit(false);

            int bookingID;

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO booking (client_id, num_days, date) VALUES (?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS))
            {
                stmt.setInt(1, guest.getId());
                stmt.setInt(2, numDays);
                stmt.setDate(3, java.sql.Date.valueOf(date));
                stmt.executeUpdate();

                try (var generatedKeys = stmt.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        bookingID = generatedKeys.getInt(1);
                    }
                    else
                    {
                        throw new SQLException("Creating booking failed");
                    }
                }
            }

            for (Room room : rooms)
            {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO room_mapping (booking_id, room_id) VALUES (?, ?);"))
                {
                    stmt.setInt(1, bookingID);
                    stmt.setInt(2, room.getId());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
