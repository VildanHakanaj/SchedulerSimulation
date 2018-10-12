import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 10;

    private Random random;
    private int clock;
    ArrayList<Job> jobs;
    ArrayList<Job> queue;

    Simulation() {
        this.random = new Random();
        this.clock = 0;
    }

    void runSimulation() {
        jobs = createJobSet1();
        runFcfs(jobs);
        /*
        for (int i = 0; i < NUM_TRIALS; i++) {
            String s;
            s = jobs.get(i).toString();
            System.out.println(s);
        }
        */
    }

    private double createArrival(){
        return Generation.NextGaussian(120, 80);
    }

    //Create jobs
    private ArrayList<Job> createJobSet1() {
        double mean, stdDev, min, max;
        int arrivalTime;
        arrivalTime = (int)createArrival();
        mean = 150;
        stdDev = 20;
        min = mean - (stdDev * 4);
        max = mean + (stdDev * 4);

        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), arrivalTime));
        }

        return jobs;
    }

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

            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), (int)createArrival()));
        }

        return jobs;
    }

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

            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), (int)createArrival()));
        }

        return jobs;
    }

    // TODO: 2018-10-11
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
    * */

    public void runFcfs(ArrayList<Job> jobList){
        this.queue = new ArrayList<Job>();
        this.clock = 0; //Set clock to 0;
        int[] waitTime = new int[NUM_TRIALS];
        Job currentJob;
        System.out.println("Starting Processing ...\n");
        while(jobList.size() > 0){
            // Check if any jobs within jobList have arrived
            // Arrival
            currentJob = jobList.get(0);
            jobList.remove(0); //Remove the job from the list.
            //currentJob.setArrivalTime(this.clock);
            System.out.println("Job " + currentJob.getJobId() + " started processing at time: " + currentJob.getArrivalTime());
            // Processing
            this.clock += currentJob.getJobLength();
            // Terminating the job has finished processing.
            System.out.println("Job " + currentJob.getJobId() + " finished processing at time: " + this.clock);
            waitTime[currentJob.getJobId()] = currentJob.getJobLength() - currentJob.getArrivalTime(); //Find the wait time
            System.out.println("The job waited for " + waitTime[currentJob.getJobId()]);
        }
        System.out.println("Finished Processing.");
    }
}