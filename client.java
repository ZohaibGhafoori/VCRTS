import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class client {
    
private String clientId;
private List<Job> jobList = new ArrayList<>();

public client(String clientId) {
    this.clientId = clientId;
}

public String getClientId() {
    return clientId;
}
public void setClientId(String clientId) { this.clientId = clientId; }

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
            writer.write("User Type: Client\n");
            writer.write("Client ID: " + clientId + "\n");
            writer.write(j.toRecord());
        } catch (IOException ex) {
           
        }
            
        }
 
        writer.close();
    }
}
 