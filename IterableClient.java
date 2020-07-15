import edu.princeton.cs.algs4.*;

public class IterableClient {

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Testing client for iterable linked structures");

        /* variables */
        LinkedList<Integer> list = new LinkedList<>();
        Stack<Integer> stack = new Stack<>();
        Queue<Integer> queue = new Queue<>();

        /* gets size input */
        StdOut.print("\nEnter size of inputs: ");
        int enterSize = StdIn.readInt();
        StdOut.println();

        /* keeps taking numbers to put in linked structure */
        for (int i = 1; i < enterSize + 1; i++) {
            StdOut.print("Input " + i + ": ");
            int input = StdIn.readInt();
            list.add(input);
            stack.push(input);
            queue.enqueue(input);
        }
        StdOut.println("Inputs complete.");

        /* prints out LinkedList through for each */
        StdOut.println("\nIterable LinkedList: ");
        for(int integer : list)
            StdOut.print(integer + " ");

        /* prints out stack through for each */
        StdOut.println("\n\nIterable Stack: ");
        for(int integer : stack)
            StdOut.print(integer + " ");

        /* prints out queue through for each */
        StdOut.println("\n\nIterable Queue: ");
        for(int integer : queue)
            StdOut.print(integer + " ");

        StdOut.println("\n\nHave a great day!");
    }
}
