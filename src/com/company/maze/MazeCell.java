package com.company.maze;

public class MazeCell {
    /**
     * 0 = North
     * 1 = East
     * 2 = South
     * 3 = West
     */
    private boolean[] walls;
    private boolean visited;

    public MazeCell(){
        this.walls = new boolean[4];
        this.resetWalls();
        this.visited = false;
    }

    public void resetWalls(){
        for(int i = 0; i < this.walls.length; ++i){
            this.walls[i] = true;
        }
    }

    public boolean[] getWalls(){
        return this.walls;
    }

    public boolean hasWall(int i){
        return this.walls[i];
    }

    public void removeWall(int i){
        this.walls[i] = false;
    }

    public void visit(){
        this.visited = true;
    }

    public boolean isVisited() {
        return visited;
    }
}
