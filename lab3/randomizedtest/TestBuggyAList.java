package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    AListNoResizing<Integer> a = new AListNoResizing<>();
    BuggyAList<Integer> b = new BuggyAList<>();

    @Test
    public void testThreeAddThreeRemove() {
        int[] t = {4, 5, 6};
        for (int i : t) {
            a.addLast(i);
            b.addLast(i);
        }
        for (int j = 0; j < 3; j++) {
            assertEquals(a.removeLast(), b.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                a.addLast(randVal);
                b.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(a.size(), b.size());
            } else if (a.size() != 0 && b.size() != 0 && operationNumber == 2) {
                assertEquals(a.getLast(), b.getLast());
                assertEquals(a.removeLast(), b.removeLast());
            }
        }
    }
}
