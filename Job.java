public class Job {

    public static final int HIGHEST_PRIORITY = 1;
    public static final int LOWEST_PRIORITY = 10;
    public static final int DEFAULT_PRIORITY = 5;

    private static int counter =1;

    private String jobID;
    private String clientID;
    private int jobduration;
    private String jobdDeadline;
    private String jobStatus;
    private int priority;
    private int waitingTime;
    private int completionTime;

    public Job(String clientID, int jobduration, String jobdDeadline, String jobStatus, int priority) {
        this.jobID = "J" + counter++;
        this.clientID = clientID;
        this.jobduration = jobduration;
        this.jobdDeadline = jobdDeadline;
        this.jobStatus = "Pending";
        this.priority = priority;

    }
    public String getJobID() {
        return jobID;
    
    }
    public String getClientID() {
            return clientID;
    }

    public int getJobduration() {
        return jobduration;
    }
    public void setJobduration(int jobduration) {
        this.jobduration = jobduration;
    }

    public String getJobdDeadline() {
        return jobdDeadline;
    }
    public void setJobdDeadline(String jobdDeadline) {
        this.jobdDeadline = jobdDeadline;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
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
        return "Completed".equals(jobStatus);
    }

    public String trackJob() {
        return "Job " + jobID + " is " + jobStatus;
    }

    public String getdurationText() {
        return(jobduration/60) + " hours  " + (jobduration%60) + " minutes";
    }

    public String toRecord() { 
        return "Job Id: " + jobID + "\n"
                + "Job Duration: " + getdurationText() + "\n"
                + "Job Deadline: " + jobdDeadline + "\n"
                + "Job Status: " + jobStatus + "\n"
                + "Job Priority: " + priority + "\n"
                + "Job Waiting Time: " + waitingTime + "\n"
                + "Job Completion Time: " + completionTime + "\n";
                
    }
}


