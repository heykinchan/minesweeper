package minesweeper;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 576; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = HEIGHT/CELLHEIGHT;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();
	
	public static int[][] mineCountColour = new int[][] {
            {0,0,0}, // 0 is not shown
            {0,0,255},
            {0,133,0},
            {255,0,0},
            {0,0,132},
            {132,0,0},
            {0,132,132},
            {132,0,132},
            {32,32,32}
    };
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.
    public Tile[][] board = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    public static int numMine = -1;
    private int startTime = 0;
    public boolean gameOver = false;
    public boolean gameWin = false;

    private HashMap<String, PImage> sprites = new HashMap<>();

    // Function to access the image stored in the App
    public PImage getSprite(String s) {
        PImage result = sprites.get(s);
        if (result == null) {
            result = loadImage(this.getClass().getResource(s+".png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            sprites.put(s, result);
        }
        return result;
    }

    // Function to check if the game has been won already
    private void checkWin(){
        boolean won = true;
        //Checks if all the non-mine tiles have been opened
        for(int rowNum = 0; rowNum < BOARD_HEIGHT; rowNum ++){
            for (int colNum = 0; colNum < BOARD_WIDTH; colNum ++){
                if((!board[rowNum][colNum].isMine) && (!board[rowNum][colNum].isClicked)){
                    won = false;
                }
            }
        }
        // If the game is won
        if(won){
            this.gameWin = true;
            this.gameOver = true;
        }
    }

    // Function to set up/reset the game
    private void reset(){
        gameOver = false;
        gameWin = false;

        // Log the start time
        startTime = millis();

        // create attributes for data storage, eg board
        // Initialize the board with all empty tiles
        for (int rowNum = 0; rowNum < BOARD_HEIGHT; rowNum++){
            for (int colNum = 0; colNum < BOARD_WIDTH; colNum++){
                board[rowNum][colNum] = new Tile(
                    colNum * CELLSIZE,
                    rowNum * CELLSIZE + TOPBAR,
                    CELLSIZE, CELLHEIGHT,TOPBAR);                
            }
        }

        // Read the number of Mine from command line, set it to 100 if no valid input is read
        if(numMine == -1){
            numMine = 100;
        }
        // Place mine to the board
        int minePlaced = 0;
        while (minePlaced < numMine){
            for (int rowNum = 0; rowNum < BOARD_HEIGHT; rowNum++){
                if(minePlaced == numMine){
                    break;
                }
                for (int colNum = 0; colNum < BOARD_WIDTH; colNum++){
                    if (minePlaced == numMine){
                        break;
                    }
                    // To place Mine randomly onto the board
                    boolean temp = random.nextInt(BOARD_HEIGHT * BOARD_WIDTH - 1) == 0;
                    if(temp){
                        if(board[rowNum][colNum].placeMine()){
                            minePlaced++;
                        }
                    }           
                }
            }
        }
        
        // Count the mines nearby for each tile
        for (int rowNum = 0; rowNum < BOARD_HEIGHT; rowNum++){
            for (int colNum = 0; colNum < BOARD_WIDTH; colNum++){
                board[rowNum][colNum].countMine(board);             
            }
        }
    }

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT + TOPBAR);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        
        // Pre-load the images to the App
        String[] sprites = new String[] {
            "tile1",
            "tile2",
            "flag",
            "tile"
        };
        for (int i = 0; i < sprites.length; i++) {
            getSprite(sprites[i]);
        }
        for (int i = 0; i < 10; i++) {
            getSprite("mine"+String.valueOf(i));
        }
        // Set up the game using reset() function
        reset();
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        // Reset the game if the game is over
        if (gameOver && (event.getKey() == 'r' || event.getKey() == 'R')) {
            // Restart the game
            reset();
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        // NOTHING HAPPENS
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver){
            int colNum = mouseX / CELLSIZE;
            int rowNum = (mouseY - TOPBAR) / CELLSIZE;

            // Handle the reaction based on the right click or left click
            if (colNum < BOARD_WIDTH && rowNum < BOARD_HEIGHT && colNum >= 0 && rowNum >= 0){
                if (mouseButton == LEFT){
                    boolean gameStop = board[rowNum][colNum].open(board);
                    if(gameStop){
                        gameOver = true;
                    }
                } else if (mouseButton == RIGHT) {
                    board[rowNum][colNum].flag();
                }
            }

            // Check if the game has been won
            checkWin();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // NOTHING HAPPENS
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        if(!gameOver){
            // clear the background
            background(255);

            // draw the top bar with timer
            int timeSpent = (millis() - startTime) / 1000;
            fill(0);
            textSize(24);
            textAlign(RIGHT,BOTTOM);
            text("Time: " + timeSpent, WIDTH-(CELLSIZE * 2),TOPBAR);

            //draw game board
            for (Tile[]row: board){
                for(Tile t: row){
                    t.draw(this);
                }
            }
        } else {
            //draw the final game board, display the mines if needed
            for (Tile[]row: board){
                for(Tile t: row){
                    t.draw(this);
                }
            }
            // Display the Win / Lost message in the topbar
            if (gameWin){
                // Game over with win
                fill(0, 255, 0);
                textSize(32);
                textAlign(LEFT, BOTTOM);
                text("You win!", CELLSIZE *2, TOPBAR);
            } else {
                // Game over with lost
                fill(255, 0, 0);
                textSize(32);
                textAlign(LEFT, BOTTOM);
                text("You Lost!", CELLSIZE *2, TOPBAR);

                // Make each of the mine explode
                boolean mineExploding = false;
                for (int rowNum = 0; rowNum < BOARD_HEIGHT; rowNum ++){
                    if(mineExploding){
                        break;
                    }
                    for (int colNum = 0; colNum < BOARD_WIDTH; colNum ++){
                        // Check if the mine is exploding
                        if (board[rowNum][colNum].isMine && board[rowNum][colNum].toExplode && (!board[rowNum][colNum].isExploded)){
                            mineExploding = true;
                            break;
                        } else if (board[rowNum][colNum].isMine && (!board[rowNum][colNum].toExplode)){
                            board[rowNum][colNum].explode();
                            mineExploding = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // Check if there is any parameter in the command line
        if (args.length == 1) {
            try {
                int temp = Integer.parseInt(args[0]);  
                if (temp <= 0 || temp >= BOARD_HEIGHT * BOARD_WIDTH){
                    throw new IllegalArgumentException();
                }
                numMine = temp;
            } catch (NumberFormatException e) {
                System.err.println("The input is invalid, the program will be launch with the default number (100) of mines.");
            } catch (IllegalArgumentException e){
                System.err.println("The input is invalid, the program will be launch with the default number (100) of mines.");
            }
        }
        
        PApplet.main("minesweeper.App");
    }

}
