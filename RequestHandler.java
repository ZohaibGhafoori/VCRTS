// Runs on its own thread for each user that connects to the server

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;


public class RequestHandler implements Runnable {

    // Connection to the sender
    private Socket socket;

    // Requests shown on the VC Controller
    private DefaultListModel<Request> waitingRequests;

    public RequestHandler(Socket socket, DefaultListModel<Request> waitingRequests) {
        this.socket = socket;
        this.waitingRequests = waitingRequests;
    }

    // Reads a request and adds it to the list
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Read the user type and the information
            String userType = inputStream.readUTF();
            String record = inputStream.readUTF();
            System.out.println("Request received from " + userType);

            Request request = new Request(userType, record, socket, outputStream);

            // Tell the sender we got it
            request.sendMessage("RECEIVED");

            // Add the request to the list on screen
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    waitingRequests.addElement(request);
                }
            });


        } catch (IOException ex) {
            System.out.println("Could not read the request: " + ex.getMessage());
        }
    }
}
