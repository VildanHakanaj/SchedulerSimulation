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
        for (int i = 0; i < 10; i++) {
            Job job = new Job(20, 01);
        }
    }
    private void createJobSet2() {

    }

    private void createJobSet3() {

    }
}