import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.DefaultListModel;

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

                // Give each sender its own thread
                RequestHandler handler = new RequestHandler(socket, waitingRequests);
                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (IOException ex) {
            setOnline(false);
            System.out.println("Server error: " + ex.getMessage());
        }
    }
}
