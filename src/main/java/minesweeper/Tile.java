package minesweeper;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    
    protected int x;
    protected int y;
    protected int CELLSIZE;
    protected int CELLHEIGHT;
    protected int TOPBAR;

    protected boolean isClicked = false;
    protected boolean isMine = false;
    protected boolean isFlagged;
    protected int minesNearby = -1;
    protected int mineFrameCount = -1;
    protected boolean toExplode = false;
    protected boolean isExploded = false;

    public Tile(int x, int y,int CELLSIZE, int CELLHEIGHT, int TOPBAR){
        this.x = x;
        this.y = y; 
        // Record the board set-up in the tile
        this.CELLHEIGHT = CELLHEIGHT;
        this.CELLSIZE = CELLSIZE;
        this.TOPBAR = TOPBAR;
    }

    public void draw(App app) {
        // Based on the different scenario of the tile to draw        
        // If it is opened but not mine
        if (isClicked && !isMine){
            app.image(app.getSprite("tile"),x,y);
            // Display the number of mines nearby
            if(this.minesNearby > 0){
                app.fill(app.mineCountColour[minesNearby][0],app.mineCountColour[minesNearby][1],app.mineCountColour[minesNearby][2]);
                app.textSize(16);
                app.textAlign(App.CENTER, App.CENTER);
                app.text(this.minesNearby,x + App.CELLSIZE/2, y + App.CELLHEIGHT/2);
            }
        } else if (toExplode && isMine){
            // If the tile has a mine and it is its turn to explode
            // Check if the animation has ended
            if(mineFrameCount >= 9*3){
                app.image(app.getSprite("mine9"),x,y); 
                if(mineFrameCount >= 10*3){
                    // Delay 3 frames to explode the next mine
                    isExploded = true;
                } else {
                    mineFrameCount ++;
                }  
            } else {
                // Process to explode the mine
                mineFrameCount++;
                int mineImgNum = (int)(mineFrameCount/3);
                app.image(app.getSprite("mine" + mineImgNum),x,y);
            }    
        } else if (isFlagged){
            // Display the flag on tile if it is flagged
            app.image(app.getSprite("tile1"),x,y);
            app.image(app.getSprite("flag"),x,y);
        } else {
            if(app.gameOver && isMine){
                // if the game is over and lost, display the other mines
                if((!app.gameWin) && (!isExploded)){
                    app.image(app.getSprite("mine0"),x,y);
                }
            } else if (app.mouseX >= x && app.mouseX <= x+ CELLSIZE && app.mouseY >= y && app.mouseY <= y + CELLHEIGHT){ 
                // If the cursor is on it, display the special tile image               
                app.image(app.getSprite("tile2"),x,y);
            } else {
                // Display the normal tile
                app.image(app.getSprite("tile1"),x,y);
            }
        }
    }

    // Return true if it is a mine, false if it is not
    public boolean open(Tile [][] board){
        // Only allow open if it is not flagged
        boolean opened = false;
        if(!isFlagged){
            this.isClicked = true;
            if (this.isMine){
                // Return true if it is a Mine
                opened = true;
            }
        }

        // Open the surrounding if it has zero mines nearby
        if (minesNearby == 0){
            List<Tile> tiles = this.getClosedAdjacentTiles(board);
            for (Tile t: tiles){
                t.open(board);
            }
        }
        return opened;
    }

    public void explode(){
        if(!toExplode){
            this.toExplode = true;
        }
    }

    // Get the un-opened tiles nearby
    public List<Tile> getClosedAdjacentTiles(Tile[][] board){
        ArrayList<Tile> result = new ArrayList<>();
        int xNum = (int)(x/CELLSIZE);
        int yNum = (int)((y-TOPBAR)/CELLHEIGHT);

        // Check the surrounding tiles 
        for(int rowNum = yNum - 1; rowNum <= yNum + 1; rowNum++){
            // If the rowNum is out of range, skip to the next rowNum
            if (rowNum < 0 || rowNum >= board.length){
                continue;
            }
            for (int colNum = xNum - 1; colNum <= xNum + 1; colNum++ ){
                // If the colNum is out of range, skip to the next colNum
                if(colNum < 0 || colNum >= board[0].length){
                    continue;
                } else if(rowNum == yNum && colNum == xNum){
                    // If it is checking the center tile
                    continue;
                } else if(!board[rowNum][colNum].isClicked) {
                    // Add to the list only if it is an un-opened tile
                    result.add(board[rowNum][colNum]);
                }
            }
        }

        return result;
    }


    // Return true if it can be flagged/unflagged, false if it cannot
    public boolean flag(){
        // If it is flagged
        if(isFlagged){
            this.isFlagged = false;
            return true;
        } else {
            // If it is not flagged and not clicked yet
            if(!isClicked){
                this.isFlagged = true;
                return true;
            }
        }
        return false;
    }

    // Return true if the mine can be placed
    public boolean placeMine(){
        // Can only place Mine if there is no mine yet
        if (!this.isMine){
            this.isMine = true;
            return true;
        }
        return false;
    }

    // Count the Mines near the current tile
    public void countMine(Tile[][] board){
        int count = 0;
        int xNum = (int)(x/CELLSIZE);
        int yNum = (int)((y-TOPBAR)/CELLHEIGHT);

        // Put -1 if it is a mine on the current tile
        if (isMine){
            count = -1;
        } else {
            // Check the surrounding mines 
            for(int rowNum = yNum - 1; rowNum <= yNum + 1; rowNum++){
                // If the rowNum is out of range, skip to the next rowNum
                if (rowNum < 0 || rowNum >= board.length){
                    continue;
                }
                for (int colNum = xNum - 1; colNum <= xNum + 1; colNum++ ){
                    // If the colNum is out of range, skip to the next colNum
                    if(colNum < 0 || colNum >= board[0].length){
                        continue;
                    } else if(rowNum == yNum && colNum == xNum){
                        // If it is checking the center tile
                        continue;
                    }else if (board[rowNum][colNum].hasMine()){
                        // Add to the count if a Mine is found nearby
                        count ++;
                    }
                }
            }
        }

        this.minesNearby = count;
    }

    // Return if the the tile has mine in it
    public boolean hasMine(){
        return this.isMine;
    }
}
