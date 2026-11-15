import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class VCServer implements Runnable {

    public static final int PORT = 5050;

    // Requests waiting for the VC Controller to accept or reject
    private ArrayList<Request> pendingRequests = new ArrayList<Request>();

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

    // Reads one request, keeps it waiting for a decision, and tells the sender it arrived
    private void receiveRequest(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String type = in.readLine();

            // An owner request has 5 lines: owner ID, plate, model, type and residency time
            String[] info = new String[5];
            for (int i = 0; i < info.length; i++) {
                info[i] = in.readLine();
            }

            Request request = new Request(type, info, socket, out);
            addRequest(request);

            request.sendMessage("RECEIVED");
            System.out.println("Received " + type + " request from " + info[0]);

        } catch (IOException ex) {
            System.out.println("Could not read the request: " + ex.getMessage());
        }
    }

    public synchronized void addRequest(Request request) {
        pendingRequests.add(request);
    }

    public synchronized ArrayList<Request> getPendingRequests() {
        return pendingRequests;
    }
}
