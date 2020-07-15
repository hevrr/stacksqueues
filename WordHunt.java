import acm.util.ErrorException;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class WordHunt {

    /* variables */
    private static LinkedList<String> dictionary = new LinkedList<>();
    private static LinkedList<Location> founds = new LinkedList<>();
    private static String[][] wordhunt;

    private static final int APPLICATION_SIZE = 800;
    private static double squareLength = 0.5;

    /* directions */
    private enum Direction {
        left, right, up, down, leftup, rightup, leftdown, rightdown
    }

    public static void main(String[] args) {

        /* instructions */
        StdOut.println("- Finds all words in word hunt puzzle using queue and sorts");
        StdOut.println("- Interesting implementation to remove words that don't exist and eliminate extra searches: see diagram\n");

        StdDraw.setCanvasSize(APPLICATION_SIZE, APPLICATION_SIZE);
        loadDictionary();
        loadWordhunt();
        Queue<Location> queue = new Queue<>();

        System.out.println("OK");

        /* set up all starting positions with directions */
        for (int i = 0; i < wordhunt[0].length; i++)
            for (int j = 0; j < wordhunt.length; j++)
                for (Direction d : Direction.values())
                    queue.enqueue(new Location(i, j, wordhunt[i][j], d));

        /* keeps finding words */
        while (!queue.isEmpty()) {
            Location loc = queue.dequeue();
            Location move = movement(loc.d);
            if (isWord(loc.word, 0, dictionary.size() - 1) != -1)
                founds.add(loc);
            else if (inBounds(loc.x + move.x, loc.y + move.y) && containsString(loc.word + wordhunt[loc.x + move.x][loc.y + move.y], 0, dictionary.size() - 1) != -1) {
                Location newLoc = new Location(loc.x + move.x, loc.y + move.y, loc.word + wordhunt[loc.x + move.x][loc.y + move.y], loc.d);
                queue.enqueue(newLoc);
            }
        }

        // prints found words
        LinkedList<String> wordsFound = new LinkedList<>();
        for (int i = 0; i < founds.size(); i++)
            wordsFound.add(founds.get(i).word);
        sort(wordsFound);
        StdOut.println(dictionary.size() + " words to find:\t" + dictionary.toString());
        StdOut.println(wordsFound.size() + " words found:\t\t" + wordsFound.toString());
        StdOut.println("\nHappy early Halloween!");

        // shows puzzle
        drawWordhunt();
    }

    /* binary search to see if has word */
    private static int containsString(String letters, int l, int r) {
        if (r >= l) {
            int mid = l + (r - l) / 2;
            if (dictionary.get(mid).length() >= letters.length() && dictionary.get(mid).substring(0, letters.length()).equals(letters))
                return mid;
            if (dictionary.get(mid).compareTo(letters) > 0)
                return containsString(letters, l, mid - 1);
            return containsString(letters, mid + 1, r);
        }
        return -1;
    }

    /* binary search for word */
    private static int isWord(String letters, int l, int r) {
        if (r >= l) {
            int mid = l + (r - l) / 2;
            if (dictionary.get(mid).equals(letters))
                return mid;
            if (dictionary.get(mid).compareTo(letters) > 0)
                return isWord(letters, l, mid - 1);
            return isWord(letters, mid + 1, r);
        }
        return -1;
    }

    /* in bounds of wordhunt puzzle */
    private static boolean inBounds(int x, int y) {
        return x >= 0 && x < wordhunt[0].length && y >= 0 && y < wordhunt.length;
    }

    /* returns location with given direction */
    private static Location movement(Direction d) {
        switch (d) {
            case left:
                return new Location(-1, 0);
            case right:
                return new Location(1, 0);
            case up:
                return new Location(0, 1);
            case down:
                return new Location(0, -1);
            case leftup:
                return new Location(-1, 1);
            case rightup:
                return new Location(1, 1);
            case leftdown:
                return new Location(-1, -1);
            case rightdown:
                return new Location(1, -1);
            default:
                return new Location(0, 0);
        }
    }

    /* sorts list */
    private static void sort(LinkedList<String> list) {
        for (int i = 1; i < list.size(); i++) {
            int k = i - 1;
            while (k >= 0 && list.get(k + 1).compareTo(list.get(k)) < 0) {
                String change = list.get(k);
                list.set(k, list.get(k + 1));
                list.set(k + 1, change);
                k--;
            }
        }
    }

    /* loads the wordhunt from text file */
    private static void loadWordhunt() {
        int width = 0;
        int height = 0;
        try {
            Scanner scanner = new Scanner(new FileReader("src/Wordhunt.txt"));
            while (scanner.hasNext()) {
                String line = scanner.next();
                width = line.length();
                height++;
            }
            scanner.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
        wordhunt = new String[width][height];
        int rowCounter = 0;
        try {
            Scanner scanner = new Scanner(new FileReader("src/Wordhunt.txt"));
            while (scanner.hasNext()) {
                String line = scanner.next();
                for (int i = 0; i < line.length(); i++)
                    wordhunt[i][rowCounter] = Character.toString(line.charAt(i));
                rowCounter++;
            }
            squareLength /= rowCounter;
            scanner.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
    }

    /* loads dictionary */
    private static void loadDictionary() {
        try {
            Scanner scanner = new Scanner(new FileReader("src/Dictionary.txt"));
            while (scanner.hasNext()) {
                String l = scanner.next();
                dictionary.add(l);
            }
            sort(dictionary);
            scanner.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
    }

    /* draws word hunt */
    private static void drawWordhunt() {
        Font font = new Font("Helvetica", Font.PLAIN, 15);
        StdDraw.setFont(font);
        for (int i = 0; i < wordhunt.length; i++)
            for (int j = 0; j < wordhunt.length; j++) {
                StdDraw.setFont(font);
                StdDraw.text(2 * i * squareLength + squareLength, 2 * (wordhunt.length - 1 - j) * squareLength + squareLength, wordhunt[i][j]);
            }
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        for (int i = 0; i < founds.size(); i++) {
            Location l = founds.get(i);
            Location m = movement(l.d);
            StdDraw.line(2 * l.x * squareLength + squareLength, 2 * (wordhunt.length - 1 - l.y) * squareLength + squareLength, 2 * (l.x - m.x * (l.word.length() - 1)) * squareLength + squareLength, 2 * (wordhunt.length - 1 - l.y + m.y * (l.word.length() - 1)) * squareLength + squareLength);
        }
    }

    /* location class */
    private static class Location {
        int x;
        int y;
        String word;
        Direction d;

        Location(int x, int y, String word, Direction d) {
            this.x = x;
            this.y = y;
            this.word = word;
            this.d = d;
        }

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
