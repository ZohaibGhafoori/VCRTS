import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Client extends User {

    private List<Job> jobList = new ArrayList<>();

    public Client(String clientID) {
        super(clientID);
    }

    public String getUserType() {
        return "Client";
    }

    public void addJob(Job job) {
        jobList.add(job);
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public String trackJob(String jobId) {
        for (Job job : jobList) {
            if (job.getJobID().equals(jobId)) { 
                return job.trackJob();
            }

        }
        return "Job not found.";

    }

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
 