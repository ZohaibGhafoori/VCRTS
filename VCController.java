import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VCController {

    // Controller information
    private String controllerID;
    private List<Vehicle> vehicles;
    private Queue<Job> pendingJobs;
    private List<Job> jobList;
    private List<Job> completedJobs;

    public VCController(String controllerID) {
        this.controllerID = controllerID;
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
}
