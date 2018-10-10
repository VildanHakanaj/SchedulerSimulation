public class Job {

    private boolean type;   // true === large job. false === small job.
    private int arrivalTime;
    private int jobLength;

    // Constructor
    public Job() {

    }

    // Getters & Setters
    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getJobLength() {
        return jobLength;
    }

    public void setJobLength(int jobLength) {
        this.jobLength = jobLength;
    }
}
