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

    // Combines the methods in order to run the simulation.
    // TODO: Possibly add a menu for selecting which algorithm/jobset to run ...
    void runSimulation() {
        // Run all four scheduling algorithms within each of the three job sets ...
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    this.jobs = createJobSet1();
                    System.out.println("Using Job Set #1 ...");
                break;
                case 1:
                    this.jobs = createJobSet2();
                    System.out.println("Using Job Set #2 ...");
                break;
                case 2:
                    this.jobs = createJobSet3();
                    System.out.println("Using Job Set #3 ...");
                break;
            }
            //runFCFS(this.jobs);
            runSJF(this.jobs);
            runSJFP(this.jobs);
            runRR(this.jobs);
        }
        System.out.println("All four algorithms have successfully run using all three job sets.");
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

    // Make a method of getting arrival times for jobs.
    /*
    * First Come First Serve
    * The first Job that comes will be served and done with.
    *  */
    // TODO: Implement response-time and turn around time within this method.
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
    // TODO: Implement response-time and turn around time within this method.
    // TODO: Fix out of bounds bug ...
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
    // Shortest Job First with pre-emption.
    // TODO: Implement response-time and turn around time within this method.
    private void runSJFP(ArrayList<Job> jobList) {
        boolean completedProcessFlag = false;                           // Needed for the correct printing to the console of the order of events.
        resetVar();                                                     // Reset the relevant class variables.
        ArrayList<Job> arrivedJobs = new ArrayList<>();                 // Init the arrived list.
        System.out.println("Starting Shortest Job First with Pre-emption ... \n"); // Print out the algorithm information.
        while(jobList.size() > 0 || arrivedJobs.size() > 0) {           // Check if the arrived/unarrived job lists are empty.
            if(arrivedJobs.size() > 0) {                                // Check if there are any jobs that have already arrived.
                do {
                    Collections.sort(arrivedJobs);                      // Sort the arrived jobs by job length (remaining processing time).
                    // Context Switch (if it occurs).
                    if((this.currentJob == null) || (currentJob.getJobId() != arrivedJobs.get(0).getJobId())) {
                        if(currentJob != null && currentJob.getJobLength() > 0) {   // Only allow "pre-emption" message if job has not finished.
                            System.out.println("Job " + currentJob.getJobId() + " has been pre-empted at time: " + this.clock + " with " +
                                currentJob.getJobLength() + " time left to process."); //
                        }
                        this.currentJob = arrivedJobs.get(0);           // Get the next job.
                        this.contextSwitchCounter++;                    // Increment the switch counter.
                        System.out.println("Job " + currentJob.getJobId() + " has started processing at time: " + this.clock + " with " +
                                currentJob.getJobLength() + " time left to process ...");
                    }
                    // Running the process for the appropriate length of time.
                    if(currentJob.getJobLength() > PRE_EMPTION_TIME) {  // If the job is bigger than the pre-emption ...
                        clock += PRE_EMPTION_TIME;                      // Run the job for pre-emption time.
                        currentJob.setJobLength(currentJob.getJobLength() - PRE_EMPTION_TIME);//Update the time remaining.

                    } else {                                            // Else the job is shorter than the time-slice.
                        clock += currentJob.getJobLength();             // Add the remaining job-time to the clock.
                        currentJob.setJobLength(0);                     // Set the remaining job-time to 0.
                        completedProcessFlag = true;                    // True when a job is completed (for printing purposes).
                        arrivedJobs.remove(0);                   // Remove the finished job.
                    }
                    // Finding the processes that may have arrived while the current process was running.
                    for(int i = 0; i < jobList.size(); i++){            // Loop to check for any jobs that arrived during processing.
                        if(jobList.get(i).getArrivalTime() <= clock){   // Loop and find all the arrived jobs.
                            System.out.println("Job " + jobList.get(i).getJobId() + " has arrived at time: " + jobList.get(i).getArrivalTime() +
                                    " and is awaiting its turn to process ...");
                            arrivedJobs.add(jobList.get(i));            // Add the jobs that have arrived to the arrived jobs queue.
                            jobList.remove(i);                          // Remove the job from the jobList.
                        }else{                                          // Stop going through the list.
                            break;                                      // (further indexing will only hold jobs with even greater arrival times).
                        }
                    }
                    // The following If statement MUST come after the preceding for-loop.
                    if(completedProcessFlag) {                          // If the job has finished ...
                        // Let the user know that the job has finished
                        System.out.println("Job " + currentJob.getJobId() + " has finished processing at time: " + this.clock);
                        completedProcessFlag = false;                   // Set the flag to false
                    }
                } while (arrivedJobs.size() > 0);                       // Do this while there are items in the arrived List waiting to process.
            } else {
                // There are no jobs left in the arrived list --> get the next arriving job and update the clock.
                System.out.println("Job " + jobList.get(0).getJobId() + " has arrived at time: " + jobList.get(0).getArrivalTime() +
                        " and is awaiting its turn to process ...");
                clock = jobList.get(0).getArrivalTime();                // Add the time when the job has arrived.
                arrivedJobs.add(jobList.get(0));                        // Add the job in the arrived list.
                jobList.remove(0);                               // Remove from the JobList.
            }
        }
        // The algorithm has finished. Print out information pertaining to the algorithms entire run.
        System.out.println("\nThe Shortest Job First with Pre-emption sorting algorithm has finished ...");
        System.out.println("Number of context switches: " + this.contextSwitchCounter);
        System.out.println("Average Response Time: " + this.responseTime);
    }

    // Round Robin
    // TODO: Implement response-time and turn around time within this method.
    private void runRR(ArrayList<Job> jobList) {
        boolean completedProcessFlag = false;                           // Needed for the correct printing to the console of the order of events.
        resetVar();                                                     // Reset the relevant class variables.
        ArrayList<Job> arrivedJobs = new ArrayList<>();                 // Init the arrived list.
        System.out.println("Starting Round Robin Algorithm... \n");     // Print out the algorithm information.
        while(jobList.size() > 0 || arrivedJobs.size() > 0) {           // Check if the arrived/unarrived job lists are empty.
            if(arrivedJobs.size() > 0) {                                // Check if there are any jobs that have already arrived.
                do {
                    // Context Switch (if it occurs).
                    if (currentJob == null || arrivedJobs.get(0).getJobId() != currentJob.getJobId()) {
                        this.currentJob = arrivedJobs.get(0);           // Get the next job.
                        this.contextSwitchCounter++;                    // Increment the switch counter.
                        System.out.println("Job " + currentJob.getJobId() + " has started processing at time: " + this.clock + " with " +
                                currentJob.getJobLength() + " time left to process ...");
                    }
                    // Running the process for the appropriate length of time.
                    if(currentJob.getJobLength() > TIME_SLICE) {        // If the job is bigger than the time-slice.
                        clock += TIME_SLICE;                            // Run the job for time-slice time.
                        currentJob.setJobLength(currentJob.getJobLength() - TIME_SLICE);// Decrease the time remaining in the job.
                    } else {                                            // Else the job is shorter than the time-slice.
                        clock += currentJob.getJobLength();             // Add the remaining job-time to the clock.
                        currentJob.setJobLength(0);                     // Set the remaining job-time to 0.
                        completedProcessFlag = true;                    // True when a job is completed (for printing purposes).
                        arrivedJobs.remove(0);                  // Remove the finished job.
                    }
                    // Finding the processes that may have arrived while the current process was running.
                    for(int i = 0; i < jobList.size(); i++){            // Loop to check for any jobs that arrived during processing.
                        if(jobList.get(i).getArrivalTime() <= clock){   // Loop and find all the arrived jobs.
                            System.out.println("Job " + jobList.get(i).getJobId() + " has arrived at time: " + jobList.get(i).getArrivalTime() +
                                    " and is awaiting its turn to process ...");
                            arrivedJobs.add(jobList.get(i));            // Add the jobs that have arrived to the arrived jobs queue.
                            jobList.remove(i);                          // Remove the job from the jobList.
                        }else{                                          // Stop going through the list.
                            break;                                      // (further indexing will only hold jobs with even greater arrival times).
                        }
                    }
                    // The following If-Else statement MUST come after the preceding for-loop.
                    if(completedProcessFlag) {                          // If the job has finished ...
                        // Let the user know that the job has finished
                        System.out.println("Job " + currentJob.getJobId() + " has finished processing at time: " + this.clock);
                        completedProcessFlag = false;                   // Set the flag to false
                    } else {                                            // If the job has not finished ...
                        arrivedJobs.remove(0);                    // Remove the running job from the queue.
                        arrivedJobs.add(currentJob);                    // Add the job to the back of the queue.
                    }
                } while (arrivedJobs.size() > 0);                       // Do this while there are items in the arrived List waiting to process.
            } else {
                // There are no jobs left in the arrived list --> get the next arriving job and update the clock.
                System.out.println("Job " + jobList.get(0).getJobId() + " has arrived at time: " + jobList.get(0).getArrivalTime() +
                        " and is awaiting its turn to process ...");
                clock = jobList.get(0).getArrivalTime();                // Add the time when the job has arrived.
                arrivedJobs.add(jobList.get(0));                        // Add the job in the arrived list.
                jobList.remove(0);                               // Remove from the JobList.
            }
        }
        // The algorithm has finished. Print out information pertaining to the algorithms entire run.
        System.out.println("\nThe Round Robin sorting algorithm has finished ...");
        System.out.println("Number of context switches: " + this.contextSwitchCounter);
        System.out.println("Average Response Time: " + this.responseTime);
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