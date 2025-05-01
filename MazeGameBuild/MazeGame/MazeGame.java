import java.util.Scanner;

public class MazeGame {
    private static final int[][] maze = {
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 1},
            {0, 1, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 0, 1, 1, 0, 1, 1},
            {1, 0, 0, 1, 1, 1, 0, 0, 0, 0}
    };

    private static int playerX = 0;
    private static int playerY = 0;
    private static final int END_X = 9;
    private static final int END_Y = 9;
    private static int moveCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMaze();

            if (playerX == END_X && playerY == END_Y) {
                System.out.println("🎉 You reached the end in " + moveCount + " moves!");
                break;
            }

            System.out.print("Move (W/A/S/D): ");
            String input = scanner.nextLine().trim().toUpperCase();

            int nextX = playerX;
            int nextY = playerY;

            switch (input) {
                case "W": nextX--; break;
                case "S": nextX++; break;
                case "A": nextY--; break;
                case "D": nextY++; break;
                default:
                    System.out.println("❌ Invalid input. Use W, A, S, or D.");
                    continue;
            }

            if (isValidMove(nextX, nextY)) {
                maze[playerX][playerY] = 2; // Mark current as visited
                playerX = nextX;
                playerY = nextY;
                moveCount++; // Count the move
            } else {
                System.out.println("🚫 Can't move there (wall or out of bounds). Try again.");
            }
        }
    }

    private static boolean isValidMove(int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }

    private static void printMaze() {
        System.out.println();
        System.out.println("Moves: " + moveCount);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("P "); // Player
                } else if (maze[i][j] == 1) {
                    System.out.print("# "); // Wall
                } else if (maze[i][j] == 2) {
                    System.out.print(". "); // Visited
                } else {
                    System.out.print("  "); // Open
                }
            }
            System.out.println();
        }
    }
}
