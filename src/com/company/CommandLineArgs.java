package com.company;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.File;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;

public class CommandLineArgs {
    @Option(name="-w", aliases = {"--width"}, usage="Width of the Maze")
    public int mazeWidth;
    @Option(name="-h", aliases = {"--height"}, usage="Height of the Maze")
    public int mazeHeight;

    @Option(name="-c", aliases = {"--cycle"}, usage = "Number of cycles the Maze should perform")
    public int updateCycles;

    @Option(name="-bg", aliases = {"--background-color", "--background"}, usage = "Hex String that represents the Background color")
    public String backgroundColorHexString;
    @Option(name="-fg", aliases = {"--foreground-color", "--foreground"}, usage = "Hex String that represents the Foreground color")
    public String foregroundColorHexString;

    @Option(name="-v", aliases = {"--only-visited", "--visited"}, usage = "Boolean Value for only showing visited cells", handler = BooleanOptionHandler.class)
    public boolean showOnlyVisitedCells;

    @Option(name="-mv", aliases = {"--mark-visited"}, usage = "Hex String that represents the color of visited Cells")
    public String visitedCellColor;
    @Option(name="-mc", aliases = {"--mark-current"}, usage = "Hex String that represents the color of the current cell")
    public String currentCellColor;

    @Option(name = "-f", aliases = {"--output"}, usage = "Path to directory where all frames will be stored")
    public String outputDirectory;

    @Option(name="-t", aliases = {"--template"}, usage = "Path to a template image", metaVar = "OUTPUT")
    public File templateFile;

    @Option(name="-sf", aliases = {"--save-frames"}, usage = "Boolean Value for saving all frames", handler = BooleanOptionHandler.class)
    public boolean saveAllFrames;

    @Option(name="-cs", aliases = {"--cell-size"}, usage = "Size of ")
    public int cellSize;

    public void parseComandline(String[] args){
        CmdLineParser parser = new CmdLineParser(this);
        try{
            parser.parseArgument(args);

            if(this.mazeWidth <= 0){
                System.err.println("Given Maze-Width is invalid! Will use default value.");
                this.mazeWidth = 40;
            }

            if(this.mazeHeight <= 0){
                System.err.println("Given Maze-Height is invalid! Will use default value.");
                this.mazeHeight = 40;
            }

            if(this.updateCycles <= 0){
                System.err.println("Given Number of Cycles is invalid! Will update Maze till it is finished");
                this.updateCycles = Integer.MAX_VALUE;
            }

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java MazeGen [options...] args...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("Example: java MazeGen " + parser.printExample(ALL));
            throw new IllegalArgumentException();
        }
    }
}
