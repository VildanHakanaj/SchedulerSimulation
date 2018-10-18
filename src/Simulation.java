import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 20;
    private Random random; //
    private int clock = 0; //The simulation clock
    private double responseTime = 0.0; //Response time
    int contextSwitch = 0;
    private int arrivalTime = 0; //The first arrival time
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

        mean = 150;
        stdDev = 20;

        min = mean - (stdDev * 4);
        max = mean + (stdDev * 4);

        //Create the job list
        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            //Create the arrival times
            arrivalTime += (int)Generation.NextGaussian(160, 15);
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
            arrivalTime += (int) Generation.NextGaussian(160, 15); //Get the arrival Time
            //Range of the gaussian distribution
            min = mean - (stdDev * 4);
            max = mean + (stdDev * 4);
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), arrivalTime));
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

            arrivalTime += (int)Generation.NextGaussian(160,15);
            //Range of the gaussian distribution
            min = mean - (stdDev * 4);
            max = mean + (stdDev * 4);
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max), arrivalTime));
        }
        return jobs;
    }
    // TODO : QUESTIONS
    // Make a method of getting arrival times for jobs.
    /*
    * First Come First Serve
    * The first Job that comes will be served and done with.
    *  */
    public void runFcfs(ArrayList<Job> jobList){
        Job currentJob; //Store the job
        currentJob = jobList.get(0); //Get the first Job.
        jobList.remove(0); //Remove the first job that has been processed
        clock = currentJob.getJobLength() + currentJob.getArrivalTime(); //Update the first clock time

        while(!jobList.isEmpty()){ //Go through the list until all there is no more jobs
            for (int i = 0; i < jobList.size(); i++) //Loop to find the next job that arrives.
                if(jobList.get(i).getArrivalTime() <= clock){ //if the Job has arrived
                    currentJob = jobList.get(i); //Get the job from the list
                    jobList.remove(i); //Then remove it.
                    break;
                }
            System.out.println("Job " + currentJob.getJobId() + " has arrived at " + currentJob.getArrivalTime() + " | and started processing at: " + clock);
            responseTime += clock -  currentJob.getArrivalTime(); //Add the respons time.
            clock += currentJob.getJobLength();//Run the process.
            System.out.println("Job: " + currentJob.getJobId() + " has finished processing at: " + clock);
        }
        System.out.println("The response time for FCFS is: " + responseTime / NUM_TRIALS);
    }

    public void sJf(){

    }
}