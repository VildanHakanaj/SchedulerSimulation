import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 1000;
    private Random random;
    private int clock; //
    private ArrayList<Job> jobs; //Store the intial jobs
//    private Queue<Job> queue = new LinkedList<>();
//    private PriorityQueue<Job> pq = new PriorityQueue<>(); //Using a priority Queue to sort he job based on the arrival time.
    Simulation() {
        this.random = new Random(); //Create the new randoms
    }

    //Combines the methods in order to run the simulation
    void runSimulation() {
        jobs = createJobSet1();
        System.out.println("Before the First in first out");
        runFcfs(jobs); //Run the algorithm of first come first serve
    }

    /*
     * Create the job set 1
     * returns @Job[] ArrayList
     * Create and stores all the jobs in the Array List
     * */
    private ArrayList<Job> createJobSet1() {
        double mean, stdDev, min, max;
        int arrivalTime;

        mean = 150;
        stdDev = 20;

        min = mean - (stdDev * 4);
        max = mean + (stdDev * 4);

        //Create the job list
        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            //Create the arrival times
            arrivalTime = (int)Generation.NextGaussian(160, 15);
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), arrivalTime));
        }
        return jobs; //Return the array list containing jobs
    }

    /*
     * Create the job set 2
     * returns @Job[] ArrayList
     * Create and stores all the jobs in the Array List
     * */
    private ArrayList<Job> createJobSet2() {
        double mean, stdDev, min, max, gaussian, rnd;


        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            rnd = random.nextDouble();
            if(rnd > 0.8){ //Check if the job is large
                mean = 250;
                stdDev = 15;
            }else{ //The job is small
                mean = 50;
                stdDev = 5;
            }
            //Range of the gaussian distribution
            min = mean - (stdDev * 4);
            max = mean + (stdDev * 4);
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), (int) Generation.NextGaussian(160, 15)));
        }
        return jobs;
    }

    /*
     * Create the job set 3
     * Create and stores all the jobs in the Array List
     * returns @Job[] ArrayList
     * */
    private ArrayList<Job> createJobSet3() {
        double mean, stdDev, min, max, gaussian, rnd;


        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            rnd = random.nextDouble();
            if(rnd > 0.8){ //Check if the job is large
                mean = 50;
                stdDev = 5;
            }else{ //The job is small
                mean = 250;
                stdDev = 15;
            }
            //Range of the gaussian distribution
            min = mean - (stdDev * 4);
            max = mean + (stdDev * 4);

            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), (int)Generation.NextGaussian(160,15)));
        }

        return jobs;
    }


    // TODO : QUESTIONS
    // Make a method of getting arrival times for jobs.
    /*-----------Question--------
    * [ ] What is Turn around time
    * [ ] Do we add the interarrival to the arrival time.
    * [ ] DO we have to increment the time as we wait or just directly skip
    * [ ] Average response times
    * [ ] What defines the context switch { First come first serve does a job finishing executing count as a switch }
    *
    * */

    // TODO: 2018-10-12
    /*
    * [ ] Add a wait time array
    * [ ] Find the wait time for each job
    *   4-  Find waiting time for all other processes i.e. for process i -> wt[i] = bt[i-1] + wt[i-1] .
    *   5-  Find turnaround time = waiting_time + burst_time for all processes.
    *   6-  Find average waiting time = total_waiting_time / no_of_processes.
    *   7-  Similarly, find average turnaround time = total_turn_around_time / no_of_processes.
    *
    *
    *   [ ] We need an event queue to keep track of who is coming and going.
    *       [ ] because the arrival times will be different for each job we have to sort them based on the arrival time
    * */

    public void runFcfs(ArrayList<Job> jobList){
        this.clock = 0;
        double responseTime = 0.0;
        int contextSwitch = 0;
        Job currentJob; //Store the job
        currentJob = jobList.get(0); //Get the first Job.
        jobList.remove(0); //Remove the first job that has been processed
        clock = currentJob.getJobLength() + currentJob.getArrivalTime();

        while(!jobList.isEmpty()){
            for (int i = 0; i < jobList.size(); i++) {
                if(jobList.get(i).getArrivalTime() <= clock){
                    currentJob = jobList.get(i);
                    jobList.remove(i);
                    break;
                }
            }
            System.out.println("Job " + currentJob.getJobId() + " has arrived at " + currentJob.getArrivalTime() + " | and started processing at: " + clock);
            responseTime += clock -  currentJob.getArrivalTime();
            clock += currentJob.getJobLength();//Run the process.
            System.out.println("Job: " + currentJob.getJobId() + " has finished processing at: " + clock);
        }
        System.out.println("The response time for FCFS is: " + responseTime / NUM_TRIALS);
    }
}