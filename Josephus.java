import edu.princeton.cs.algs4.*;

public class Josephus {

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Solves the suicide circle problem through queues and recursion and reports solving time. Where must Josephus sit to survive?");
        StdOut.println("- Note that number of people in circle and number of people skipped must be greater than zero.");
        StdOut.println("- An input of 1 will be used if an illegal input is found.");
        StdOut.println("- Enter a value number of people in circle >= 1000 for a noticeable solving time.");

        /* user inputs */
        StdOut.print("\nEnter number of people in circle: ");
        int numberPeople = Math.max(1, StdIn.readInt());
        StdOut.print("Enter number of people skipped: ");
        int numberSkipped = Math.max(1, StdIn.readInt());

        /* prints for queue */
        StdOut.println("\nQueue indices of people killed: ");
        Stopwatch queueStopwatch = new Stopwatch();
        int queueSolution = queueJosephus(numberPeople, numberSkipped);
        StdOut.println("\n\nQueue solving time: " + queueStopwatch.elapsedTime() + " s");
        StdOut.println("To survive elimination, Josephus must sit at index: " + queueSolution);

        /* prints for recursion */
        Stopwatch recursiveStopwatch = new Stopwatch();
        int recursiveSolution = recursiveJosephus(numberPeople, numberSkipped);
        StdOut.println("\nRecursive solving time: " + recursiveStopwatch.elapsedTime() + " s");
        StdOut.println("To survive elimination, Josephus must sit at index: " + recursiveSolution);
    }

    /* queue for Josephus problem */
    private static int queueJosephus(int numberPeople, int numberSkipped) {

        /* enqueues all people */
        Queue<Integer> people = new Queue<>();
        for (int i = 1; i < numberPeople + 1; i++)
            people.enqueue(i);

        /* keeps going until one survivor */
        while (people.size() > 1) {

            /* remove and add people traversed and not killed */
            for (int i = 0; i < numberSkipped - 1; i++)
                people.enqueue(people.dequeue());

            /* kill this person */
            StdOut.print(people.dequeue() + " ");
        }

        /* last person alive */
        return people.dequeue();
    }

    /* recursion for Josephus problem */
    private static int recursiveJosephus(int numberPeople, int numberSkipped) {
        /* base case: when one person there, they must be alive */
        if (numberPeople == 1)
            return 1;

        /* step case: take survivor from n-1 and add onto people skipped and mod for index of current survivor */
        else
            return (recursiveJosephus(numberPeople - 1, numberSkipped) + numberSkipped - 1) % numberPeople + 1;
    }
}
