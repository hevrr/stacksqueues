import edu.princeton.cs.algs4.*;

public class ReversePolishNotation {

    /* mathematical operations */
    private enum Operations {
        addition, subtraction, division, multiplication
    }

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Evaluates Reverse Polish Notation function");
        StdOut.println("- Test case (ans 42): 395+*= (equivalent to 3*(9+5)");

        /* get expression to evaluate */
        StdOut.println("\nEnter expression term by term in RPM and '=' to evaluate: ");

        /* stack of operands */
        Stack<Double> operands = new Stack<>();

        String c;

        /* goes through expression */
        do {
            /* character at index */
            c = StdIn.readString();

            /* evaluates past two operands with given operation */
            if (c.equals("+") || c.equals("-") || c.equals("/") || c.equals("*"))
                operands.push(evaluate(operands.pop(), operands.pop(), convertToOperation(c)));

                /* push if operand */
            else if (!c.equals("="))
                operands.push(Double.parseDouble(c));
        } while (!c.equals("="));

        /* final evaluation */
        StdOut.println("Final evaluation = " + operands.toString());
    }

    /* converts to an operation */
    private static Operations convertToOperation(String c) {
        switch (c) {
            case "+":
                return Operations.addition;
            case "-":
                return Operations.subtraction;
            case "/":
                return Operations.division;
            default:
                return Operations.multiplication;
        }
    }

    /* evaluate numbers with given operation */
    private static double evaluate(double n2, double n1, Operations operator) {
        switch (operator) {
            case addition:
                return n1 + n2;
            case subtraction:
                return n1 - n2;
            case division:
                return n1 / n2;
            case multiplication:
                return n1 * n2;
            default:
                return 0;
        }
    }
}
