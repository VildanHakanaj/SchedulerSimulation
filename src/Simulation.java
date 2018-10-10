import java.util.Random;

class Simulation {
    private Random random;
    private int clock;

    Simulation() {
        this.random = new Random();
        this.clock = 0;
    }
    void runSimulation(){

    }

    private void createJobSet1() {
        int mean, stdDev;
        mean = 150;
        stdDev = 20;

        for (int i = 0; i < 10; i++) {
            Job job = new Job(20, 01);
        }
    }
    private void createJobSet2() {
        int mean, stdDev;
        //make job
        //set job type
        /*set mean/stdDev based on job type
        if() {

        } else {

        }
        */

    }

    private void createJobSet3() {
        int mean, stdDev;

    }
}