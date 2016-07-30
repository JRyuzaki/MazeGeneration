package com.company.maze;

import java.awt.*;
import java.util.Random;
import java.util.Stack;

public class Maze {
    private int width;
    private int height;

    private int lastCheckX;
    private int lastCheckY;

    private MazeCell[][] maze;

    private Stack<Point> mazeCellStack;

    private Random random;

    public Maze(int width, int height){
        this.width = width;
        this.height = height;

        this.lastCheckX = this.lastCheckY = 0;

        this.maze = new MazeCell[this.height][this.width];

        this.mazeCellStack = new Stack<>();

        this.random = new Random();

        for(int y = 0; y < this.height; ++y){
            for(int x = 0; x < this.width; ++x){
                this.maze[y][x] = new MazeCell();
            }
        }
    }

    public void updateMaze(){
        Point currentCoordinates;
        MazeCell currentCell;

        while(true) {
            if(this.mazeCellStack.isEmpty() && !this.checkIfMazeHasOtherUnvisitedCells())
                return;

            currentCoordinates = this.mazeCellStack.peek();
            currentCell = this.getMazeCell(currentCoordinates);
            if(currentCell != null && this.hasUnvisitedNeighbours(currentCoordinates))
                break;

            this.mazeCellStack.pop();
        }

        boolean updating = true;

        int tries = 0;
        while(updating) {
            if(tries == 10)
                while(true) ;


            int direction = this.random.nextInt(4);
            Point newCoordinates = new Point(currentCoordinates);

            switch (direction) {
                case 0:
                    if (currentCoordinates.y == 0)
                        continue;
                    newCoordinates.setLocation(currentCoordinates.x, currentCoordinates.y - 1);
                    break;
                case 1:
                    if (currentCoordinates.x == (this.width - 1))
                        continue;
                    newCoordinates.setLocation(currentCoordinates.x + 1, currentCoordinates.y);
                    break;
                case 2:
                    if (currentCoordinates.y == (this.height - 1))
                        continue;
                    newCoordinates.setLocation(currentCoordinates.x, currentCoordinates.y + 1);
                    break;
                case 3:
                    if (currentCoordinates.x == 0)
                        continue;
                    newCoordinates.setLocation(currentCoordinates.x - 1, currentCoordinates.y);
                    break;
            }

            MazeCell newCell = this.getMazeCell(newCoordinates);

            if(newCell == null || newCell.isVisited())
                continue;

            switch(direction){
                case 0:
                    currentCell.removeWall(0);
                    newCell.removeWall(2);
                    break;
                case 1:
                    currentCell.removeWall(1);
                    newCell.removeWall(3);
                    break;
                case 2:
                    currentCell.removeWall(2);
                    newCell.removeWall(0);
                    break;
                case 3:
                    currentCell.removeWall(3);
                    newCell.removeWall(1);
                    break;
            }

            this.maze[currentCoordinates.y][currentCoordinates.x] = currentCell;
            this.maze[newCoordinates.y][newCoordinates.x] = newCell;

            updating = false;
            this.visit(newCoordinates);
        }
    }

    private boolean checkIfMazeHasOtherUnvisitedCells() {
        for(int y = this.lastCheckY; y < this.height; ++y){
            for(int x = this.lastCheckX; x < this.width; ++x){
                if(this.maze[y][x] != null && !this.maze[y][x].isVisited()){
                    this.lastCheckX = x;
                    this.lastCheckY = y;
                    this.visit(new Point(x, y));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasUnvisitedNeighbours(Point currentCoordinates) {
        for(int y = -1; y <= 1; y += 2){
            int newY = currentCoordinates.y + y;

            if(newY < 0 || newY > (this.height - 1))
                continue;

            MazeCell currentMazeCell = this.maze[newY][currentCoordinates.x];

            if(currentMazeCell != null && !currentMazeCell.isVisited()){
                return true;
            }
        }

        for(int x = -1; x <= 1; x += 2){
            int newX = currentCoordinates.x + x;

            if(newX < 0 || newX > (this.width - 1))
                continue;

            MazeCell currentMazeCell = this.maze[currentCoordinates.y][newX];
            if(currentMazeCell != null && !currentMazeCell.isVisited()){
                return true;
            }
        }
        return false;
    }

    public MazeCell getMazeCell(int x, int y){
        return this.maze[y][x];
    }

    public MazeCell getMazeCell(Point point){
        return this.maze[point.y][point.x];
    }

    public Point getRandomCoordinates() {
        boolean validPosition = true;
        Point point = new Point();

        while(validPosition) {
            point = new Point(this.random.nextInt(this.width), this.random.nextInt(this.height));
            if(getMazeCell(point) != null)
                validPosition = false;
        }
        return point;
    }

    public void visit(Point coordinates){
        this.maze[coordinates.y][coordinates.x].visit();
        this.mazeCellStack.push(coordinates);
    }

    public boolean isDone(){
        return this.mazeCellStack.isEmpty();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCurrentPosition(){
        return this.mazeCellStack.peek();
    }

    public void removeMazeCellFromArray(int x, int y){
        this.maze[y][x] = null;
    }

    public void setStartpoint(Point point){
        this.visit(point);
    }
}
