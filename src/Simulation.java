import java.util.*;

class Simulation {
    private final int NUM_TRIALS = 10;

    private Random random;
    private int clock;
    ArrayList<Job> jobs = new ArrayList<>();

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

    //Create jobs
    private ArrayList<Job> createJobSet1() {
        double mean, stdDev, min, max, gaussian;
        mean = 150;
        stdDev = 20;
        min = mean - (stdDev * 4);
        max = mean + (stdDev * 4);

        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < NUM_TRIALS; i++) {
            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max)));
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

            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max)));
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

            jobs.add(new Job(i, (int)Generation.NextGaussian(mean,stdDev, min, max)));
        }

        return jobs;
    }

    // TODO: 2018-10-11
    //  SHCEDULING
    //

    public void runFcfs(ArrayList<Job> list){
        this.clock = 0; //Set clock to 0;
        Job currentJob;
        System.out.println("Starting Processing ...\n");
        while(list.size() > 0){
            // Arrival
            currentJob = list.get(0);
            list.remove(0);
            currentJob.setArrivalTime(this.clock);
            System.out.println("Job " + currentJob.getJobId() + " started processing at time: " + currentJob.getArrivalTime());
            // Processing
            this.clock += currentJob.getJobLength();
            // Terminating
            System.out.println("Job " + currentJob.getJobId() + " finished processing at time: " + this.clock);
        }
        System.out.println("Finished Processing.");


    }



}