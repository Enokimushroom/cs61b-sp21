package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {

    @Test
    public void compareStrTest() {

        MaxArrayDeque<String> max1 = new MaxArrayDeque<>(new strComparator());

        assertTrue("A newly initialized LLDeque should be empty", max1.isEmpty());
        max1.addFirst("front");

        assertEquals("back", max1.max());
        assertFalse("max1 should now contain 1 item", max1.isEmpty());

        max1.addLast("front!");
        assertEquals("front", max1.max());

        max1.addLast("back");
        assertEquals("back", max1.max());

        System.out.println("Printing out deque: ");
        max1.printDeque();

    }

    @Test
    public void compareStrLenTest() {

        MaxArrayDeque<String> max1 = new MaxArrayDeque<>(new strLenComparator());

        assertTrue("A newly initialized LLDeque should be empty", max1.isEmpty());
        max1.addFirst("front");

        assertEquals("front", max1.max());
        assertFalse("max1 should now contain 1 item", max1.isEmpty());

        max1.addLast("middle");
        assertEquals("middle", max1.max());

        max1.addLast("back");
        assertEquals("middle", max1.max());

        System.out.println("Printing out deque: ");
        max1.printDeque();

    }

    @Test
    public void compareIntTest() {

        MaxArrayDeque<Integer> max1 = new MaxArrayDeque<>(new intComparator());

        assertTrue("A newly initialized LLDeque should be empty", max1.isEmpty());
        max1.addFirst(3);

        assertEquals((Integer) 3, max1.max());
        assertFalse("max1 should now contain 1 item", max1.isEmpty());

        max1.addLast(4);
        assertEquals((Integer) 4, max1.max());

        max1.addLast(1);
        assertEquals((Integer) 4, max1.max());

        System.out.println("Printing out deque: ");
        max1.printDeque();

    }

    private static class strComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int len1 = o1.length();
            int len2 = o2.length();
            for (int i = 0; i < Math.min(len1, len2); i++) {
                char ch1 = o1.charAt(i);
                char ch2 = o2.charAt(i);
                if (ch1 != ch2) {
                    return ch2 - ch1;
                }
            }
            if (len1 != len2) {
                return len2 - len1;
            }
            return 0;
        }
    }

    private static class strLenComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

    private static class intComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }


}
