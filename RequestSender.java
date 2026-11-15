import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Sends a request to the VC Controller on its own thread
public class RequestSender implements Runnable {

    // "Owner" or "Client"
    private String userType;

    // The information to save
    private String record;

    public RequestSender(String userType, String record) {
        this.userType = userType;
        this.record = record;
    }

    public void run() {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", VCServer.PORT);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Send the user type and the data
            outputStream.writeUTF(userType);
            outputStream.writeUTF(record);

            // Wait for the server to say it has recieved it
            String message = inputStream.readUTF();

            if (message.equals("RECEIVED")) {
                showMessage("The VC Controller has received your request.");
            }

            // Wait for Accept or Reject
            message = inputStream.readUTF();

            if (message.equals("ACCEPTED")) {
                showMessage("Your request was accepted and saved.");
            }
            else if (message.equals("REJECTED")) {
                showMessage("Your request was rejected and not saved.");
            }

            socket.close();

        } catch (IOException ex) {
            showMessage("Could not reach the VC Controller.");
        }
    }

    // Shows a popup from the window's thread
    private void showMessage(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, text);
            }
        });
    }
}
