import java.io.*;
import java.net.*;

public class TestServer {
    public static void main(String[] args) {
        System.out.println("--- Starting Server Communication Test ---");
        
        // Test 1: Valid Owner Request
        System.out.println("\nTest 1: Sending VALID request...");
        sendOwnerRequest("12345", "ABC-123", "Toyota Camry", "Gas", 2.0);
        
        // Test 2: Invalid Owner Request (Bad ID)
        System.out.println("\nTest 2: Sending INVALID request (Bad ID)...");
        sendOwnerRequest("BAD_ID", "XYZ-789", "Honda Civic", "Hybrid", 1.5);
        
        // Test 3: Invalid Owner Request (Duplicate Plate)
        System.out.println("\nTest 3: Sending INVALID request (Duplicate Plate)...");
        sendOwnerRequest("67890", "ABC-123", "Ford Mustang", "Gas", 3.0);
    }

    private static void sendOwnerRequest(String id, String plate, String model, String type, double time) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("OWNER");
            out.println(id);
            out.println(plate);
            out.println(model);
            out.println(type);
            out.println(time);

            // Read the server's response
            String response = in.readLine();
            System.out.println("Server Response: " + response);

        } catch (IOException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}