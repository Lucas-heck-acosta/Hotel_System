/**********************************************
 * PROJECT
 * Course: APD545
 * Last Name:Acosta
 * First Name:Lucas
 * ID: 165041211
 * Section: NAA
 * This assignment represents my own work in accordance with Seneca Academic Policy.
 * Lucas heck acosta
 * Date:08/13/2023
 * **********************************************/

package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class HotelServer extends Application
{
    private static int PORT = 8080;
    private ServerSocket ss;

    private TextArea ta = new TextArea();
    private Hashtable outputStream = new Hashtable();


    @Override
    public void start(Stage ps) throws Exception {

        ta.setWrapText(true);

        Scene sc = new Scene(new ScrollPane(ta), 350, 200);
        ps.setTitle("Hotel Server");
        ps.setScene(sc);
        ps.show();

        loadDatabase();

        new Thread(() -> listen()).start();
    }

    private void loadDatabase()
    {
        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/lucas/IdeaProjects/HotelSystem/src/main/java/application/hotel.db");
            Statement stm = conn.createStatement();)
        {
            System.out.println(stm.toString());

            String query = "CREATE TABLE IF NOT EXISTS log_in (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "password VARCHAR(100)" +
                    ");";
            boolean result = stm.execute(query);
            if (!result)
            {
                System.out.println("Table log_in created successfully.");
            }
            else
            {
                System.out.println("Table log_in creation failed.");
            }

            String checkUserQuery = "SELECT COUNT(*) FROM log_in WHERE username = 'admin1';";
            ResultSet rs = stm.executeQuery(checkUserQuery);
            rs.next();
            int count = rs.getInt(1);

            if (count == 0)
            {
                String insertUserQuery = "INSERT INTO log_in (username, password) VALUES ('admin1', '1234');";
                stm.execute(insertUserQuery);
                System.out.println("admin1 created");

                insertUserQuery = "INSERT INTO log_in (username, password) VALUES ('admin2', '1234');";
                stm.execute(insertUserQuery);
                System.out.println("admin2 created");
            }
            else
            {
                System.out.println("admin1 found");
                System.out.println("admin2 found");
            }



            query = "CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INTEGER PRIMARY KEY," +
                    "type TEXT," +
                    "rate REAL," +
                    "booked BOOLEAN" +
                    ");";

            result = stm.execute(query);
            if (!result)
            {
                System.out.println("Table rooms created successfully.");

                String checkEmptyQuery = "SELECT COUNT(*) FROM rooms;";
                rs = stm.executeQuery(checkEmptyQuery);
                rs.next();
                count = rs.getInt(1);

                if (count == 0)
                {
                    String insertDataQuery = "INSERT INTO rooms (type, rate, booked) VALUES " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Single', 35, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Double', 55, 0), " +
                            "('Deluxe', 100, 0), " +
                            "('Deluxe', 100, 0), " +
                            "('Deluxe', 100, 0), " +
                            "('Deluxe', 100, 0), " +
                            "('Deluxe', 100, 0), " +
                            "('Penthouse', 240, 0);";
                    stm.execute(insertDataQuery);
                    System.out.println("Initial room data inserted successfully.");
                }

            }
            else
            {
                System.out.println("Table rooms creation failed.");
            }


            query = "CREATE TABLE IF NOT EXISTS client (" +
                    "    id INTEGER PRIMARY KEY," +
                    "    fName VARCHAR(50)," +
                    "    lName VARCHAR(50)," +
                    "    Address VARCHAR(100)," +
                    "    Phone VARCHAR(15)," +
                    "    Email VARCHAR(100)" +
                    ");";
            result = stm.execute(query);
            if (!result)
            {
                System.out.println("Table client created successfully.");
            }
            else
            {
                System.out.println("Table client creation failed.");
            }



            query = "CREATE TABLE IF NOT EXISTS booking (" +
                    "    id INTEGER PRIMARY KEY," +
                    "    client_id INTEGER," +
                    "    num_days INTEGER," +
                    "    date DATE," +
                    "    FOREIGN KEY (client_id) REFERENCES client(id)" +
                    ");";
            result = stm.execute(query);
            if (!result)
            {
                System.out.println("Table booking created successfully.");
            }
            else
            {
                System.out.println("Table booking creation failed.");
            }




            query = "CREATE TABLE IF NOT EXISTS room_mapping (" +
                    "    booking_id INT," +
                    "    room_id INT," +
                    "    PRIMARY KEY (booking_id, room_id)," +
                    "    FOREIGN KEY (booking_id) REFERENCES booking(id)," +
                    "    FOREIGN KEY (room_id) REFERENCES rooms(id)" +
                    ");";

            result = stm.execute(query);
            if (!result)
            {
                System.out.println("Table room_mapping created successfully.");
            }
            else
            {
                System.out.println("Table room_mapping creation failed.");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void listen() {
        try {
            ss = new ServerSocket(8080);

            Platform.runLater(() -> ta.appendText("Server started - " + new Date() + "\n"));


            while (true) {
                Socket s = ss.accept();
                Platform.runLater(() -> ta.appendText("Connected: " + s + " - " + new Date() + "\n"));

                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                outputStream.put(s, dout);

                new ServerThread(this, s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Enumeration getOutputStreams() {
        return outputStream.elements();
    }


    void sendToAll(String message) {
        for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
            DataOutputStream dout = (DataOutputStream) e.nextElement();
            try {
                dout.writeUTF(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    class ServerThread extends Thread
    {
        private HotelServer server;
        private Socket socket;

        public ServerThread(HotelServer server, Socket socket)
        {
            this.server = server;
            this.socket = socket;
            start();
        }

        public void run()
        {
            try {
                DataInputStream din = new DataInputStream(socket.getInputStream());
                while (true)
                {
                    String st = din.readUTF();
                    server.sendToAll(st);

                    ta.appendText(st + "\n");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
