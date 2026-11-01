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
