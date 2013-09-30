package com.gamesbykevin.boomshine.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.shared.IElement;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * This is the ball on the board
 * @author GOD
 */
public class Ball extends Sprite implements Disposable, IElement
{
    //random color
    private Color color;
    
    //do we fill the ball
    private final boolean fill;
    
    //should the ball expand
    private boolean expand = false;
    
    //should the ball shrink
    private boolean shrink = false;
    
    //expand-shrink for 3 seconds
    private final long EXPAND_DURATION = TimerCollection.toNanoSeconds(3000L);
    
    //what is the width limit for expansion
    private final int EXPAND_LIMIT = 50;
    
    //timer to keep track of how long expanding/shrinking
    private Timer timer;
    
    public Ball()
    {
        //we will only draw the outline
        this.fill = false;
        
        //the timer so we know the progress of expanding/shrinking
        this.timer = new Timer(EXPAND_DURATION);
    }
    
    /**
     * Each ball will have a random color
     * @param random 
     */
    public Ball(final Random random)
    {
        //create random color
        color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        
        this.fill = true;
    }
    
    public void setColor(final Color color)
    {
        this.color = color;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        super.update();
        
        if (shrink)
        {
            timer.update(engine.getMain().getTime());
            
            //set the dimensions accordingly
            super.setDimensions(timer.getProgress() * EXPAND_LIMIT, timer.getProgress() * EXPAND_LIMIT);
            
        }
        
        if (expand)
        {
            timer.update(engine.getMain().getTime());
            
            //set the dimensions accordingly
            super.setDimensions(timer.getProgress() * EXPAND_LIMIT, timer.getProgress() * EXPAND_LIMIT);
            
            if (timer.hasTimePassed())
            {
                
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        setX(getX() - (getWidth() / 2));
        setY(getY() - (getHeight() / 2));
        
        graphics.setColor(color);
        
        if (fill)
        {
            graphics.fillOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        }
        else
        {
            graphics.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        }
        
        setX(getX() + (getWidth() / 2));
        setY(getY() + (getHeight() / 2));
    }
}