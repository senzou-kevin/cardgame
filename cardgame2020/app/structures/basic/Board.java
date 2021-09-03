package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;

import java.sql.Array;
import java.util.ArrayList;

/**
 * create a board field on the screen
 */
public class Board {
    //board's Tiles
    Tile[][] board;


    /**
     * new 9 * 5 Tiles
     * */
    public Board() {
        board = new Tile[9][5];
        for(int x = 0; x < 9;x++){
            for(int y =0; y < 5;y++){
                board[x][y] = BasicObjectBuilders.loadTile(x,y);
            }
        }
    }
    public Tile[][] getTiles()
    {
        return board;
    }

    /**
     * draw 9 * 5 Tiles
     * */
    public void drawBoard(ActorRef out){
        for(int x = 0; x < 9;x++){
            for(int y =0; y < 5;y++){
                BasicCommands.drawTile(out,board[x][y],0);
                try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }

    }

    /**
     * get Tile from Board by Tilex and Tiley
     * */
    public Tile getTileByBoard(int tilex,int tiley){
        return board[tilex][tiley];
    }

}
