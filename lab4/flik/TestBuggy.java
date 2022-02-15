package flik;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

public class TestBuggy {

    @Test
    public void randomTestFlik() {
        int randomNumber = StdRandom.uniform(0, 500);
        int j = randomNumber;
        for (int i = randomNumber; i < 1000; i++) {
            assertTrue(String.format("i:%d not same as j:%d ??", i, j), Flik.isSameNumber(i, j));
            j++;
        }
    }

    @Test
    public void TestFlik1() {
        int j = 0;
        for (int i = 0; i < 128; i++) {
            assertTrue(String.format("i:%d not same as j:%d ??", i, j), Flik.isSameNumber(i, j));
            j++;
        }
    }

    @Test
    public void TestFlik2() {
        int j = 128;
        for (int i = 128; i < 1000; i++) {
            assertTrue(String.format("i:%d not same as j:%d ??", i, j), Flik.isSameNumber(i, j));
            j++;
        }
    }
}
