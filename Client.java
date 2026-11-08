/* Project: VCRTS
 * Class: Client.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * A client who submits jobs.
 */
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//represents a client who submits a job to the system, a client can have many jobs 
public class Client extends User {

    // A client can submit any number of jobs
    private ArrayList<Job> jobList;

    public Client(String clientID) {
        super(clientID);
        jobList = new ArrayList<Job>();
    }

    public String getUserType() {
        return "Client";
    }

    // Add a job and link it back to this client
    public void addJob(Job job) {
        jobList.add(job);
        job.setClient(this);
    }

    public void removeJob(Job job) {
        jobList.remove(job);
    }

    public ArrayList<Job> getJobList() {
        return jobList;
    }
//allows a client to track the status of their job by its unique job ID
    public String trackJob(String jobId) {
        for (Job job : jobList) {
            if (job.getJobID().equals(jobId)) { 
                return job.trackJob();
            }

        }
        return "Job not found.";

    }
//saves the job to a text file, shows the time stamp, date, and client ID
    public void saveToFile(String fileName) throws IOException {

    FileWriter writer = new FileWriter(fileName, true);
     
   for (Job j : jobList) {
        try {
            writer.write("Time: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("User Type: " + getUserType() + "\n");
            writer.write("Client ID: " + getUserID() + "\n");
            writer.write(j.toRecord());
        } catch (IOException ex) {
           
        }
            
        }
 
        writer.close();
    }
}
 