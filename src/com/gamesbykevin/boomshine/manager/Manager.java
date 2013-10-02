package com.gamesbykevin.boomshine.manager;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

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

    //the timer when playing timed mode
    private Timer countdown;
    
    //our game over image that will be buffered
    private BufferedImage gameoverImage;
    
    //the delay which will be multiplied by a factor of the current level you are on
    private static final long DELAY_COUNTDOWN = TimerCollection.toNanoSeconds(15000L);
    
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
    
    //the user selected mode
    private final Mode mode;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //the mode of game play
        this.mode = Mode.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode)];
        
        //set the location/dimensions of the board
        this.board = new Board();
        this.board.setLocation(0, 0);
        this.board.setDimensions(engine.getMain().getScreen().getWidth(), engine.getMain().getScreen().getHeight());
        
        //setup the appropriate timer
        setupTimer();
        
        //call this last in the constructor
        setupLevel();
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        if (this.gameoverImage != null)
            this.gameoverImage.flush();
        
        this.gameoverImage = null;
        
        if (this.board != null)
            this.board.dispose();
        
        this.board = null;
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
            
            //if we are in timed mode make sure time hasn't run out
            if (this.mode == Mode.Timed)
            {
                this.countdown.update(engine.getMain().getTime());
                
                if (this.countdown.hasTimePassed())
                {
                    this.countdown.setRemaining(0);
                    this.gameover = true;
                    return;
                }
            }
            
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
                    
                    //setup the timer
                    setupTimer();
                    
                    //set the variables for the next level
                    setupLevel();
                }
                else
                {
                    //if we didn't succeed and playing survival mode the game ends
                    if (mode == Mode.Survival)
                    {
                        gameover = true;
                    }
                    else
                    {
                        //restart current level
                        setupLevel();
                    }
                }
            }
        }
    }
    
    /**
     * 
     */
    private void setupTimer()
    {
        //create our timer to track overall time played
        if (this.timer == null)
        {
            //create timer
            this.timer = new Timer();
        }
        
        //if this is timed mode, calculate new time for next level
        if (this.mode == Mode.Timed)
        {
            //set the countdown time
            this.countdown = new Timer((DELAY_COUNTDOWN * level) + DELAY_COUNTDOWN);
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
            
            //draw timer
            graphics.setColor(Color.WHITE);
            
            if (this.countdown != null)
            {
                graphics.drawString("Time: " + countdown.getDescRemaining(TimerCollection.FORMAT_6), (int)board.getX() + 25, (int)board.getY() + 25);
            }
            else
            {
                graphics.drawString("Time: " + timer.getDescPassed(TimerCollection.FORMAT_6), (int)board.getX() + 25, (int)board.getY() + 25);
            }
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
                
                final float factorIncrease = 1.95f;
                
                //set the appropriate font and color
                tmp.setFont(graphics.getFont());
                tmp.setFont(tmp.getFont().deriveFont(36f));
                tmp.setColor(Color.WHITE);
                
                String display = "Game Over";
                
                int x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                int y = (int)(board.getY() + (board.getHeight() * .25));

                //draw game over message
                tmp.drawString(display, x, y);
                
                //time played message
                display = "Time Played: " + timer.getDescPassed(TimerCollection.FORMAT_6);
                
                //draw string
                x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                y += (height * factorIncrease);
                tmp.drawString(display, x, y);
                
                //score played message
                display = "Score: " + board.getScore();
                
                //draw string
                x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                y += (height * factorIncrease);
                tmp.drawString(display, x, y);
                
                //let user know
                display = "Hit \"Esc\" to open menu";
                
                //draw string
                x = (int)(board.getX() + (board.getWidth()  / 2) - (tmp.getFontMetrics().stringWidth(display) / 2));
                y += (height * factorIncrease);
                tmp.drawString(display, x, y);
            }
            else
            {
                graphics.drawImage(gameoverImage, (int)board.getX(), (int)board.getY(), (int)board.getWidth(), (int)board.getHeight(), null);
            }
        }
    }
}