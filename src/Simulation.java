import java.util.*;

class Simulation {

    //region Distribution Constants.
    private final int NUM_TRIALS = 1000;
    private final int NUM_JOB_SETS = 3;
    private final int JOB_LENGTH_STANDARD_DEVIATION_RANGE = 4;  // The range in either direction in std dev's
    private final int MEAN_ARRIVAL = 160;
    private final int STANDARD_DEVIATION_ARRIVAL = 15;
    private final int MEAN_JOB_SET_ONE = 150;
    private final int STANDARD_DEVIATION_JOB_SET_ONE = 20;
    private final int MEAN_JOB_SMALL = 50;                      // 80% of jobs in set 2, 20% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_SMALL = 5;
    private final int MEAN_JOB_LARGE = 250;                     // 20% of jobs in set 2, 80% of jobs in set 3
    private final int STANDARD_DEVIATION_JOB_LARGE = 15;
    private final double PERCENTAGE_OF_LARGE_JOBS_SET_2 = 0.20;
    private final double PERCENTAGE_OF_LARGE_JOBS_SET_3 = 0.80;
    //endregion

    //region Time Slice Constants
    private final int PRE_EMPTION_TIME = 40;
    private final int TIME_SLICE1 = 50;
    private final int TIME_SLICE2 = 75;
    //endregion

    //region Class Data Fields
    private Random random;                  // The 'Random' object instance for use within each simulation algorithm.
    private ArrayList<Job> jobs;            // The initial jobs stored on creation (before arrival).
    private int clock;                      // The simulation clock.
    private double aggregateResponseTime;   //The response time
    private double aggregateTurnAroundTime; //Turn around time
    private double averageResponseTime;     //Average Response time
    private double averageTurnAroundTime;   //Average response time
    int contextSwitchCounter;               //context Switching
    private Job currentJob;
    //endregion

    public Simulation() {
        this.random = new Random();         // Create a new Random object.
    }

    /*
    * Runs the methods needed for the simulation
    * */
    void runSimulation() {
        Table table;                        // The table to fill with data and print to the console.
        int row,col;                        // The row and column indexer for determining the cell to fill with data.
        String tableTitle = "";             // The title to display for the current table (jobset).
        String[][] data = new String[4][6]; // [3 pieces of data + 1 header][5 algorithm simulations + 1 header]
        // Add data to the top row for algorithm headers. Add data to the leftmost column for data headers.
        data[0] = new String[] {"", "FCFS", "SJF", "SJFP", "RR with Q=" + this.TIME_SLICE1, "RR with Q=" + this.TIME_SLICE2};
        data[1][0] = "Avg. TurnAround Time";
        data[2][0] = "Avg. Response Time";
        data[3][0] = "Num. context switch";
        // This leaves (1,1 to 1,5), (2,1 to 2,5), (3,1 to 3,5) to be filled with data.
        // Run all four scheduling algorithms within each of the three job sets ...
        for (int i = 0; i < this.NUM_JOB_SETS; i++) {
            switch(i) {
                case 0:
                    this.jobs = createJobSet1();
                    tableTitle = "Job Set #1";
                    break;
                case 1:
                    this.jobs = createJobSet2();
                    tableTitle = "Job Set #2";
                    break;
                case 2:
                    this.jobs = createJobSet3();
                    tableTitle = "Job Set #3";
                    break;
                default:
                    this.jobs = null;
                    break;
            }

            row = 1; col = 1;       // Init the indexers to the first cell to be written to: [1, 1].

            // Run First Come First Serve Algorithm and store the output data in the correct cells.
            runFCFS(memberwiseCloneJobList(this.jobs));
            insertAlgorithmOutputIntoDataColumn(data, row, col);// Insert avg.turn-time, avg.resp-time, and num c.switches to given column.
            row = 1; col++;         // Reset row to 1. Increment the column.

            // Run Shortest Job First Algorithm and store the output data in the correct cells.
            runSJF(memberwiseCloneJobList(this.jobs));
            insertAlgorithmOutputIntoDataColumn(data, row, col);// Insert avg.turn-time, avg.resp-time, and num c.switches to given column.
            row = 1; col++;         // Reset row to 1. Increment the column.

            // Run Shortest Job First with Preemption Algorithm and store the output data in the correct cells.
            runSJFP(memberwiseCloneJobList(this.jobs));
            insertAlgorithmOutputIntoDataColumn(data, row, col);// Insert avg.turn-time, avg.resp-time, and num c.switches to given column.
            row = 1; col++;         // Reset row to 1. Increment the column.

            // Run Round Robin Algorithm with first time slice and store the output data in the correct cells.
            runRR(memberwiseCloneJobList(this.jobs), TIME_SLICE1);
            insertAlgorithmOutputIntoDataColumn(data, row, col);// Insert avg.turn-time, avg.resp-time, and num c.switches to given column.
            row = 1; col++;         // Reset row to 1. Increment the column.

            // Run Round Robin Algorithm with second time slice and store the output data in the correct cells.
            runRR(memberwiseCloneJobList(this.jobs), TIME_SLICE2);
            insertAlgorithmOutputIntoDataColumn(data, row, col);// Insert avg.turn-time, avg.resp-time, and num c.switches to given column.

            // Create the table given the title(jobset) and the simulated data.
            table = new Table(tableTitle, data);

            // Draw the table to the console.
            table.draw();
            System.out.println();   // Spaces the tables out.
        }
    }

    //region Job Generation Methods

    /*
     * Create the job set 1
     * returns @Job[] ==> ArrayList of jobs
     *
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
     * returns @Job[] ==> ArrayList with jobs
     * */
    private ArrayList<Job> createJobSet3() {
        try { return createLargeSmallJobs(PERCENTAGE_OF_LARGE_JOBS_SET_3); } catch (Exception e ){}
        return null;    // Percentage out of bounds. Set not created.
    }
    //endregion

    //region Scheduling Algorithms

    /*
    * Name: First Come First Serve
    *
    * @param ArrayList<Job> jobList ==> The list of jobs to schedule
    *
    * @return ==> void
    *
    * This algorithm will take the jobs and run them
    * based on whichever comes first.
    * */
    private void runFCFS(ArrayList<Job> jobList){
        resetVar();
        Job currentJob;                                                     // Store the job
        while(!jobList.isEmpty()){                                          // Go through the list until all there is no more jobs
            currentJob = jobList.get(0);                                    // Get the first Job.
            jobList.remove(0);
            if (currentJob.getArrivalTime() > clock) {                      // Check if the job has arrived or not.
                clock = currentJob.getArrivalTime();
            }
            currentJob.setAsBegunProcessing();                              // Not required for this algorithm; Included for consistency.
            aggregateResponseTime += clock -  currentJob.getArrivalTime();  // Add the response time.
            clock += currentJob.getJobLength();                             // Run the process.
            aggregateTurnAroundTime += clock - currentJob.getArrivalTime();
            contextSwitchCounter++;
        }
        // The algorithm has finished.
        calcAndStoreAvgResponseTime();
        calcAndStoreAvgTurnAroundTime();
    }

    /*
    * Name: Shortest Job First
    *
    * @param ArrayList<Job> jobList ==> List of jobs to schedule
    *
    * @return ==> void
    *
    * This algorithm will take the jobs and schedule them
    * based on whichever has arrived and has the shortest
    * job length first.
    * */
    private void runSJF(ArrayList<Job> jobList){
        resetVar();
        ArrayList<Job> arrivedJobs = new ArrayList<>();
        while(jobList.size() > 0 || arrivedJobs.size() > 0){
            if(!arrivedJobs.isEmpty()){
                Collections.sort(arrivedJobs);                                  // Sort the list so that the shortest job is next;
                this.currentJob = arrivedJobs.get(0);
                contextSwitchCounter++;                                         // Update context switch.
                arrivedJobs.remove(0);
                currentJob.setAsBegunProcessing();                              // Not required for this algorithm; Included for consistency.
                this.aggregateResponseTime += clock - currentJob.getArrivalTime();
                this.clock += currentJob.getJobLength();
                this.aggregateTurnAroundTime += (clock - currentJob.getArrivalTime());
                for(int i = 0; i < jobList.size(); i++){
                    if(jobList.get(i).getArrivalTime() <= clock){               // Loop and find all the arrived jobs;
                        arrivedJobs.add(jobList.get(i));                        // Add the jobs that have arrived
                        jobList.remove(i);                                      // Remove the job from the list
                    }
                }
            }else{
                // There are no jobs left in the arrived list --> get the next arriving job and update the clock.
                clock = jobList.get(0).getArrivalTime();                 //Add the time when the job has arrived.
                arrivedJobs.add(jobList.get(0));                         //Add the job in the arrived list.
                jobList.remove(0);                                 //Remove from the JobList.
            }
        }
        // The algorithm has finished.
        calcAndStoreAvgResponseTime();
        calcAndStoreAvgTurnAroundTime();
    }

    /*
    * Name: Shortest Job with Pre-mption
    *
    * @param ArrayList<Job> jobList ==> List of jobs to be schedule
    *
    * @return ==> void
    *
    * This algorithm will take the jobs and schedule them
    * based on whichever has arrived and has the shortest
    * job length first. This time it will run the jobs
    * for a certain amount of time defined by the time slice
    * and will check if any other job have arrived that have a
    * shorter job length then the current job
    * */
    private void runSJFP(ArrayList<Job> jobList) {
        boolean completedProcessFlag = false;                           // Needed for the correct printing to the console of the order of events.
        resetVar();                                                     // Reset the relevant class variables.
        ArrayList<Job> arrivedJobs = new ArrayList<>();                 // Init the arrived list.
        while(!jobList.isEmpty() || !arrivedJobs.isEmpty()) {           // Check if the arrived/unarrived job lists are empty.
            if(!arrivedJobs.isEmpty()) {                                // Check if there are any jobs that have already arrived.
                do {
                    Collections.sort(arrivedJobs);                      // Sort the arrived jobs by job length (remaining processing time).
                    // Context Switch (if it occurs).
                    if((this.currentJob == null) || (currentJob.getJobId() != arrivedJobs.get(0).getJobId())) {
                        if(currentJob != null && currentJob.getJobLength() > 0) {   // Only allow "pre-emption" message if job has not finished.
                        }
                        this.currentJob = arrivedJobs.get(0);           // Get the next job.
                        this.contextSwitchCounter++;                    // Increment the switch counter.
                    }
                    // Add response time of the process to the total response time of the algorithm if the process is running for the first time.
                    if(!currentJob.hasBegunProcessing()) {
                        this.aggregateResponseTime += clock - currentJob.getArrivalTime();
                        currentJob.setAsBegunProcessing();
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
                            arrivedJobs.add(jobList.get(i));            // Add the jobs that have arrived to the arrived jobs queue.
                            jobList.remove(i);                          // Remove the job from the jobList.
                        }else{                                          // Stop going through the list.
                            break;                                      // (further indexing will only hold jobs with even greater arrival times).
                        }
                    }
                    // The following If statement MUST come after the preceding for-loop.
                    if(completedProcessFlag) {                          // If the job has finished ...
                        this.aggregateTurnAroundTime += (clock - currentJob.getArrivalTime());
                        completedProcessFlag = false;                   // Set the flag to false
                    }
                } while (!arrivedJobs.isEmpty());                       // Do this while there are items in the arrived List waiting to process.
            } else {
                clock = jobList.get(0).getArrivalTime();                // Add the time when the job has arrived.
                arrivedJobs.add(jobList.get(0));                        // Add the job in the arrived list.
                jobList.remove(0);                               // Remove from the JobList.
            }
        }
        // The algorithm has finished.
        calcAndStoreAvgResponseTime();
        calcAndStoreAvgTurnAroundTime();
    }

    /*
     * Name: Round Robin
     *
     * @param ArrayList<Job> jobList ==> List of jobs to be schedule
     *
     * @return ==> void
     *
     * This algorithm will take the jobs and schedule them
     * based on who has arrived first. Just like pre-emption, this
     * will run the jobs for a time quantum and then check if any
     * other jobs have arrived or/and waiting in the queue--> if yes than it switches the new job with
     * the old one and places the old one back in the queue to wait its turn again.
     * */
    private void runRR(ArrayList<Job> jobList, int timeSlice) {
        boolean completedProcessFlag = false;                           // Needed for the correct printing to the console of the order of events.
        resetVar();                                                     // Reset the relevant class variables.
        ArrayList<Job> arrivedJobs = new ArrayList<>();                 // Init the arrived list.

        while(!jobList.isEmpty() || !arrivedJobs.isEmpty()) {           // do this while arrived/unarrived job lists aren't empty.
            if(!arrivedJobs.isEmpty()) {                                // Check if there are any jobs that have already arrived.
                do {

                    //Check if a new job is ready to process
                    if (currentJob == null || arrivedJobs.get(0).getJobId() != currentJob.getJobId()) {
                        this.currentJob = arrivedJobs.get(0);           //Get the next job to process
                        this.contextSwitchCounter++;                    //increment the context switch
                    }
                    // Add response time of the process to the total response time
                    // of the algorithm if the process is running for the first time.
                    if(!currentJob.hasBegunProcessing()) {
                        this.aggregateResponseTime += clock - currentJob.getArrivalTime();
                        currentJob.setAsBegunProcessing();
                    }
                    // Running the process for the appropriate length of time.
                    // Based on the time quantum
                    if(currentJob.getJobLength() > timeSlice) {        // If the job is bigger than the time-slice.
                        clock += timeSlice;                            // Run the job for time-slice time.
                        currentJob.setJobLength(currentJob.getJobLength() - timeSlice);// Decrease the time remaining in the job.
                    } else {                                            // Else the job is shorter than the time-slice.
                        clock += currentJob.getJobLength();             // Add the remaining job-time to the clock.
                        currentJob.setJobLength(0);                     // Set the remaining job-time to 0.
                        completedProcessFlag = true;                    // True when a job is completed (for printing purposes).
                        arrivedJobs.remove(0);                    // Remove the finished job.
                    }

                    // Finding the processes that may have
                    // arrived while the current process was running.
                    for(int i = 0; i < jobList.size(); i++){            // Loop to check for any jobs that arrived during processing.
                        if(jobList.get(i).getArrivalTime() <= clock){   // Loop and find all the arrived jobs.
                            arrivedJobs.add(jobList.get(i));            // Add the jobs that have arrived to the arrived jobs queue.
                            jobList.remove(i);                          // Remove the job from the jobList.
                        }else{                                          // Stop going through the list.
                            break;                                      // (further indexing will only hold jobs with even greater arrival times).
                        }
                    }

                    // The following If-Else statement MUST come after the preceding for-loop.
                    if(completedProcessFlag) {                          // If the job has finished ...
                        this.aggregateTurnAroundTime += (clock - currentJob.getArrivalTime());
                        completedProcessFlag = false;                   // Set the flag to false
                    } else {                                            // If the job has not finished ...
                        arrivedJobs.remove(0);                    // Remove the running job from the queue.
                        arrivedJobs.add(currentJob);                    // Add the job to the back of the queue.
                    }
                } while (!arrivedJobs.isEmpty());                       // Do this while there are items in the arrived List waiting to process.

            } else {
                // There are no jobs left in the arrived list --> get the next arriving job and update the clock.
                clock = jobList.get(0).getArrivalTime();                // Add the time when the job has arrived.
                arrivedJobs.add(jobList.get(0));                        // Add the job in the arrived list.
                jobList.remove(0);                               // Remove from the JobList.
            }
        }

        //Calculate averages
        calcAndStoreAvgResponseTime();
        calcAndStoreAvgTurnAroundTime();
    }
    //endregion

    //region Helper Methods
    /*
    *
    * Calculates the average the average response
    *
    * */
    private void calcAndStoreAvgResponseTime() {
        this.averageResponseTime = this.aggregateResponseTime / this.NUM_TRIALS;
    }

    /*
    * Calculate the turnaround time of each algortithm
    * We have placed this methods at the bottom of each of them
    * */
    private void calcAndStoreAvgTurnAroundTime() {
        this.averageTurnAroundTime = this.aggregateTurnAroundTime / this.NUM_TRIALS;
    }

    /*
    * We used this to debug the code
    * */
    private void printAlgorithmInformation() {
        // Print out information pertaining to the algorithms entire run.
        System.out.println("Number of context switches: " + this.contextSwitchCounter);
        System.out.println("Average Response Time: " + this.aggregateResponseTime / NUM_TRIALS);
        System.out.println("The average turn-around time: " + this.aggregateTurnAroundTime / NUM_TRIALS);
    }

    /*
    * Does a deep copy of each of the job lists
    * So that we have the same jobs for all 4
    * of the algorithms.
    *
    * */
    private ArrayList<Job> memberwiseCloneJobList(ArrayList<Job> jobs) {
        ArrayList<Job> clones = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            clones.add(jobs.get(i).memberwiseClone());
        }
        return clones;
    }

    /*
    * We use this method at the top of
    * each algorithm to reset the values
    * to an initial state before running them
    * */
    private void resetVar(){
        clock = 0;
        aggregateResponseTime = 0;
        currentJob = null;
        contextSwitchCounter = 0;
        aggregateTurnAroundTime = 0;
    }


    /*
     Method Name: createLargeSmallJobs
     Behaviour:
     Creates 'large' and 'small' jobs based on a given percentage as a floating point number between [0,1).
     The percentage is the number of large jobs created, with the remainder being the percentage of small jobs created.
     Large and small job properties are defined as class constants at the top of the code.
    */
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

    /*
     * Name:    insertAlgorithmOutputIntoDataColumn
     *
     * @param String[][] data => The data array that will populate the on-screen table.
     *
     * @param int row => the first row to be filled within the given column of the data array.
     *
     * @param int col => The column to be filled within the data array.
     *
     * Inserts the output from the last algorithm to have run into the given column of the given data array.
     * The data to fill the data array is; avg. Turn-around time; avg. Response time; number of context switches.
     * */
    private void insertAlgorithmOutputIntoDataColumn(String[][] data, int row, int col) {
        data[row++][col] = Double.toString(this.averageTurnAroundTime);
        data[row++][col] = Double.toString(this.averageResponseTime);
        data[row][col] = Integer.toString(this.contextSwitchCounter);
    }
    //endregion
}
