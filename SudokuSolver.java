import acm.util.ErrorException;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class SudokuSolver {

    /* variables */
    private static final int APPLICATION_SIZE = 500;
    private static final double squareLength = 0.5 / 9;

    private static int[][] sudoku = new int[9][9];
    private static int[][] initialSudoku = new int[9][9];
    private static Location lastSpaceToFill;

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Can solve a sudoku puzzle through stack\n");

        /* graphics canvas */
        StdDraw.setCanvasSize(APPLICATION_SIZE, APPLICATION_SIZE);

        /* load sudoku and create stack */
        loadRandomlySelectedSudoku();
        Stack<Location> stack = new Stack<>();

        /* initial sudoku */
        StdOut.println("- Initial Sudoku -");
        printSudoku();
        drawGrid();
        drawNumbers();

        /* tracks current start */
        int currentStart = 0;

        /* tracks moves */
        int moveCounter = 0;

        /* keep going all spaces filled */
        while (sudoku[lastSpaceToFill.x][lastSpaceToFill.y] == 0) {
            Location next = nextLocation(currentStart);
            moveCounter++;

            if (next.num != -1) {
                updateSudoku(next);
                stack.push(next);
                currentStart = 0;
            } else {
                next = stack.pop();
                sudoku[next.x][next.y] = 0;
                currentStart = next.num;
            }
        }

        /* shows final solved sudoku */
        StdOut.println("- Solved Sudoku: " + moveCounter + " moves -");
        printSudoku();
        drawGrid();
        drawNumbers();
    }

    /* updates sudoku */
    private static void updateSudoku(Location loc) {
        sudoku[loc.x][loc.y] = loc.num;
    }

    /* finds next location */
    private static Location nextLocation(int min) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0) {
                    if (canMove(i, j, min) != -1)
                        return new Location(i, j, canMove(i, j, min));
                    return new Location(-1, -1, -1);
                }
            }
        return new Location(-1, -1, -1);
    }

    /* checks whether it is a possible move */
    private static int canMove(int i, int j, int min) {
        for (int q = min + 1; q <= 9; q++)
            if (checkCol(i, q) && checkRow(j, q) && checkBox((i / 3) * 3, (j / 3) * 3, q))
                return q;
        return -1;
    }

    /* checks if column placement is valid */
    private static boolean checkCol(int c, int num) {
        for (int i = 0; i < 9; i++)
            if (sudoku[c][i] == num)
                return false;
        return true;
    }

    /* checks if row placement is valid */
    private static boolean checkRow(int r, int num) {
        for (int i = 0; i < 9; i++)
            if (sudoku[i][r] == num)
                return false;
        return true;
    }

    /* checks if box placement is valid */
    private static boolean checkBox(int r, int c, int num) {
        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (sudoku[i][j] == num)
                    return false;
        return true;
    }

    /* draws sudoku numbers */
    private static void drawNumbers() {
        Font font = new Font("Helvetica", Font.PLAIN, 20);
        StdDraw.setFont(font);
        for (int i = 0; i < sudoku.length; i++)
            for (int j = 0; j < sudoku.length; j++) {
                if (initialSudoku[i][j] != 0)
                    font = new Font("Helvetica", Font.BOLD, 20);
                else
                    font = new Font("Helvetica", Font.PLAIN, 20);
                StdDraw.setFont(font);
                StdDraw.text(2 * i * squareLength + squareLength, 2 * (8 - j) * squareLength + squareLength, Integer.toString(sudoku[i][j]));
            }
    }

    /* draws sudoku grid */
    private static void drawGrid() {
        for (int i = 0; i < sudoku.length; i++)
            for (int j = 0; j < sudoku.length; j++) {
                if (initialSudoku[i][j] == 0)
                    StdDraw.setPenColor(Color.WHITE);
                else
                    StdDraw.setPenColor(Color.LIGHT_GRAY);
                StdDraw.filledSquare(2 * i * squareLength + squareLength, 2 * (8 - j) * squareLength + squareLength, squareLength);
            }

        StdDraw.setPenColor(Color.BLACK);
        for (int i = 0; i < initialSudoku.length; i++) {
            if (i % 3 == 0 && i != 0)
                StdDraw.setPenRadius(5 / (double) APPLICATION_SIZE);
            else
                StdDraw.setPenRadius(1 / (double) APPLICATION_SIZE);
            StdDraw.line(squareLength * 2 * i, 0, squareLength * 2 * i, 1);
            StdDraw.line(0, squareLength * 2 * i, 1, squareLength * 2 * i);
        }
    }

    /* prints sudoku */
    private static void printSudoku() {
        for (int i = 0; i < sudoku.length; i++) {
            for (int[] ints : sudoku) StdOut.print(ints[i] + " ");
            StdOut.println();
        }
        StdOut.println();
    }

    /* loads the text */
    private static void loadRandomlySelectedSudoku() {
        int counter = 0;
        try {
            Scanner scanner = new Scanner(new FileReader("src/Sudoku.txt"));
            while (scanner.hasNext()) {
                scanner.next();
                counter++;
            }
            scanner.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
        Random random = new Random();
        int r = random.nextInt(counter) / 9 * 9;
        int rowCounter = 0;
        try {
            Scanner sc = new Scanner(new FileReader("src/Sudoku.txt"));
            for (int i = 0; i < r; i++) {
                sc.next();
            }
            while (sc.hasNext() && rowCounter < 9) {
                String line = sc.next();
                for (int i = 0; i < line.length(); i++)
                    if (line.charAt(i) != ' ') {
                        sudoku[i][rowCounter] = Character.getNumericValue(line.charAt(i));
                        initialSudoku[i][rowCounter] = sudoku[i][rowCounter];
                    }
                rowCounter++;
            }
            for (int i = 0; i < sudoku.length; i++)
                for (int j = 0; j < sudoku.length; j++)
                    if (sudoku[i][j] == 0)
                        lastSpaceToFill = new Location(i, j, 0);
            sc.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
    }

    /* location class */
    private static class Location {
        int x;
        int y;
        int num;

        Location(int x, int y, int num) {
            this.x = x;
            this.y = y;
            this.num = num;
        }
    }
}
