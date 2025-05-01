# Maze Game Solvers

A standalone, interactive Java maze game featuring visualizations of multiple pathfinding algorithms. Built to demonstrate how different graph traversal strategies behave in a maze environment using OOP principles and design patterns.

## Objective

- Practice algorithm implementation with a visual component
- Demonstrate and compare solver behavior: DFS, BFS, A*, Dijkstra
- Use AI (e.g. ChatGPT) to accelerate development
- Practice clean OOP design and design pattern use (Strategy Pattern)

## Algorithms Implemented

- **Depth-First Search (DFS)**: LIFO, explores deep into paths before backtracking
- **Breadth-First Search (BFS)**: FIFO, expands level by level like a flood
- **A\***: Heuristic-based, prioritizes paths closer to the end
- **Dijkstra**: Greedy algorithm using known shortest path, similar to BFS in equal-cost mazes

> ⚠All tiles in the maze have equal traversal cost — these algorithms are used for **comparison**, not efficiency.

## Features

- GUI-based gameplay using `javax.swing`
- Manual control (arrow keys)
- Console-mode gameplay using `WASD` (via separate `main()` method)
- Visual solver playback: shows visited tiles and move cost
- Strategy Pattern used to swap solvers dynamically
- Tile color legend:
  - 🟥 Black: Walls  
  - ⬜ White: Open path  
  - 🟦 Blue: Goal  
  - 🟩 Green: Player  
  - ⬛ Grey: Visited by solver  

## Object-Oriented Design

- Strategy Pattern for flexible algorithm swapping
- Emphasis on cohesion, extensibility, and maintainability
- UML class diagram included in documentation

## Run Instructions

Build/run the JAR located at:

```bash
out/artifacts/MazeGameBuild_jar/MazeGameBuild.jar
