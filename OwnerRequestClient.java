
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
            Socket socket = new Socket("localhost", 5000);

            // Send information to server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("OWNER");
            out.println(ownerID);
            out.println(plate);
            out.println(model);
            out.println(type);
            out.println(time);

            System.out.println("Owner information sent");

            socket.close();

        } catch (Exception e) {
            System.out.println("Could not send Owner information");
        }
    }
}
