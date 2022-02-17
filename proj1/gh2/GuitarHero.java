package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static double frequency(int i) {
        return 440.0 * Math.pow(2, (i - 24) / 12.0);
    }

    public static GuitarString createString(char key) {
        return new GuitarString(frequency(keyboard.indexOf(key)));
    }

    public static void main(String[] args) {

        GuitarString keyType = new GuitarString(440.0);

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) != -1) {
                    keyType = createString(key);
                    keyType.pluck();
                }
            }

            /* compute the superposition of samples */
            //double sample = stringA.sample() + stringC.sample();

            /* play the sample on standard audio */
            //StdAudio.play(sample);
            StdAudio.play(keyType.sample());

            /* advance the simulation of each guitar string by one step */
            keyType.tic();
        }
    }
}
