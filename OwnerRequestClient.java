
import java.io.*;
import java.net.*;
import javax.swing.*;

public class OwnerRequestClient {

    public static void sendRequest(
            String ownerID,
            String plate,
            String model,
            String type,
            double time) {

        try {
            // Zohaib - Connect to VC Controller
            Socket socket = new Socket("localhost", 5000);

            System.out.println("Connected to VC Controller");

            socket.close();

        } catch (Exception e) {
            System.out.println("Could not connect to server");
        }
    }
}
