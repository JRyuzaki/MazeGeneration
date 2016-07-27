package com.company.maze;


import java.awt.*;
import java.awt.image.BufferedImage;

public class MazeRenderer {
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;
    private boolean displayUnvisitedCells = true;
    private boolean markVisitedCells = false;
    private Color visitedCellsColor = Color.blue;
    private boolean markCurrentCell = false;
    private Color currentCellColor = Color.green;
    private int wallSize = 2;

    private BufferedImage lastRender = null;

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setDisplayUnvisitedCells(boolean displayUnvisitedCells) {
        this.displayUnvisitedCells = displayUnvisitedCells;
    }

    public void setMarkVisitedCells(boolean markVisitedCells) {
        this.markVisitedCells = markVisitedCells;
    }

    public void setVisitedCellsColor(Color visitedCellsColor) {
        this.visitedCellsColor = visitedCellsColor;
    }

    public void setMarkCurrentCell(boolean markCurrentCell){
        this.markCurrentCell = markCurrentCell;
    }

    public void setCurrentCellColor(Color currentCellColor){
        this.currentCellColor = currentCellColor;
    }

    public void setWallSize(int cellSize) {
        this.wallSize = cellSize;
    }

    public BufferedImage renderMaze(Maze maze){
        BufferedImage image = new BufferedImage(maze.getWidth() * wallSize + 1, maze.getHeight() * wallSize + 1, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = (Graphics2D)image.getGraphics();
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(0, 0, wallSize * maze.getWidth() + 1, wallSize * maze.getHeight() + 1);

        for(int y = 0; y < maze.getHeight(); ++y){
            for(int x = 0; x < maze.getWidth(); ++x){
                MazeCell currentMazeCell = maze.getMazeCell(x, y);

                if(currentMazeCell == null)
                    continue;

                if(!currentMazeCell.isVisited() && !displayUnvisitedCells) {
                    continue;
                }

                if(markVisitedCells && currentMazeCell.isVisited()){
                    graphics2D.setColor(visitedCellsColor);
                    graphics2D.fillRect(x * wallSize, y * wallSize, wallSize, wallSize);
                }

                graphics2D.setColor(foregroundColor);
                for(int i = 0; i < 4; ++i){
                    if(currentMazeCell.hasWall(i)){
                        switch (i){
                            case 0:
                                graphics2D.drawLine(x * wallSize, y * wallSize, (x + 1) * wallSize, y * wallSize);
                                break;
                            case 1:
                                graphics2D.drawLine((x + 1) * wallSize, y * wallSize, (x + 1) * wallSize, (y + 1) * wallSize);
                                break;
                            case 2:
                                graphics2D.drawLine(x * wallSize, (y + 1) * wallSize, (x + 1) * wallSize, (y + 1) * wallSize);
                                break;
                            case 3:
                                graphics2D.drawLine(x * wallSize, y * wallSize, x * wallSize, (y + 1) * wallSize);
                                break;
                        }
                    }
                }
            }
        }

        if(!maze.isDone() && markCurrentCell) {
            Point currentPoint = maze.getCurrentPosition();
            graphics2D.setColor(currentCellColor);
            graphics2D.fillRect(currentPoint.x * wallSize, currentPoint.y * wallSize, wallSize, wallSize);
        }

        this.lastRender = image;
        return image;
    }
}
