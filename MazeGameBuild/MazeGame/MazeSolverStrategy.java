import java.awt.Point;
import java.util.List;

public interface MazeSolverStrategy {
    List<Point> solve(int[][] maze, Point start, Point end);
}
