import edu.princeton.cs.algs4.*;

public class ReverseListClient {

    private static final int LIST_INTEGER_RANGE = 100;

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Enter integer list size to reverse.");
        StdOut.println("- The list will reverse both recursively and iteratively.");

        /* linked list */
        LinkedList<Integer> list = new LinkedList<>();

        /* gets user input of list size and also generates list of random integers from 0 to given number */
        StdOut.print("\nEnter list size to generate: ");
        int size = StdIn.readInt();
        for (int i = 0; i < size; i++)
            list.add(StdRandom.uniform(LIST_INTEGER_RANGE));

        /* prints original list */
        StdOut.println("\nOriginal:\t" + list.toString());

        /* prints reversed list generated through recursion */
        StdOut.print("\nRecursively: ");
        Stopwatch recursiveStopwatch = new Stopwatch();
        list.recursivelyReverse();
        StdOut.println(recursiveStopwatch.elapsedTime() + " s.");
        StdOut.println("\nList:\t\t" + list.toString());

        /* prints reversed list generated through iteration */
        StdOut.print("\nIteratively: ");
        Stopwatch iterativeStopwatch = new Stopwatch();
        list.iterativelyReverse();
        StdOut.println(iterativeStopwatch.elapsedTime() + " s.");
        StdOut.println("\nList:\t\t" + list.toString());
    }
}
