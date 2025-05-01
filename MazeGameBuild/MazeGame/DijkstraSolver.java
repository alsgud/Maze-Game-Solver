import java.awt.*;
import java.util.*;
import java.util.List;

public class DijkstraSolver implements MazeSolverStrategy {
    private boolean[][] visited;
    private MazeStepListener stepListener;
    private int rows, cols;
    private int moveCounter;

    public DijkstraSolver(MazeStepListener listener) {
        this.stepListener = listener;
    }

    @Override
    public List<Point> solve(int[][] maze, Point start, Point end) {
        rows = maze.length;
        cols = maze[0].length;
        visited = new boolean[rows][cols];
        moveCounter = 0; // Reset the move counter for each run

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.g));
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
        openList.add(new Node(start, 0));

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            // Increment moveCounter after each node is processed
            moveCounter++;

            // Notify the GUI for each explored point
            if (stepListener != null) {
                stepListener.onStep(current.point.x, current.point.y, moveCounter);
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }

            // If we reached the end, reconstruct the path
            if (current.point.equals(end)) {
                return reconstructPath(cameFrom, current.point);
            }

            for (int i = 0; i < 4; i++) {
                Point next = new Point(current.point.x + dx[i], current.point.y + dy[i]);
                if (isValidMove(maze, next)) {
                    int tentativeGScore = gScore.getOrDefault(current.point, Integer.MAX_VALUE) + 1;
                    if (tentativeGScore < gScore.getOrDefault(next, Integer.MAX_VALUE)) {
                        cameFrom.put(next, current.point);
                        gScore.put(next, tentativeGScore);
                        openList.add(new Node(next, tentativeGScore));
                    }
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
        return point.x >= 0 && point.y >= 0 && point.x < maze.length && point.y < maze[0].length && maze[point.x][point.y] != 1;
    }

    static class Node {
        Point point;
        int g;

        Node(Point point, int g) {
            this.point = point;
            this.g = g;
        }
    }
}
