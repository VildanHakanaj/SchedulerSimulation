import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 20;

    private Random random; //
    private int clock = 0; //The simulation clock
    private double responseTime = 0.0; //Response time
    int contextSwitch = 0;
    private int arrivalTime = 0; //The first arrival time
    private Job currentJob; //Store the job

    //Constant Declarations
    private final int MEAN_ARRIVAL = 160;
    private final int STANDARD_DEVIATION_ARRIVAL = 15;
    private final int MEAN_JOB_SET_ONE = 150;
    private final int STANDARD_DEVIATION_JOB_SET_ONE = 20;
    private final int MEAN_JOB_SET_TWO_SMALL = 50;          // 80% of jobs in set 2, 20% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_SMALL = 5;
    private final int MEAN_JOB_SET_TWO_LARGE = 250;         // 20% of jobs in set 2, 80% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_LARGE = 15;
    //End of constant declaration block
    private ArrayList<Job> jobs; //Store the intial jobs


    Simulation() {
        this.random = new Random(); //Create the new randoms
    }

    //Combines the methods in order to run the simulation
    void runSimulation() {
        jobs = createJobSet1();
        System.out.println("Before the First in first out");
        runFCFS(jobs); //Run the algorithm of first come first serve
    }
    /*
     * Create the job set 1
     * returns @Job[] ArrayList
     * Create and stores all the jobs in the Array List
     * */
    private ArrayList<Job> createJobSet1() {
        double minLength, maxLength, minArrival, maxArrival;
        int arrivalTime = 0;
        minLength = MEAN_JOB_SET_ONE - (STANDARD_DEVIATION_JOB_SET_ONE * 4);
        maxLength = MEAN_JOB_SET_ONE + (STANDARD_DEVIATION_JOB_SET_ONE * 4);
        minArrival = MEAN_ARRIVAL - (STANDARD_DEVIATION_ARRIVAL * 8);
        maxArrival = MEAN_ARRIVAL + (STANDARD_DEVIATION_ARRIVAL * 8);

        //Create the job list
        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            //Create the arrival times
            arrivalTime += (int)Generation.NextGaussian(MEAN_ARRIVAL, STANDARD_DEVIATION_ARRIVAL, minArrival, maxArrival);
            jobs.add(new Job(i, (int)Generation.NextGaussian(MEAN_JOB_SET_ONE,STANDARD_DEVIATION_JOB_SET_ONE, minLength, maxLength), arrivalTime));
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
    public void runFCFS(ArrayList<Job> jobList){
        resetVar();
        currentJob = jobList.get(0); //Get the first Job.
        jobList.remove(0); //Remove the first job that has been processed
        clock = currentJob.getJobLength() + currentJob.getArrivalTime(); //Update the first clock time
        while(!jobList.isEmpty()){ //Go through the list until all there is no more jobs
            for (int i = 0; i < jobList.size(); i++) { //Loop to find the next job that arrives.
                if (jobList.get(i).getArrivalTime() <= clock) { //if the Job has arrived
                    currentJob = jobList.get(i); //Get the job from the list
                    jobList.remove(i); //Then remove it.
                    break;
                }
            }
//            System.out.println("Job " + currentJob.getJobId() + " has arrived at " + currentJob.getArrivalTime() + " | and started processing at: " + clock);
            responseTime += clock -  currentJob.getArrivalTime(); //Add the respons time.
            clock += currentJob.getJobLength();//Run the process.
            System.out.println("Job: " + currentJob.getJobId() + " has finished processing at: " + clock);
        }
//        System.out.println("The response time for FCFS is: " + responseTime / NUM_TRIALS);
    }

    public void runSJF(ArrayList<Job> jobList){
        resetVar();
        ArrayList<Job> arrivedJobs = new ArrayList<>();
        currentJob = jobList.get(0);
        jobList.remove(0);
        clock = currentJob.getJobLength() + currentJob.getArrivalTime(); //Update the clock for the first job;
        while(jobList.size() > 0){
            for(int i = 0; i < jobList.size(); i++){
                if(jobList.get(i).getArrivalTime() < clock){ //Loop and find all the arrived jobs;
                    arrivedJobs.add(jobList.get(i)); //Add the jobs that have arrived
                    jobList.remove(i);//Remove the job from the list
                }
            }
            Collections.sort(arrivedJobs);//Sort the list so that the shortest job is next;
            currentJob = arrivedJobs.get(0);
            responseTime += clock - currentJob.getArrivalTime();
            clock += currentJob.getJobLength();
        }
    }
    /****************************************
     *           Assitant methods
     **************************************
     */
    /*Reset all the variables to start state of the schedule*/
    private void resetVar(){
        clock = 0;
        responseTime = 0;
        currentJob = null;
        contextSwitch = 0;
    }


}