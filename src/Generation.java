import java.util.Random;

public class Generation {
    static Random random = new Random();
    private static int PREVIOUS_TIME = 0;
    public static void resetArrivalTimeCounter() {
        PREVIOUS_TIME = 0;
    }
    

        /*
         * min = mean - (stdDev * 4);
         * max = mean + (stdDev * 4);
        */

    // Method Name: NextGaussian
    // Returns:     double value based on a Gaussian distribution with a mean of 0 and a standard distribution of 1.
    // The following method was derived from the one found at this website ...
    // https://www.alanzucconi.com/2015/09/16/how-to-sample-from-a-gaussian-distribution/
    public static double NextGaussian() {
        double v1, v2, s;
        do {
            v1 = 2.0 * random.nextDouble() - 1.0;
            v2 = 2.0 * random.nextDouble() - 1.0;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1.0 || s == 0);

        s = Math.sqrt((-2.0 * Math.log(s)) / s);

        return v1 * s;
    }

    // Method Name: NextGaussian
    // Returns:     double value based on a Gaussian distribution with a given mean and a standard distribution.
    // The following method was derived from the one found at this website ...
    // https://www.alanzucconi.com/2015/09/16/how-to-sample-from-a-gaussian-distribution/
    public static double NextGaussian(double mean, double standard_deviation) {
        return mean + NextGaussian() * standard_deviation;
    }

    // Method Name: NextGaussian
    // Returns:     double value based on a Gaussian distribution with a given mean and a standard distribution.
    //              the value returned will fall between the given min and max values.
    // The following method was derived from the one found at this website ...
    // https://www.alanzucconi.com/2015/09/16/how-to-sample-from-a-gaussian-distribution/
    public static double NextGaussian(double mean, double standard_deviation, double min, double max) {
        double x;
        do {
            x = NextGaussian(mean, standard_deviation);
        } while (x < min || x > max);
        return x;
    }
}
