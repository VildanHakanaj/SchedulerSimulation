import com.sun.istack.internal.Nullable;

import javax.lang.model.type.NullType;
import java.util.LinkedList;
import java.util.Queue;

public class Job implements Comparable{

//    private boolean type;   // true === large job. false === small job.
    private int arrivalTime;
    private int jobLength; //The time it takes to finish
    private int jobId;

    // Constructor
    public Job(int jobId, int jobLength) {
        this.jobId = jobId;
        this.jobLength = jobLength;
    }

//    // Getters & Setters
//    public boolean isType() {
//        return type;
//    }

    public int getJobId() {
        return jobId;
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

    @Override
    public String toString() {
        return "Process id = " + this.jobId + "\nJob Arrival TIme: " + this.arrivalTime + "\nJob Run Time: " + this.jobLength + "\n\n";
    }
}
