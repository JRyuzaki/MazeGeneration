package com.company;


import com.company.maze.Maze;
import com.company.maze.MazeRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final int BLACK_COLOR_CODE = Color.BLACK.getRGB();
    private static final int WHITE_COLOR_CODE = Color.WHITE.getRGB();

    public static void main(String[] args) {
        MazeRenderer mazeRenderer = new MazeRenderer();
        int maxCycle;
        int mazeWidth, mazeHeight;
        BufferedImage template = null;
        boolean saveFrames;
        String outputDirectoryPath = "";


        CommandLineArgs commandLineArgs = new CommandLineArgs();
        try {
            commandLineArgs.parseComandline(args);
        } catch (IllegalArgumentException e) {
            return;
        }

        maxCycle = commandLineArgs.updateCycles;

        if(commandLineArgs.backgroundColorHexString != null){
            try{
                mazeRenderer.setBackgroundColor(Color.decode(commandLineArgs.backgroundColorHexString));
            }catch (NumberFormatException e){
                mazeRenderer.setBackgroundColor(Color.BLACK);
            }
        }

        if(commandLineArgs.foregroundColorHexString != null){
            try{
                mazeRenderer.setForegroundColor(Color.decode(commandLineArgs.foregroundColorHexString));
            }catch (NumberFormatException e){
                mazeRenderer.setForegroundColor(Color.WHITE);
            }
        }

        mazeRenderer.setDisplayUnvisitedCells(!commandLineArgs.showOnlyVisitedCells);

        if(commandLineArgs.visitedCellColor != null){
            mazeRenderer.setMarkVisitedCells(true);
            try {
                mazeRenderer.setVisitedCellsColor(Color.decode(commandLineArgs.visitedCellColor));
            }catch(NumberFormatException e){
                mazeRenderer.setVisitedCellsColor(Color.BLUE);
            }
        }

        if(commandLineArgs.currentCellColor != null){
            mazeRenderer.setMarkCurrentCell(true);
            try {
                mazeRenderer.setCurrentCellColor(Color.decode(commandLineArgs.currentCellColor));
            }catch(NumberFormatException e){
                mazeRenderer.setCurrentCellColor(Color.GREEN);
            }
        }

        saveFrames = commandLineArgs.saveAllFrames;

        if(commandLineArgs.outputDirectory != null){
            outputDirectoryPath = commandLineArgs.outputDirectory;
        }

        if(commandLineArgs.templateFile != null){
            try {
                template = ImageIO.read(commandLineArgs.templateFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(commandLineArgs.cellSize <= 0){
            mazeRenderer.setWallSize(2);
        }else{
            mazeRenderer.setWallSize(commandLineArgs.cellSize);
        }

        Maze maze = new Maze(commandLineArgs.mazeWidth, commandLineArgs.mazeHeight);

        if(template != null){
            mazeWidth = maze.getWidth();
            mazeHeight = maze.getHeight();

            if(mazeWidth != template.getWidth() || mazeHeight != template.getHeight())
                maze = new Maze(template.getWidth(), template.getHeight());

            int templateColorCode;
            if(!commandLineArgs.invertTemplate)
                templateColorCode = BLACK_COLOR_CODE;
            else
                templateColorCode = WHITE_COLOR_CODE;

            for(int y = 0; y < template.getHeight(); ++y){
                for(int x = 0; x < template.getWidth(); ++x){
                    if(template.getRGB(x, y) == templateColorCode){
                        maze.removeMazeCellFromArray(x, y);
                    }
                }
            }
        }

        maze.setStartpoint(maze.getRandomCoordinates());

        for (int cycle = 0; cycle < maxCycle; ++cycle) {
            if(saveFrames){
                saveMazeRender(mazeRenderer, maze, outputDirectoryPath);
            }

            maze.updateMaze();

            if(maze.isDone()){
                break;
            }
        }

        if(!saveFrames){
            saveMazeRender(mazeRenderer, maze, outputDirectoryPath);
        }
    }

    public static void saveMazeRender(MazeRenderer mazeRenderer, Maze maze, String outputDirectoryPath){
        try {
            ImageIO.write((mazeRenderer.renderMaze(maze)), "png", new File(outputDirectoryPath + "/" + System.nanoTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
