import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VCRTSServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("VCRTS Server is running on port " + PORT + "...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                // Handle the client in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Inner class to handle each client connection
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private VCController controller;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            // Initialize controller (this loads the existing files)
            this.controller = new VCController("VC");
            this.controller.loadVehicles("owner_transactions.txt");
            this.controller.loadJobs("client_transactions.txt");
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String requestType = in.readLine();
                if (requestType == null) return;

                if (requestType.equals("OWNER")) {
                    handleOwnerRequest(in, out);
                } else if (requestType.equals("CLIENT")) {
                    handleClientRequest(in, out);
                }

            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException e) {}
            }
        }

        private void handleOwnerRequest(BufferedReader in, PrintWriter out) throws IOException {
            String ownerID = in.readLine();
            String plate = in.readLine();
            String model = in.readLine();
            String type = in.readLine();
            String timeStr = in.readLine();

            // --- VALIDATION LOGIC (The Controller's decision) ---
            boolean accepted = true;
            String reason = "";

            if (ownerID == null || ownerID.trim().isEmpty() || !ownerID.matches("\\d+")) {
                accepted = false; reason = "Invalid Owner ID (must be numeric)";
            } else if (plate == null || plate.trim().length() < 2 || plate.trim().length() > 8) {
                accepted = false; reason = "Invalid License Plate length";
            } else if (timeStr == null || Double.parseDouble(timeStr) <= 0) {
                accepted = false; reason = "Residency time must be > 0";
            }

            // Check if vehicle already exists in the loaded controller
            if (accepted) {
                for (Vehicle v : controller.getVehicles()) {
                    if (v.getVehicleID().equalsIgnoreCase(plate)) {
                        accepted = false;
                        reason = "Vehicle already registered";
                        break;
                    }
                }
            }

            // --- SAVE LOGIC ---
            if (accepted) {
                // 1. Save to Server's memory
                Vehicle vehicle = new Vehicle(plate, model, type, Double.parseDouble(timeStr), "Unknown", "Unknown");
                controller.registerVehicle(ownerID, vehicle);

                // 2. Save to file (only accepted requests)
                saveOwnerToFile(ownerID, plate, model, type, Double.parseDouble(timeStr));
                
                out.println("ACCEPTED|Owner " + ownerID + " registered successfully.");
                System.out.println("ACCEPTED: Owner " + ownerID);
            } else {
                // REJECTED - Do not save to file
                out.println("REJECTED|" + reason);
                System.out.println("REJECTED: " + reason);
            }
        }

        private void handleClientRequest(BufferedReader in, PrintWriter out) throws IOException {
            String clientID = in.readLine();
            String jobDurationStr = in.readLine();
            String deadline = in.readLine();
            String priorityStr = in.readLine();

            // --- VALIDATION LOGIC ---
            boolean accepted = true;
            String reason = "";

            try {
                if (clientID == null || clientID.trim().isEmpty() || !clientID.matches("\\d+")) {
                    accepted = false; reason = "Invalid Client ID (must be numeric)";
                } else if (jobDurationStr == null || Integer.parseInt(jobDurationStr) <= 0) {
                    accepted = false; reason = "Job duration must be > 0";
                } else if (deadline == null || deadline.trim().isEmpty()) {
                    accepted = false; reason = "Invalid deadline";
                }
            } catch (NumberFormatException e) {
                accepted = false; reason = "Invalid number format";
            }

            // --- SAVE LOGIC ---
            if (accepted) {
                saveClientToFile(clientID, jobDurationStr, deadline, priorityStr);
                out.println("ACCEPTED|Job registered successfully.");
                System.out.println("ACCEPTED: Client " + clientID);
            } else {
                // REJECTED - Do not save to file
                out.println("REJECTED|" + reason);
                System.out.println("REJECTED: " + reason);
            }
        }

        private void saveOwnerToFile(String ownerID, String plate, String model, String type, double time) {
            try (FileWriter writer = new FileWriter("owner_transactions.txt", true)) {
                String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                writer.write("Time: " + timeStr + "\n");
                writer.write("User Type: Owner\n");
                writer.write("Owner ID: " + ownerID + "\n");
                writer.write("License Plate: " + plate + "\n");
                writer.write("Vehicle Model: " + model + "\n");
                writer.write("Vehicle Type: " + type + "\n");
                writer.write("Residency Time: " + (int)(time) + " hours 0 minutes\n"); 
                writer.write("----------------------\n");
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }

        private void saveClientToFile(String clientID, String jobDurationStr, String deadline, String priorityStr) {
            try (FileWriter writer = new FileWriter("client_transactions.txt", true)) {
                String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                writer.write("Time: " + timeStr + "\n");
                writer.write("User Type: Client\n");
                writer.write("Client ID: " + clientID + "\n");
                writer.write("Job ID: " + Job.generateJobID() + "\n"); 
                writer.write("Job Duration: " + jobDurationStr + " minutes\n");
                writer.write("Job Deadline: " + deadline + "\n");
                writer.write("Job Priority: " + priorityStr + "\n");
                writer.write("----------------------\n");
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }
    }
}