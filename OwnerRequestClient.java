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

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // Send Owner information
            out.println("OWNER");
            out.println(ownerID);
            out.println(plate);
            out.println(model);
            out.println(type);
            out.println(time);

            // Check if server received request
            String message = in.readLine();

            if ("RECEIVED".equals(message)) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            null, "Server received your request"
                    )
                );
            } else {
                throw new IOException("No acknowledgment");
            }

            // Get controller decision
            String result = in.readLine();

            if ("ACCEPTED".equals(result)) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            null, "Vehicle registration accepted"
                    )
                );
            } else if ("REJECTED".equals(result)) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            null, "Vehicle registration rejected"
                    )
                );
            } else {
                throw new IOException("No decision received");
            }

            socket.close();

        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        null, "Request failed: " + e.getMessage()
                )
            );
        }
    }
}
