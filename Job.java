public class Job {

    public static final int HIGHEST_PRIORITY = 1;
    public static final int LOWEST_PRIORITY = 10;
    public static final int DEFAULT_PRIORITY = 5;

    private static int counter =1;

    private String jobID;
    private Client client;
    private int jobDuration;
    private String jobDeadline;
    private String jobStatus;
    private int priority;
    private int redundancyLevel;
    private int waitingTime;
    private int completionTime;

    public Job(int jobDuration, String jobDeadline, int priority) {
        this.jobID = "J" + counter;
        counter = counter + 1;

        this.jobDuration = jobDuration;
        this.jobDeadline = jobDeadline;
        this.jobStatus = "Pending";
        this.priority = priority;
        this.redundancyLevel = 1;
    }

    public String getJobID() {
        return jobID;
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
