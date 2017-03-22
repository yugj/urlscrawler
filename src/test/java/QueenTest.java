import org.junit.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * Created by yugj on 17/3/20.
 */
public class QueenTest {

    @org.junit.Test
    public void testQueen() {
        Queue<String> queue = new LinkedList<String>();

        queue.offer("hell");
        queue.offer("hell");
        queue.add("add");

        for (String hell : queue) {
            System.out.println(hell);
        }
    }
}
