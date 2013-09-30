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
import java.awt.Point;
import java.awt.Rectangle;
/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements Disposable, IElement
{
    //the timer to keep track of time playing
    private Timer timer;
    
    //the game board
    private Board board;
    
    //the object representing the mouse
    private Ball mouse;
    
    //the width of the mouse capture display
    private static final int MOUSE_DIMENSION = 15;
    
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
        
        /**
         * The score will be the number of chains you get in each level added up
         * 
         * maybe have a certain number of lives to complete the level
         * timed mode, you will have unlimited lives but a specific time to finish the level
         * 
         * Level 1  = 05 is the count and 01 is the goal
         * Level 2  = 10 is the count and 02 is the goal
         * Level 3  = 15 is the count and 03 is the goal
         * Level 4  = 20 is the count and 05 is the goal
         * Level 5  = 25 is the count and 07 is the goal
         * Level 6  = 30 is the count and 10 is the goal
         * Level 7  = 35 is the count and 15 is the goal
         * Level 8  = 40 is the count and 21 is the goal
         * Level 9  = 45 is the count and 27 is the goal
         * Level 10 = 50 is the count and 33 is the goal
         * Level 11 = 55 is the count and 44 is the goal
         * Level 12 = 60 is the count and 55 is the goal
         * 
         * 
         */
        
        
        this.board.resetBoard(5, 1);
        
        //new object representing the mouse
        mouse = new Ball();
        mouse.setColor(Color.WHITE);
        mouse.setDimensions(MOUSE_DIMENSION, MOUSE_DIMENSION);
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
        if (this.board != null)
            this.board.update(engine);
        
        //set the appropriate location
        mouse.setLocation(engine.getMouse().getLocation());
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        if (this.board != null)
            this.board.render(graphics);
        
        if (this.mouse != null)
            this.mouse.render(graphics);
    }
}