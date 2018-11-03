import java.util.*;

interface ISimulation {
    void runSimulation();
    void runFCFS(ArrayList<Job> jobList);
    void runSJF(ArrayList<Job> jobList);
    void runSJFP(ArrayList<Job> jobList);
    void runRR(ArrayList<Job> jobList, int timeSlice);
}
