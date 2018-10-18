public class Job implements Comparable{

    private boolean type;   // true === large job. false === small job.
    private int arrivalTime;
    private int jobLength;  //The time it takes to finish
    private int jobId;



    // Constructor
    public Job(int jobId, int jobLength, int arrivalTime) {
        this.jobId = jobId;
        this.jobLength = jobLength;
        this.arrivalTime = arrivalTime;
    }

    // Getters & Setters
    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

    @Override
    public int compareTo(Object o) {
        Job that = (Job)o;
        return Integer.compare(this.arrivalTime, that.arrivalTime);
    }

    @Override
    public String toString() {
        return "Process id = " + this.jobId + "\nJob Arrival TIme: " + this.arrivalTime + "\nJob Run Time: " + this.jobLength + "\n\n";
    }
}
