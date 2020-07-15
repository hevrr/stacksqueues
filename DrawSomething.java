import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.event.*;

public class DrawSomething extends JPanel implements Runnable {

    /* variables */
    private static JFrame f;
    private Thread gameThread;

    private final int APPLICATION_SIZE = 500;

    private Location prev = new Location(0, 0);

    private Color[][] colorCanvas = new Color[APPLICATION_SIZE][APPLICATION_SIZE];
    private static Color currentColor = Color.BLACK;

    private static boolean findArea = false;

    public static void main(String[] args) {

        /* instructions */
        System.out.println("- Can draw outlines and fill enclosed area");

        System.out.println("\nControls:");
        System.out.println("[Hold mouse]: draw");
        System.out.println("[F + mouse click]: flood fill area under mouse location and find area");
        System.out.println("[space]: reset canvas");
        System.out.println("[cmd R]: set paint bucket as red");
        System.out.println("[cmd G]: set paint bucket as green");
        System.out.println("[cmd B]: set paint bucket as blue");
        System.out.println("[cmd X]: set paint bucket as white");
        System.out.println("[cmd O]: set paint bucket as black\n");

        SwingUtilities.invokeLater(() -> {
            f = new JFrame();
            f.setTitle("DrawSomethingQ2");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new DrawSomething(), BorderLayout.CENTER);
            f.setJMenuBar(createMenuBar());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    @Override
    public void run() {
        newCanvas();
    }

    /* creates menu bar shortcuts for colors */
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Select Color");
        menuBar.add(menu);

        JMenuItem blue = new JMenuItem(" Blue");
        JMenuItem red = new JMenuItem(" Red");
        JMenuItem green = new JMenuItem(" Green");
        JMenuItem white = new JMenuItem(" White");
        JMenuItem black = new JMenuItem(" Black");


        blue.addActionListener(e -> currentColor = new Color(0, 128, 255));
        blue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        red.addActionListener(e -> currentColor = new Color(204, 0, 0));
        red.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        green.addActionListener(e -> currentColor = new Color(0, 153, 0));
        green.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        white.addActionListener(e -> currentColor = new Color(255, 255, 255));
        white.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        black.addActionListener(e -> currentColor = new Color(0, 0, 0));
        black.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        menu.add(red);
        menu.add(green);
        menu.add(blue);
        menu.add(white);
        menu.add(black);

        return menuBar;
    }

    /* background, thread, listeners */
    private DrawSomething() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(APPLICATION_SIZE, APPLICATION_SIZE));
        setFocusable(true);

        (gameThread = new Thread(this)).start();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Location curr = new Location(e.getX(), e.getY());
                if (isWithinBounds(prev, curr))
                    linkPoints(prev, curr);
                prev.x = curr.x;
                prev.y = curr.y;
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prev.x = e.getX();
                prev.y = e.getY();
                if (findArea) {
                    if (isWithinBounds(e.getX(), e.getY()))
                        System.out.println("Area: " + floodFill(e.getX(), e.getY()) + " pixels");
                    repaint();
                }
            }
        });


        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F)
                    findArea = true;
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        startNewThread();
                        break;
                    case KeyEvent.VK_F:
                        findArea = false;
                        break;
                }
                repaint();
            }
        });
    }

    /* returns area and flood fills canvas */
    private int floodFill(int x, int y) {

        /* adds first point in queue */
        int areaCounter = 0;
        Queue<Location> queue = new Queue<>();
        Location start = new Location(x, y);
        queue.enqueue(start);

        /* finds replaced color */
        Color replaced = colorCanvas[x][y];

        /* tracks places already been */
        boolean[][] tracker = new boolean[APPLICATION_SIZE][APPLICATION_SIZE];

        /* keeps going until fully flooded */
        while (!queue.isEmpty()) {

            /* gets new point */
            Location loc = queue.dequeue();

            /* in the four directions */
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++) {
                    if (Math.abs(i) != Math.abs(j)) {

                        /* checks if location is within bounds and the same color as the color to be replaced and if it has already been filled */
                        if (isWithinBounds(loc.x + i, loc.y + j) && canFill(loc.x + i, loc.y + j, replaced) && !tracker[loc.x + i][loc.y + j]) {

                            /* fills the canvas and updates tracker*/
                            colorCanvas[loc.x + i][loc.y + j] = currentColor;
                            tracker[loc.x + i][loc.y + j] = true;

                            /* enqueues new point and updates area counter */
                            queue.enqueue(new Location(loc.x + i, loc.y + j));
                            areaCounter++;
                        }
                    }
                }
        }
        return areaCounter;
    }

    /* links two points drawn because certain movements in mouse are not updated instantaneously in JFrame, causing some points to not be connected when drawing */
    private void linkPoints(Location prev, Location curr) {
        colorCanvas[prev.x][prev.y] = currentColor;

        /* links them together like a line */
        while (prev.x != curr.x || prev.y != curr.y) {
            int xDiff = curr.x - prev.x;
            int yDiff = curr.y - prev.y;
            if (xDiff < 0 && yDiff < 0) {
                prev.x--;
                prev.y--;
            } else if (xDiff > 0 && yDiff > 0) {
                prev.x++;
                prev.y++;
            } else if (xDiff > 0 && yDiff < 0) {
                prev.x++;
                prev.y--;
            } else if (xDiff < 0 && yDiff > 0) {
                prev.x--;
                prev.y++;
            } else if (xDiff == 0 && yDiff > 0) {
                prev.y++;
            } else if (xDiff == 0 && yDiff < 0) {
                prev.y--;
            } else if (xDiff > 0) {
                prev.x++;
            } else if (xDiff < 0) {
                prev.x--;
            }
            colorCanvas[prev.x][prev.y] = currentColor;
        }
    }

    /* checks if within bounds of application */
    private boolean isWithinBounds(Location prev, Location curr) {
        return curr.x >= 0 && curr.y >= 0 && curr.x < APPLICATION_SIZE && curr.y < APPLICATION_SIZE && prev.x >= 0 && prev.y >= 0 && prev.x < APPLICATION_SIZE && prev.y < APPLICATION_SIZE;
    }

    /* checks if within bounds of application */
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < APPLICATION_SIZE && y < APPLICATION_SIZE;
    }

    /* if location is fillable */
    private boolean canFill(int x, int y, Color toBeFilled) {
        return colorCanvas[x][y].getRGB() == toBeFilled.getRGB();
    }

    /* creates a blank canvas */
    private void newCanvas() {
        for (int i = 0; i < colorCanvas.length; i++)
            for (int j = 0; j < colorCanvas.length; j++)
                colorCanvas[i][j] = Color.WHITE;
    }

    /* starts new thread */
    private void startNewThread() {
        (gameThread = new Thread(this)).start();
    }

    /* paints grid */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < colorCanvas.length; i++)
            for (int j = 0; j < colorCanvas.length; j++) {
                g.setColor(colorCanvas[i][j]);
                g.drawRect(i, j, 1, 1);
            }
    }

    /* location class */
    private static class Location {
        int x;
        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
