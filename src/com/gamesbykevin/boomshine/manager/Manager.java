package com.gamesbykevin.boomshine.manager;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.boomshine.board.Ball;
import com.gamesbykevin.boomshine.board.Board;
import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.menu.CustomMenu.LayerKey;
import com.gamesbykevin.boomshine.menu.CustomMenu.OptionKey;
import com.gamesbykevin.boomshine.shared.IElement;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements Disposable, IElement
{
    //the game board
    private Board board;
    
    //keep track of the level
    private int level = 0;
    
    //this will determine if the players game is over
    private boolean gameover = false;
    
    //this timer will track the time passed
    private Timer timer;

    //our game over image that will be buffered
    private BufferedImage gameoverImage;
    
    /**
     * Different modes of play
     * 
     * Free - play with no restrictions
     * Timed - Must beat each level within the given amount of time
     * Survival - You have a limited number of attempts
     */
    public enum Mode
    {
        Free, Timed, Survival
    }
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //dimension size
        //this.dimension = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Dimensions) + 3;
        
        this.board = new Board();
        
        //set the location/dimensions of the board
        this.board.setLocation(0, 0);
        this.board.setDimensions(engine.getMain().getScreen().getWidth(), engine.getMain().getScreen().getHeight());
        
        //create timer
        this.timer = new Timer();
        
        //call this last in the constructor
        setupLevel();
    }
    
    private void setupLevel()
    {
        switch(level)
        {
            case 0:
                this.board.resetBoard(5, 1);
                break;
                
            case 1:
                this.board.resetBoard(10, 2);
                break;
                
            case 2:
                this.board.resetBoard(15, 3);
                break;
                
            case 3:
                this.board.resetBoard(20, 5);
                break;
                
            case 4:
                this.board.resetBoard(25, 7);
                break;
                
            case 5:
                this.board.resetBoard(30, 10);
                break;
                
            case 6:
                this.board.resetBoard(35, 15);
                break;
                
            case 7:
                this.board.resetBoard(40, 21);
                break;
                
            case 8:
                this.board.resetBoard(45, 27);
                break;
                
            case 9:
                this.board.resetBoard(50, 33);
                break;
                
            case 10:
                this.board.resetBoard(55, 44);
                break;
                
            case 11:
                this.board.resetBoard(60, 55);
                break;
        }
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        
    }
    
    /**
     * Update all application elements
     * 
     * @param engine Our main game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (board != null && !gameover)
        {
            board.update(engine);
            
            //update our overall time tracker
            timer.update(engine.getMain().getTime());
            
            if (board.hasGameover())
            {
                //if we won
                if (board.hasSucceeded())
                {
                    //add the progress to the total score
                    board.addScore(board.getProgress());
                    
                    //move to the next level
                    level++;
                    
                    //have we beaten all of the levels
                    if (level > 11)
                        gameover = true;
                    
                    setupLevel();
                }
                else
                {
                    //restart current level
                    setupLevel();
                }
            }
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        if (!gameover)
        {
            if (board != null)
                board.render(graphics);
            
            //draw time elapsed
            graphics.setColor(Color.WHITE);
            graphics.drawString("Time: " + timer.getDescPassed(TimerCollection.FORMAT_6), (int)board.getX() + 25, (int)board.getY() + 25);
        }
        else
        {
            if (this.gameoverImage == null)
            {
                //create buffered image
                this.gameoverImage = new BufferedImage((int)board.getWidth(), (int)board.getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                //graphics object for buffered image
                final Graphics tmp = this.gameoverImage.createGraphics();

                //height of font
                final int height = graphics.getFontMetrics().getHeight();
                
                //set the appropriate font and color
                tmp.setFont(graphics.getFont());
                tmp.setFont(tmp.getFont().deriveFont(36f));
                tmp.setColor(Color.WHITE);

                String display = "Game Over";
                
                int x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                int y = (int)(board.getY() + (board.getHeight() / 3));

                //draw game over message
                tmp.drawString(display, x, y);
                
                //time played message
                display = "Time Played: " + timer.getDescPassed(TimerCollection.FORMAT_6);
                
                //draw string
                x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                y += (height * 2);
                tmp.drawString(display, x, y);
                
                //score played message
                display = "Score: " + board.getScore();
                
                //draw string
                x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                y += (height * 2);
                tmp.drawString(display, x, y + (height * 2));
            }
            else
            {
                graphics.drawImage(gameoverImage, (int)board.getX(), (int)board.getY(), (int)board.getWidth(), (int)board.getHeight(), null);
            }
        }
    }
}