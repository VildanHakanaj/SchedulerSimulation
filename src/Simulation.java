import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 1000;
    // Distribution Constants.
    private final int JOB_LENGTH_STANDARD_DEVIATION_RANGE = 4;  // The range in either direction in std dev's
    private final int MEAN_ARRIVAL = 160;
    private final int STANDARD_DEVIATION_ARRIVAL = 15;
    private final int MEAN_JOB_SET_ONE = 150;
    private final int STANDARD_DEVIATION_JOB_SET_ONE = 20;
    private final int MEAN_JOB_SMALL = 50;                      // 80% of jobs in set 2, 20% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_SMALL = 5;
    private final int MEAN_JOB_LARGE = 250;                     // 20% of jobs in set 2, 80% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_LARGE = 15;
    private final double PERCENTAGE_OF_LARGE_JOBS_SET_2 = 0.80;
    private final double PERCENTAGE_OF_LARGE_JOBS_SET_3 = 0.20;
    // End of the Distribution Constant.
    // Time slice and Pre-emption.
    private final int PRE_EMPTION_TIME = 40;
    private final int TIME_SLICE = 50;
    private final int TIME_SLICE2 = 75;
    // End of constant declaration block.

    // Class variables that are set in and used in each of the four scheduling algorithms ...
    private ArrayList<Job> jobs;        // The initial jobs stored on creation (before arrival).
    private Random random;              // The 'Random' object instance for use within each simulation algorithm.
    private int clock;                  // The simulation clock.
    private double responseTime;        // Response time.
    int contextSwitchCounter;
    private Job currentJob;

    // Class Constructor.
    public Simulation() {
        this.random = new Random(); //Create the new randoms
    }

    //Combines the methods in order to run the simulation
    void runSimulation() {
        jobs = createJobSet2();
//        System.out.println("Before the First in first out");
//        runFCFS(jobs); //Run the algorithm of first come first serve
//        runSJF(jobs);
        runRR(jobs);
    }
    /*
     * Create the job set 1
     * returns @Job[] ArrayList
     * Create and stores all the jobs in the Array List
     * */
    private ArrayList<Job> createJobSet1() {
        double minLength, maxLength, minArrival, maxArrival;
        int arrivalTime = 0;

        minLength = MEAN_JOB_SET_ONE - (STANDARD_DEVIATION_JOB_SET_ONE * JOB_LENGTH_STANDARD_DEVIATION_RANGE);
        maxLength = MEAN_JOB_SET_ONE + (STANDARD_DEVIATION_JOB_SET_ONE * JOB_LENGTH_STANDARD_DEVIATION_RANGE);
        minArrival = MEAN_ARRIVAL - (STANDARD_DEVIATION_ARRIVAL * (MEAN_ARRIVAL / STANDARD_DEVIATION_ARRIVAL));
        maxArrival = MEAN_ARRIVAL + (STANDARD_DEVIATION_ARRIVAL * (MEAN_ARRIVAL / STANDARD_DEVIATION_ARRIVAL));

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
        try { return createLargeSmallJobs(PERCENTAGE_OF_LARGE_JOBS_SET_2); } catch (Exception e ){}
        return null;    // Percentage out of bounds. Set not created.
    }
    /*
     * Create the job set 3
     * Create and stores all the jobs in the Array List
     * returns @Job[] ArrayList
     * */
    private ArrayList<Job> createJobSet3() {
        try { return createLargeSmallJobs(PERCENTAGE_OF_LARGE_JOBS_SET_3); } catch (Exception e ){}
        return null;    // Percentage out of bounds. Set not created.
    }
    // TODO : QUESTIONS
    // Make a method of getting arrival times for jobs.
    /*
    * First Come First Serve
    * The first Job that comes will be served and done with.
    *  */
    private void runFCFS(ArrayList<Job> jobList){
        resetVar();
        Job currentJob; //Store the job
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
    }

    // Shortest job First
    private void runSJF(ArrayList<Job> jobList){
        resetVar();
        ArrayList<Job> arrivedJobs = new ArrayList<>();
        currentJob = jobList.get(0);
        jobList.remove(0);
        clock = currentJob.getJobLength() + currentJob.getArrivalTime();    // Update the clock for the first job;
        while(jobList.size() > 0 || arrivedJobs.size() > 0){
            for(int i = 0; i < jobList.size(); i++){
                if(jobList.get(i).getArrivalTime() <= clock){               // Loop and find all the arrived jobs;
                    arrivedJobs.add(jobList.get(i));                        // Add the jobs that have arrived
                    jobList.remove(i);                                      // Remove the job from the list
                }
            }
            Collections.sort(arrivedJobs);                                  //Sort the list so that the shortest job is next;
            if(arrivedJobs.size() > 0){
                currentJob = arrivedJobs.get(0);
                arrivedJobs.remove(0);
                System.out.println("Job " + currentJob.getJobId() + " has arrived at " + currentJob.getArrivalTime() + " | and started processing at: " + clock);
                responseTime += clock - currentJob.getArrivalTime();
                clock += currentJob.getJobLength();
                System.out.println("Job: " + currentJob.getJobId() + " has finished processing at: " + clock);
                System.out.println("The response time for SJF is: " + responseTime / NUM_TRIALS);
            }else{
                clock += 1; //Increment
            }
        }
    }

    /*
    *
    * This method will run the shortest job first and will pre-empt the job that
    * is currently running in case a shorter job comes in.
    *
    * */
    // Shortest Job First with preemption
    private void runSJFP(ArrayList<Job> jobList) {
        boolean completedProcessFlag = false;   // Needed for the correct printing to the console of the order of events.
        resetVar();//Reset the variables
        ArrayList<Job> arrivedJobs = new ArrayList<>(); //Create arrived list
        System.out.println("Starting Shortest Job First with Pre-emption ... \n"); //Print out the job information
        while(jobList.size() > 0 || arrivedJobs.size() > 0) { //Check if the the lists aren't empty
            if(arrivedJobs.size() > 0) { //Check if there is any jobs in the array
                do {
                    Collections.sort(arrivedJobs); //Sort the arrived jobs
                    if((this.currentJob == null) || (currentJob.getJobId() != arrivedJobs.get(0).getJobId())) { // Context switch.
                        this.contextSwitchCounter++;// Add the counter switch
                        if(currentJob != null && currentJob.getJobLength() > 0) { //check if the
                            System.out.println("Job " + currentJob.getJobId() + " has been pre-empted at time: " + this.clock + " with " +
                                currentJob.getJobLength() + " time left to process."); //
                        }
                        this.currentJob = arrivedJobs.get(0); //Get the next job
                        System.out.println("Job " + currentJob.getJobId() + " has started processing at time: " + this.clock + " with " +
                                currentJob.getJobLength() + " time left to process ...");
                    }
                    if(currentJob.getJobLength() > PRE_EMPTION_TIME) { //If the job is bigger than the pre emption
                        clock += PRE_EMPTION_TIME; //Run the job for pre emption time
                        currentJob.setJobLength(currentJob.getJobLength() - PRE_EMPTION_TIME);//Update the time remaining.

                    } else { //Else the job is shorter than the pre-emption.
                        clock += currentJob.getJobLength(); //Add the remaining time to the clock
                        currentJob.setJobLength(0); //Set the job to 0
                        completedProcessFlag = true; //True when a job is completed
                        arrivedJobs.remove(0); //Remove the finished jobed
                    }
                    for(int i = 0; i < jobList.size(); i++){                          //Loop to check for arrived job.
                        if(jobList.get(i).getArrivalTime() <= clock){            // Loop and find all the arrived jobs;
                            System.out.println("Job " + jobList.get(i).getJobId() + " has arrived at time: " + jobList.get(i).getArrivalTime() +
                                    " and is awaiting its turn to process ...");
                            arrivedJobs.add(jobList.get(i));                    // Add the jobs that have arrived
                            jobList.remove(i);                                  // Remove the job from the jobList
                        }else{                                          //Else stop going through the list
                            break;
                        }
                    }
                    if(completedProcessFlag) { //If the job has finished
                        //Let the user know that the job has finished
                        System.out.println("Job " + currentJob.getJobId() + " has finished processing at time: " + this.clock);
                        completedProcessFlag = false; //Set the flag to false
                    }
                } while (arrivedJobs.size() > 0); //Do this while there is items in the arrived List
            //There is nothing in the arrived list so get the next job and update the clock
            } else {
                System.out.println("Job " + jobList.get(0).getJobId() + " has arrived at time: " + jobList.get(0).getArrivalTime() +
                        " and is awaiting its turn to process ...");
                clock = jobList.get(0).getArrivalTime(); //Add the time when the job has arrived
                arrivedJobs.add(jobList.get(0)); //Add the job in the arrived list
                jobList.remove(0); //Remove from the Joblist
            }
        }
        System.out.println("All processes have completed.");//End of the simulation

        //Show the number of times the job was switched
        System.out.println("The context Switch number: " + this.contextSwitchCounter);
    }

    // Round Robin
    private void runRR(ArrayList<Job> jobList) {
        boolean completedProcessFlag = false;                           // Needed for the correct printing to the console of the order of events.
        resetVar();                                                     // Reset the relevant class variables.
        ArrayList<Job> arrivedJobs = new ArrayList<>();                 // Init the arrived list.
        System.out.println("Starting Round Robin Algorithm... \n");     // Print out the algorithm information.
        while(jobList.size() > 0 || arrivedJobs.size() > 0) {           // Check if the the lists are empty.
            if(arrivedJobs.size() > 0) {                                // Check if there are any jobs that have already arrived.
                do{
                    if(arrivedJobs.size() > 0){                         //if the job list has any additions
                        if(currentJob != null){                         //Check if the current job is still active
                            arrivedJobs.add(currentJob);                //Add the current job back in the queue
                        }
                        currentJob = arrivedJobs.get(0);                //Get the next job in the list
                        contextSwitchCounter++;                         //Add the context counter
                        arrivedJobs.remove(0);                    //Remove the job from the list.
                    }
                    if(currentJob.getJobLength() > TIME_SLICE){                             //Check if the Run time is bigger than the time slice
                        clock += TIME_SLICE;                                                //Update the clock with the time slice
                        currentJob.setJobLength(currentJob.getJobLength() - TIME_SLICE);    //Reduce the time left to run
                    }else{
                        clock += currentJob.getJobLength();                                 //Run the job until its finished
                        completedProcessFlag = true;                                        //Set the flag to true for a finished job
                    }
                    if(completedProcessFlag){                                               //Check if a job has finished
                        System.out.printf("Job %s has finished processing at time %s", currentJob.getJobId(), clock);   //Print the information
                        currentJob = null;                                                                              //Set the job null since it finished.
                        completedProcessFlag = false;                   //Set the flag back to false
                    }
                    for (int i = 0; i < jobList.size(); i++){           //Check if we need to update the arrived list.
                        if(jobList.get(i).getArrivalTime() <= clock){ //Check if the any jobs have arrived withing that time slice
                            arrivedJobs.add(jobList.get(i));          //Add the jobs into the arrived list.
                            jobList.remove(0);
                        }else{
                            break;                                    //Break if the arrival time i bigger than the clock
                        }
                    }
                }while(currentJob != null);                         //Do it until the job finishes
            } else {                                                //Else pick the next job
                System.out.println("Job " + jobList.get(0).getJobId() + " has arrived at time: " + jobList.get(0).getArrivalTime() +
                        " and is awaiting its turn to process ...");
                clock = jobList.get(0).getArrivalTime();                // Add the time when the job has arrived
                arrivedJobs.add(jobList.get(0));                        // Add the job in the arrived list
                jobList.remove(0);                               // Remove from the Joblist
            }
        }
    }
    /*****************************************
     *           Assistant methods           *
     *****************************************
     */
    /*Reset all the variables to start state of the schedule*/
    private void resetVar(){
        clock = 0;
        responseTime = 0;
        currentJob = null;
        contextSwitchCounter = 0;
    }

    // Method Name: createLargeSmallJobs
    // Behaviour:   creates large and small jobs based on the given percentage, given as a floating point number between [0,1).
    //              The percentage is the number of large jobs created, with the remainder being the percentage of small jobs created.
    //              Large and small job properties are defined as class constants at the top of the code.
    private ArrayList<Job> createLargeSmallJobs(double percentageOfLargeJobs) throws Exception {
        if(percentageOfLargeJobs < 0 || percentageOfLargeJobs >= 1) {
            throw new Exception("Given percentage is out of bounds.");
        }
        double mean, stdDev, minLength, maxLength, minArrival, maxArrival, rnd;
        int arrivalTime = 0;

        minArrival = MEAN_ARRIVAL - (STANDARD_DEVIATION_ARRIVAL * (MEAN_ARRIVAL / STANDARD_DEVIATION_ARRIVAL));
        maxArrival = MEAN_ARRIVAL + (STANDARD_DEVIATION_ARRIVAL * (MEAN_ARRIVAL / STANDARD_DEVIATION_ARRIVAL));

        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            rnd = random.nextDouble();
            if(rnd > percentageOfLargeJobs){            // The job is small.
                mean = MEAN_JOB_SMALL;
                stdDev = STANDARD_DEVIATION_JOB_SMALL;
            }else{                                      // The job is large.
                mean = MEAN_JOB_LARGE;
                stdDev = STANDARD_DEVIATION_JOB_LARGE;
            }
            arrivalTime += (int) Generation.NextGaussian(MEAN_ARRIVAL, STANDARD_DEVIATION_ARRIVAL, minArrival, maxArrival); //Get the arrival Time
            //Range of the gaussian distribution
            minLength = mean - (stdDev * JOB_LENGTH_STANDARD_DEVIATION_RANGE);
            maxLength = mean + (stdDev * JOB_LENGTH_STANDARD_DEVIATION_RANGE);
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, minLength, maxLength), arrivalTime));
        }
        return jobs;
    }
}