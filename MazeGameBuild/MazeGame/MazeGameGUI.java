import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MazeGameGUI extends JFrame {
    private static final int[][] originalMaze = {
            {0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0},
            {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1},
            {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0}
    };

    private boolean animationInProgress;

    private int[][] maze;
    private final int SIZE = 20;
    private int playerX = 0, playerY = 0;
    private int moveCount = 0;
    private final JButton[][] buttons = new JButton[SIZE][SIZE];
    private final JLabel moveLabel = new JLabel("Moves: 0");
    private SwingWorker<Void, Void> currentSolverTask;


    public MazeGameGUI() {
        setTitle("Maze Game with Solvers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font font = new Font("Monospaced", Font.BOLD, 16);
        maze = copyMaze(originalMaze);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton cell = new JButton();
                cell.setFocusable(false);
                cell.setFont(font);
                buttons[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        updateDisplay();
        add(gridPanel, BorderLayout.CENTER);
        add(moveLabel, BorderLayout.SOUTH);

        // Movement listener for player movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int nextX = playerX;
                int nextY = playerY;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> nextX--;
                    case KeyEvent.VK_DOWN -> nextX++;
                    case KeyEvent.VK_LEFT -> nextY--;
                    case KeyEvent.VK_RIGHT -> nextY++;
                    default -> { return; }
                }

                if (isValidMove(nextX, nextY)) {
                    maze[playerX][playerY] = 2;
                    playerX = nextX;
                    playerY = nextY;
                    moveCount++;
                    updateDisplay();

                    if (playerX == SIZE - 1 && playerY == SIZE - 1) {
                        JOptionPane.showMessageDialog(MazeGameGUI.this,
                                "🎉 You reached the goal in " + moveCount + " moves!",
                                "Victory", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        // Add solver buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // DFS Button
        JButton dfsButton = new JButton("Solve with DFS");
        // Inside your button listener
        dfsButton.addActionListener(e -> {
            // Prevent other animations from running
            if (animationInProgress) {
                return;  // Do nothing if an animation is already running
            }

            // Mark animation as in progress
            animationInProgress = true;

            // Reset the game state for a fresh start
            moveCount = 0;
            maze = copyMaze(originalMaze);
            playerX = 0;
            playerY = 0;
            updateDisplay();

            // Run the DFS solver in a background thread
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    DfsSolver solver = new DfsSolver((x, y, moves) -> {
                        // Update the player's position during the animation
                        maze[x][y] = 2;
                        moveCount = moves;
                        playerX = x;
                        playerY = y;

                        // Ensure the UI update is on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> updateDisplay());
                    });

                    // Perform DFS solving (passing the copy of the original maze)
                    solver.solve(copyMaze(originalMaze), new Point(0, 0), new Point(SIZE - 1, SIZE - 1));
                    return null;
                }

                @Override
                protected void done() {
                    // Mark animation as finished
                    animationInProgress = false;
                }
            }.execute();
        });


        // BFS Button
        JButton bfsButton = new JButton("Solve with BFS");
        bfsButton.addActionListener(e -> {
            if (animationInProgress) {
                return;  // Prevent starting another animation
            }

            animationInProgress = true;
            moveCount = 0;
            maze = copyMaze(originalMaze);
            playerX = 0;
            playerY = 0;
            updateDisplay();

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    // Create BFS solver and pass the listener
                    BfsSolver solver = new BfsSolver((x, y, moves) -> {
                        // Update the player's position during the animation
                        maze[x][y] = 2;  // Mark the current step as part of the path
                        moveCount = moves;
                        playerX = x;
                        playerY = y;

                        // Ensure the UI update is on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> updateDisplay());
                    });

                    // Perform BFS solving (passing the copy of the original maze)
                    solver.solve(copyMaze(originalMaze), new Point(0, 0), new Point(SIZE - 1, SIZE - 1));
                    return null;
                }

                @Override
                protected void done() {
                    // Mark animation as finished
                    animationInProgress = false;
                }
            }.execute();
        });


        // A* Button
        JButton aStarButton = new JButton("Solve with A*");
        aStarButton.addActionListener(e -> {
            if (animationInProgress) {
                return;  // Prevent starting another animation
            }

            animationInProgress = true;
            moveCount = 0;
            maze = copyMaze(originalMaze);
            playerX = 0;
            playerY = 0;
            updateDisplay();

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    // Create A* solver and pass the listener
                    AStarSolver solver = new AStarSolver((x, y, moves) -> {
                        // Update the player's position during the animation
                        maze[x][y] = 2;  // Mark the current step as part of the path
                        moveCount = moves;
                        playerX = x;
                        playerY = y;

                        // Ensure the UI update is on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> updateDisplay());
                    });

                    // Perform A* solving (passing the copy of the original maze)
                    solver.solve(copyMaze(originalMaze), new Point(0, 0), new Point(SIZE - 1, SIZE - 1));
                    return null;
                }

                @Override
                protected void done() {
                    // Mark animation as finished
                    animationInProgress = false;
                }
            }.execute();
        });


        // Dijkstra Button
        // Dijkstra Button
        JButton dijkstraButton = new JButton("Solve with Dijkstra");
        dijkstraButton.addActionListener(e -> {
            if (animationInProgress) {
                return;  // Prevent starting another animation
            }

            animationInProgress = true;
            moveCount = 0;
            maze = copyMaze(originalMaze);
            playerX = 0;
            playerY = 0;
            updateDisplay();

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    // Create Dijkstra solver and pass the listener
                    DijkstraSolver solver = new DijkstraSolver((x, y, moves) -> {
                        // Update the player's position during the animation
                        maze[x][y] = 2;  // Mark the current step as part of the path
                        moveCount = moves;
                        playerX = x;
                        playerY = y;

                        // Ensure the UI update is on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> updateDisplay());
                    });

                    // Perform Dijkstra solving (passing the copy of the original maze)
                    solver.solve(copyMaze(originalMaze), new Point(0, 0), new Point(SIZE - 1, SIZE - 1));
                    return null;
                }

                @Override
                protected void done() {
                    // Mark animation as finished
                    animationInProgress = false;
                }
            }.execute();
        });

        // Add all buttons to the panel
        buttonPanel.add(dfsButton);
        buttonPanel.add(bfsButton);
        buttonPanel.add(aStarButton);
        buttonPanel.add(dijkstraButton);
        add(buttonPanel, BorderLayout.NORTH);

        setSize(550, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // This method handles running the selected solver algorithm
    private void solveMaze(MazeSolverStrategy strategy) {
        // Cancel the previous task if it's running
        if (currentSolverTask != null && !currentSolverTask.isDone()) {
            currentSolverTask.cancel(true);
        }

        System.out.println("Running solver: " + strategy.getClass().getSimpleName());
        currentSolverTask = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                List<Point> path = strategy.solve(copyMaze(originalMaze), new Point(0, 0), new Point(SIZE - 1, SIZE - 1));
                if (path != null) {
                    for (Point p : path) {
                        if ((p.x != 0 || p.y != 0) && (p.x != SIZE - 1 || p.y != SIZE - 1)) {
                            maze[p.x][p.y] = 2;
                        }
                    }
                    SwingUtilities.invokeLater(MazeGameGUI.this::updateDisplay);
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MazeGameGUI.this, "No path found!"));
                }
                return null;
            }
        };
        currentSolverTask.execute();
    }


    private int[][] copyMaze(int[][] src) {
        int[][] copy = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, copy[i], 0, src[0].length);
        }
        return copy;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && maze[x][y] != 1;
    }

    private void updateDisplay() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton btn = buttons[i][j];
                if (i == playerX && j == playerY) {
                    btn.setBackground(Color.GREEN);
                } else if (maze[i][j] == 1) {
                    btn.setBackground(Color.DARK_GRAY);
                    btn.setForeground(Color.WHITE);
                } else if (maze[i][j] == 2) {
                    btn.setBackground(Color.LIGHT_GRAY);
                } else {
                    btn.setBackground(Color.WHITE);
                }

                if (i == SIZE - 1 && j == SIZE - 1) {
                    btn.setBackground(Color.BLUE); // Optional: highlight end point
                } else {
                    btn.setText(""); // Clear text for regular cells
                }
            }
        }
        moveLabel.setText("Moves: " + moveCount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGameGUI::new);
    }
}
