import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class VCServer implements Runnable {

    public static final int PORT = 5050;

    // Requests shown on the VC Controller
    private DefaultListModel<Request> waitingRequests;

    public VCServer(DefaultListModel<Request> waitingRequests) {
        this.waitingRequests = waitingRequests;
    }

    // True while the server is listening for Owners and Clients
    private static boolean online = false;

    // synchronized because the server thread changes this and the Controller window reads it
    public static synchronized boolean isOnline() {
        return online;
    }

    private static synchronized void setOnline(boolean value) {
        online = value;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            setOnline(true);
            System.out.println("VC Controller server is listening on port " + PORT);

            while (true) {
                // Wait for an owner or client to connect
                Socket socket = serverSocket.accept();
                receiveRequest(socket);
            }

        } catch (IOException ex) {
            setOnline(false);
            System.out.println("Server error: " + ex.getMessage());
        }
    }

    // Reads a request and adds it to the list
    private void receiveRequest(Socket socket) {
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

            // Keep the connection open for the answer

        } catch (IOException ex) {
            System.out.println("Could not read the request: " + ex.getMessage());
        }
    }
}
