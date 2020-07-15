import edu.princeton.cs.algs4.*;

class Djikstras {

    /* mathematical operations */
    private enum Operations {
        addition, subtraction, division, multiplication, leftParentheses
    }

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Evaluates expression either completely or incompletely parenthetical");
        StdOut.println("- Test case for completely parenthetical (ans 101): (1+((2+3)*(4*5)))=");
        StdOut.println("- Test case for incompletely parenthetical (ans 101): 1+(2+3)*4*5=");

        /* which method to use */
        StdOut.print("\n- True for completely parenthetical method\n- False for the incompletely parenthetical method\n\nMethod (true/false): ");
        boolean completelyParanthetical = StdIn.readBoolean();

        /* get expression to evaluate */
        StdOut.println("\nEnter expression term by term and '=' to evaluate:");

        /* prints final evaluation */
        if (completelyParanthetical)
            StdOut.println("\nFinal evaluation = " + completelyParentheticalEvaluation());
        else
            StdOut.println("\nFinal evaluation = " + incompletelyParentheticalEvaluation());
    }

    /* completely parenthetical method */
    private static double completelyParentheticalEvaluation() {

        /* stacks of operands and operators */
        Stack<Double> operands = new Stack<>();
        Stack<Operations> operators = new Stack<>();

        String c;

        /* goes through entire expression */
        do {
            /* character at index */
            c = StdIn.readString();

            /* push if an operator */
            if (c.equals("+") || c.equals("-") || c.equals("/") || c.equals("*"))
                operators.push(convertToOperation(c));

                /* computes last two operands if ending parentheses */
            else if (c.equals(")")) {
                Operations operator = operators.pop();
                double n2 = operands.pop();
                double n1 = operands.pop();
                operands.push(evaluate(n1, n2, operator));

                /* push if an operand */
            } else if (!c.equals(")") && !c.equals("="))
                operands.push(Double.parseDouble(c));

        } while(!c.equals("="));

        return operands.peek();
    }

    /* incompletely parenthetical method */
    private static double incompletelyParentheticalEvaluation() {

        /* stacks of operands and operators */
        Stack<Double> operands = new Stack<>();
        Stack<Operations> operators = new Stack<>();

        String c;

        /* goes through entire expression */
        do {
            /* character at index */
            c = StdIn.readString();

            /* tracks left parentheses for order of operations */
            if (c.equals("("))
                operators.push(Operations.leftParentheses);

                /* keeps performing operations until left parentheses */
            else if (c.equals(")")) {
                while (operators.peek() != Operations.leftParentheses)
                    operands.push(evaluate(operands.pop(), operands.pop(), operators.pop()));
                operators.pop();

                /* performs operations as long as it is higher order */
            } else if (c.equals("+") || c.equals("-") || c.equals("/") || c.equals("*")) {
                Operations op = convertToOperation(c);

                while (!operators.isEmpty() && higherOrder(operators.peek(), op))
                    operands.push(evaluate(operands.pop(), operands.pop(), operators.pop()));

                operators.push(op);

                /* push if an operand */
            } else if (!c.equals("="))
                operands.push(Double.parseDouble(c));

        } while (!c.equals("="));

        /* performs rest of operations */
        while (operands.size() != 1)
            operands.push(evaluate(operands.pop(), operands.pop(), operators.pop()));

        return operands.peek();
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

    /* checks if higher order */
    private static boolean higherOrder(Operations operator1, Operations operator2) {
        return ((operator1 == Operations.multiplication || operator1 == Operations.division) && (operator2 == Operations.addition || operator2 == Operations.subtraction));
    }
}
