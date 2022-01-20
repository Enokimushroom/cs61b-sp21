package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    private static void countAddLast(int N) {
        int i = 0;
        AList<Integer> a = new AList<>();
        while (i < N) {
            a.addLast(3);
            i++;
        }
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int[] N = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int j : N) {
            Ns.addLast(j);
            opCounts.addLast(j);
            Stopwatch sw = new Stopwatch();
            countAddLast(j);
            double timeInSecond = sw.elapsedTime();
            times.addLast(timeInSecond);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
