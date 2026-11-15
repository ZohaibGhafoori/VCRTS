/* Project: VCRTS
 * Class: Request.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 15, 2026
 * A request from an Owner or Client that waits for the VC Controller to accept or reject it.
 */
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class Request {

    // "Owner" or "Client"
    private String userType;

    // The information to save
    private String record;

    // Connection to the sender
    private Socket socket;
    private DataOutputStream outputStream;

    public Request(String userType, String record, Socket socket, DataOutputStream outputStream) {
        this.userType = userType;
        this.record = record;
        this.socket = socket;
        this.outputStream = outputStream;
    }

    // Sends a message to the sender
    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Could not send " + message + ": " + ex.getMessage());
        }
    }

    // Closes the connection to the sender
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Could not close the connection: " + ex.getMessage());
        }
    }

    // Saves the request to the owner file or the client file
    public void saveToFile() throws IOException {
        String fileName = "client_transactions.txt";

        if (userType.equals("Owner")) {
            fileName = "owner_transactions.txt";
        }

        // Jobs get their Job ID only when accepted, so rejected jobs don't use up a number
        if (userType.equals("Client")) {
            record = record.replace("Job ID: Pending", "Job ID: " + Job.generateJobID());
        }

        FileWriter writer = new FileWriter(fileName, true);
        writer.write(record);
        writer.close();
    }

    // Text shown in the requests list
    public String toString() {
        String[] lines = record.split("\n");
        String text = "";

        // Skip the time and the dashes
        for (int i = 1; i < lines.length - 1; i++) {
            if (i > 1) {
                text = text + ",  ";
            }
            text = text + lines[i];
        }

        return text;
    }
}
