import java.awt.Point;
import java.util.*;

public class DfsSolver implements MazeSolverStrategy {
    private boolean[][] visited;
    private MazeStepListener stepListener;
    private int rows, cols;
    private int moveCounter;

    public DfsSolver(MazeStepListener listener) {
        this.stepListener = listener;
    }

    @Override
    public List<Point> solve(int[][] maze, Point start, Point end) {
        rows = maze.length;
        cols = maze[0].length;
        visited = new boolean[rows][cols];
        moveCounter = 0;
        List<Point> path = new ArrayList<>();

        dfs(maze, start.x, start.y, end, path);
        return path;
    }

    private boolean dfs(int[][] maze, int x, int y, Point end, List<Point> path) {
        if (!isValidMove(maze, x, y) || visited[x][y]) return false;

        visited[x][y] = true;
        path.add(new Point(x, y));
        moveCounter++;

        // Notify GUI (slow down here for animation)
        if (stepListener != null) {
            stepListener.onStep(x, y, moveCounter);
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }

        if (x == end.x && y == end.y) return true;

        // Randomized directions
        List<int[]> directions = new ArrayList<>(Arrays.asList(
                new int[]{-1, 0},  // Up
                new int[]{1, 0},   // Down
                new int[]{0, -1},  // Left
                new int[]{0, 1}    // Right
        ));

        // Shuffle directions to randomize the path
        Collections.shuffle(directions);

        // Try each direction in random order
        for (int[] d : directions) {
            if (dfs(maze, x + d[0], y + d[1], end, path)) return true;
        }

        path.remove(path.size() - 1); // backtrack
        return false;
    }

    private boolean isValidMove(int[][] maze, int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols && maze[x][y] != 1;
    }
}
