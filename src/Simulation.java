import java.util.Random;

public class Simulation {
    Random random;

    public Simulation() {
        random = new Random();
    }

    // TODO: implement this for jobs ...
    private void setType() {
        double rand = this.random.nextDouble();
        if (rand > 0.8) {
            //true;
        } else {
            //false;
        }
    }
}