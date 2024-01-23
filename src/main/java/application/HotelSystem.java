package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

public class HotelSystem extends Application {

    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HotelSystem.class.getResource("startFXML.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();


        try
        {
            socket = new Socket(SERVER_IP, SERVER_PORT);

            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            System.out.println("connected to server: " + SERVER_IP + " on port: " + SERVER_PORT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}