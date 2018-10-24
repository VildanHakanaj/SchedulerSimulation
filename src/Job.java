public class Job implements Comparable{

    private boolean begunProcessing;    // Used for calculation of arrival times.
    private int arrivalTime;
    private int jobLength;              // The time it takes to finish.
    private int jobId;



    // Constructor
    public Job(int jobId, int jobLength, int arrivalTime) {
        this.jobId = jobId;
        this.jobLength = jobLength;
        this.arrivalTime = arrivalTime;
        this.begunProcessing = false;
    }

    // Getters & Setters

    public boolean hasBegunProcessing() {
        return this.begunProcessing;
    }

    public void setAsBegunProcessing() {
        this.begunProcessing = true;
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
        return Integer.compare(this.getJobLength(), that.getJobLength());
    }

    @Override
    public String toString() {
        return "Process id = " + this.jobId + "\nJob Arrival TIme: " + this.arrivalTime + "\nJob Run Time: " + this.jobLength + "\n\n";
    }

    public Job memberwiseClone() {
        return new Job(this.jobId, this.jobLength, this.arrivalTime);
    }
}
