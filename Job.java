/* Project: VCRTS
 * Class: Job.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * A job submitted by a client.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//represents a job a client submits, this stores the jobs details such as its priority
public class Job {

//lower number means higher priority 
    public static final int HIGHEST_PRIORITY = 1;
    public static final int LOWEST_PRIORITY = 10;
    public static final int DEFAULT_PRIORITY = 5;

    //Job attributes
    private String jobID;
    private String submitTime;
    private Client client;
    private int jobDuration;
    private String jobDeadline;
    private String jobStatus;
    private int priority;
    private int redundancyLevel;
    private int waitingTime;
    private int completionTime;


    //creates a new job 
    public Job(String jobID, int jobDuration, String jobDeadline, int priority) {
        this.jobID = jobID;
        this.jobDuration = jobDuration;
        this.jobDeadline = jobDeadline;
        this.jobStatus = "Pending";
        this.priority = priority;
        this.redundancyLevel = 1;
    }

    //Joanna - Generates unique JobID for each job submitted
    static String generateJobID() {
        String counterFile = "job_id_counter.txt";
        int next = 1;

        File file = new File(counterFile);

        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    next = scanner.nextInt() + 1;
                }
                scanner.close();
            } catch (IOException ex) {
                System.out.println("Could not read the job ID counter: " + ex.getMessage());
            }
        }
        try {
            FileWriter counterWriter = new FileWriter(counterFile, false);
            counterWriter.write(String.valueOf(next));
            counterWriter.close();
        } catch (IOException ex) {
            System.out.println("Could not save the job ID counter: " + ex.getMessage());
        }
        return "Job" + next;
    }

    public String getJobID() {
        return jobID;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(int jobDuration) {
        this.jobDuration = jobDuration;
    }

    public String getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(String jobDeadline) {
        this.jobDeadline = jobDeadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getRedundancyLevel() {
        return redundancyLevel;
    }

    // A job always needs at least one vehicle
    public void setRedundancyLevel(int redundancyLevel) {
        if (redundancyLevel < 1) {
            redundancyLevel = 1;
        }

        this.redundancyLevel = redundancyLevel;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void markCompleted() {
        this.jobStatus = "Completed";
    }

    public boolean isCompleted() {
        return jobStatus.equals("Completed");
    }

    public String trackJob() {
        return "Job " + jobID + " is " + jobStatus;
    }

    public String getDurationText() {
        return (jobDuration / 60) + " hours " + (jobDuration % 60) + " minutes";
    }

    public String toRecord() {
        return "Job ID: " + jobID + "\n"
                + "Job Duration: " + getDurationText() + "\n"
                + "Job Deadline: " + jobDeadline + "\n"
                + "Job Status: " + jobStatus + "\n"
                + "Job Priority: " + priority + "\n"
                + "Job Waiting Time: " + waitingTime + "\n"
                + "Job Completion Time: " + completionTime + "\n";
    }
}
