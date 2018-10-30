/*
    Class name: Job
    Purpose:    To represent a process within an operating system.
*/
public class Job implements Comparable{

    private boolean begunProcessing;    // If the Job has begun processing. Used for calculation of response time.
    private int arrivalTime;            // The time that the Job will arrive (become available) for processing.
    private int jobLength;              // The time left to process.
    private int jobId;                  // Unique Job identifier.

    /*
    Job() Constructor.
    Parameters: jobId       - The unique identifying key of the Job.
                jobLength   - The amount of processing time that the Job has remaining.
                arrivalTime - The time at which the Job will initially arrive in the waiting queue.
    */
    public Job(int jobId, int jobLength, int arrivalTime) {
        this.jobId = jobId;                 // Set the Job ID.
        this.jobLength = jobLength;         // Set the Job length.
        this.arrivalTime = arrivalTime;     // Set the arrival time.
        this.begunProcessing = false;       // Set the Job as not having begun processing.
    }



    //region Getters & Setters
    /*
    Method Name:    getJobId
    Returns:        int. The ID of the Job.
    */
    public int getJobId() {
        return jobId;
    }

    /*
    Method Name:    getArrivalTime
    Returns:        int. The arrival time of the Job.
    */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /*
    Method Name:    getJobId
    Returns:        int. the length of the Job.
    */
    public int getJobLength() {
        return jobLength;
    }

    /*
    Method Name:    setJobLength
    Parameters:     jobLength   - The value of which to set Job's jobLength.
    Purpose:        Sets the Job.
    */
    public void setJobLength(int jobLength) {
        this.jobLength = jobLength;
    }

    /*
    Method Name:    hasBegunProcessing
    returns:        boolean. true if the job has begun processing. false otherwise.
    */
    public boolean hasBegunProcessing() {
        return this.begunProcessing;
    }

    /*
    Method Name:    setAsBegunProcessing
    Purpose:        Sets the job as begun processing.
    */
    public void setAsBegunProcessing() {
        this.begunProcessing = true;
    }
    //endregion

    /*
    Method Name:    compareTo
    Purpose:        To compare Job instances via their Job lengths.
    Returns:        int. -1 if this Job has less remaining time. 0 if the Job lengths are equal. 1 if this Job has greater remaining time.
    */
    @Override
    public int compareTo(Object o) {
        Job that = (Job)o;
        return Integer.compare(this.getJobLength(), that.getJobLength());
    }

    /*
    Method Name:    memberwiseClone
    Returns:        Job. A memberwise clone of this Job instance.
    */
    public Job memberwiseClone() {
        return new Job(this.jobId, this.jobLength, this.arrivalTime);
    }
}
