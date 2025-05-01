import java.awt.*;
import java.util.*;
import java.util.List;

public class BfsSolver implements MazeSolverStrategy {
    private boolean[][] visited;
    private MazeStepListener stepListener;
    private int rows, cols;
    private int moveCounter;

    public BfsSolver(MazeStepListener listener) {
        this.stepListener = listener;
    }

    @Override
    public List<Point> solve(int[][] maze, Point start, Point end) {
        rows = maze.length;
        cols = maze[0].length;
        visited = new boolean[rows][cols];
        moveCounter = 0;
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        List<Point> path = new ArrayList<>();

        queue.add(start);
        visited[start.x][start.y] = true;
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            // Notify GUI for every step (both wasted and valid)
            if (stepListener != null) {
                stepListener.onStep(current.x, current.y, moveCounter);
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }

            // If we reached the end, reconstruct the path
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            // Explore neighbors
            for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                Point next = new Point(current.x + dir[0], current.y + dir[1]);
                if (isValidMove(maze, next) && !visited[next.x][next.y]) {
                    queue.add(next);
                    visited[next.x][next.y] = true;
                    cameFrom.put(next, current);
                    moveCounter++;  // Increment move count
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private boolean isValidMove(int[][] maze, Point point) {
        return point.x >= 0 && point.x < rows && point.y >= 0 && point.y < cols && maze[point.x][point.y] != 1;
    }
}
