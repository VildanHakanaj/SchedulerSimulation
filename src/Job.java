public class Job implements Comparable{

    private boolean begunProcessing;    // If the Job has begun processing. Used for calculation of response time.
    private int arrivalTime;            // The time that the Job will arrive (become available) for processing.
    private int jobLength;              // The time left to process.
    private int jobId;                  // Unique Job identifier.

    // Constructor
    public Job(int jobId, int jobLength, int arrivalTime) {
        this.jobId = jobId;
        this.jobLength = jobLength;
        this.arrivalTime = arrivalTime;
        this.begunProcessing = false;
    }

    // Getters & Setters

    public int getJobId() {
        return jobId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getJobLength() {
        return jobLength;
    }

    public void setJobLength(int jobLength) {
        this.jobLength = jobLength;
    }

    public boolean hasBegunProcessing() {
        return this.begunProcessing;
    }

    public void setAsBegunProcessing() {
        this.begunProcessing = true;
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
