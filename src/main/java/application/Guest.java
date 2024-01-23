package application;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;

public class Guest {
    private int id;
    private String fName;
    private String lName;
    private String address;
    private String phone;
    private String email;

    public Guest(String fName, String lName, String address, String phone, String email)
    {
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public void saveGuest()
    {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO client (fName, lName, Address, Phone, Email) VALUES (?, ?, ?, ?, ?);"))
        {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setString(5, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0)
            {

                ResultSet key = stmt.getGeneratedKeys();
                setId(key.getInt(1));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getfName()
    {
        return fName;
    }

    public void setfName(String fName)
    {
        this.fName = fName;
    }

    public String getlName()
    {
        return lName;
    }

    public void setlName(String lName)
    {
        this.lName = lName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
