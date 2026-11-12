/* Project: VCRTS
 * Class: VCController.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * Keeps track of vehicles and jobs and schedules the jobs.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VCController {

    // Controller information
    private String controllerID;
    private List<VehicleOwner> owners;
    private List<Client> clients;
    private List<Vehicle> vehicles;
    private Queue<Job> pendingJobs;
    private List<Job> jobList;
    private List<Job> completedJobs;

    public VCController(String controllerID) {
        this.controllerID = controllerID;
        this.owners = new ArrayList<VehicleOwner>();
        this.clients = new ArrayList<Client>();
        this.vehicles = new ArrayList<Vehicle>();
        this.pendingJobs = new LinkedList<Job>();
        this.jobList = new ArrayList<Job>();
        this.completedJobs = new ArrayList<Job>();
    }

    public String getControllerID() {
        return controllerID;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Queue<Job> getPendingJobs() {
        return pendingJobs;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public List<Job> getCompletedJobs() {
        return completedJobs;
    }

    // **PRIORITY SCHEDULING** Sorts jobs by priority and finds their waiting and completion times
    public List<Job> calculateCompletionTimes() {
        List<Job> schedule = new ArrayList<Job>();

        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            int position = 0;

            // Same priority jobs stay in order of submission
            while (position < schedule.size() && schedule.get(position).getPriority() <= job.getPriority()) {
                position++;
            }

            schedule.add(position, job);
        }

        int currentTime = 0;

        for (int i = 0; i < schedule.size(); i++) {
            Job job = schedule.get(i);

            job.setWaitingTime(currentTime);
            currentTime = currentTime + job.getJobDuration();
            job.setCompletionTime(currentTime);
        }

        return schedule;
    }

    public double getAverageWaitingTime(List<Job> schedule) {
        if (schedule.size() == 0) {
            return 0;
        }

        double total = 0;

        for (int i = 0; i < schedule.size(); i++) {
            total = total + schedule.get(i).getWaitingTime();
        }

        return total / schedule.size();
    }

    // Register a vehicle
    public void registerVehicle(String ownerID, Vehicle vehicle) {
        VehicleOwner owner = findOwner(ownerID);

        if (owner == null) {
            owner = new VehicleOwner(ownerID);
            owners.add(owner);
        }

        owner.rentVehicle(vehicle);
        vehicles.add(vehicle);
    }

    public void addJob(String clientID, Job job) {
        Client client = findClient(clientID);

        if (client == null) {
            client = new Client(clientID);
            clients.add(client);
        }

        client.addJob(job);
        jobList.add(job);
        pendingJobs.add(job);
    }

    // Read the saved owner file and register each vehicle again
    public void loadVehicles(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            return;
        }

        try {
            Scanner input = new Scanner(file);

            String ownerID = "";
            String licensePlate = "";
            String vehicleModel = "";
            String vehicleType = "Other";
            int residencyMinutes = 0;

            while (input.hasNextLine()) {
                String line = input.nextLine();

                if (line.startsWith("Owner ID: ")) {
                    ownerID = getValue(line);
                }
                else if (line.startsWith("License Plate: ")) {
                    licensePlate = getValue(line);
                }
                else if (line.startsWith("Vehicle Model: ")) {
                    vehicleModel = getValue(line);
                }
                else if (line.startsWith("Vehicle Type: ")) {
                    vehicleType = getValue(line);
                }
                else if (line.startsWith("Residency Time: ")) {
                    residencyMinutes = toMinutes(getValue(line));
                }
                else if (line.startsWith("----")) {

                    // End of one vehicle, so register it
                    if (!ownerID.equals("") && residencyMinutes > 0) {
                        Vehicle vehicle = new Vehicle(
                                licensePlate,
                                vehicleModel,
                                vehicleType,
                                residencyMinutes / 60.0,
                                "Unknown",
                                "Unknown"
                        );
                        registerVehicle(ownerID, vehicle);
                    }

                    ownerID = "";
                    licensePlate = "";
                    vehicleModel = "";
                    vehicleType = "Other";
                    residencyMinutes = 0;
                }
            }

            input.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not load vehicles: " + ex.getMessage());
        }
    }

    // Read the saved client file and add each job again
    public void loadJobs(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            return;
        }

        try {
            Scanner input = new Scanner(file);

            String time = "";
            String clientID = "";
            String jobID = "N/A";
            String jobDeadline = "";
            int jobDuration = 0;
            int priority = Job.DEFAULT_PRIORITY;

            while (input.hasNextLine()) {
                String line = input.nextLine();

                if (line.startsWith("Time: ")) {
                    time = getValue(line);
                }
                else if (line.startsWith("Client ID: ")) {
                    clientID = getValue(line);
                }
                else if (line.startsWith("Job ID: ")) {
                    jobID = getValue(line);
                }
                else if (line.startsWith("Job Duration: ")) {
                    jobDuration = toMinutes(getValue(line));
                }
                else if (line.startsWith("Job Deadline: ")) {
                    jobDeadline = getValue(line);
                }
                else if (line.startsWith("Job Priority: ")) {
                    priority = Integer.parseInt(getValue(line));
                }
                else if (line.startsWith("----")) {

                    // End of one job, so add it
                    if (!clientID.equals("") && jobDuration > 0) {
                        Job job = new Job(jobID, jobDuration, jobDeadline, priority);
                        job.setSubmitTime(time);
                        addJob(clientID, job);
                    }

                    time = "";
                    clientID = "";
                    jobID = "N/A";
                    jobDeadline = "";
                    jobDuration = 0;
                    priority = Job.DEFAULT_PRIORITY;
                }
            }

            input.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not load jobs: " + ex.getMessage());
        }
    }

    // Removes a job everywhere and saves the client file without it
    public void deleteJob(Job job) {
        jobList.remove(job);
        pendingJobs.remove(job);
        job.getClient().removeJob(job);

        saveJobs("client_transactions.txt");
    }

    // Writes all the jobs back to the file
    public void saveJobs(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName, false);

            for (int i = 0; i < jobList.size(); i++) {
                Job job = jobList.get(i);

                writer.write("Time: " + job.getSubmitTime() + "\n");
                writer.write("User Type: Client\n");
                writer.write("Client ID: " + job.getClient().getUserID() + "\n");
                writer.write("Job ID: " + job.getJobID() + "\n");
                writer.write("Job Duration: " + job.getDurationText() + "\n");
                writer.write("Job Deadline: " + job.getJobDeadline() + "\n");
                writer.write("Job Priority: " + job.getPriority() + "\n");
                writer.write("----------------------\n");
            }

            writer.close();
        } catch (IOException ex) {
            System.out.println("Could not save jobs: " + ex.getMessage());
        }
    }

    public void reloadData() {
        owners.clear();
        clients.clear();
        vehicles.clear();
        jobList.clear();
        pendingJobs.clear();

        loadVehicles("owner_transactions.txt");
        loadJobs("client_transactions.txt");
    }

    // Gets text after the label
    private String getValue(String line) {
        int colon = line.indexOf(":");
        return line.substring(colon + 2);
    }

    private int toMinutes(String text) {
        String[] parts = text.split(" ");

        if (parts.length < 4) {
            return 0;
        }

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[2]);
        return hours * 60 + minutes;
    }

    private VehicleOwner findOwner(String ownerID) {
        for (int i = 0; i < owners.size(); i++) {
            if (owners.get(i).getUserID().equals(ownerID)) {
                return owners.get(i);
            }
        }
        return null;
    }

    private Client findClient(String clientID) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUserID().equals(clientID)) {
                return clients.get(i);
            }
        }
        return null;
    }
}
