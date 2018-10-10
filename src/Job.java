public class Job implements Comparable{

    private boolean type;   // true === large job. false === small job.
    private int arrivalTime;
    private int jobLength;

    // Constructor
    public Job(int arrivalTime, int jobLength) {
        this.arrivalTime = arrivalTime;
        this.jobLength = jobLength;
    }

    // Getters & Setters
    public boolean isType() {
        return type;
    }

    /*
    * Determine the type of job;
    * if job is a large or small job;
    * */
    public void setType(double type) {
        if(type > 0.8){
            this.type = true;
        }else{
            this.type = false;
        }
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
        return Integer.compare(this.jobLength, that.jobLength);
    }
}
