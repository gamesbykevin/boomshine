package com.gamesbykevin.boomshine.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
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
    
    //how long should the ball take to expand to its full width
    private static final long EXPAND_DURATION = TimerCollection.toNanoSeconds(250L);
    
    //once the ball is expnaded how long should we freeze
    private static final long FREEZE_DURATION = TimerCollection.toNanoSeconds(2000L);
    
    //how long should the ball take to shrink until it is gone
    private static final long SHRINK_DURATION = TimerCollection.toNanoSeconds(250L);
    
    //each ball will have its own unique identifier
    private final long id = System.nanoTime();
    
    //each different animation
    public enum Animation
    {
        None(0), Expand(EXPAND_DURATION), Pause(FREEZE_DURATION), Shrink(SHRINK_DURATION);
        
        private long duration;
        
        private Animation(final long duration)
        {
            this.duration = duration;
        }
        
        public long getDuration()
        {
            return this.duration;
        }
    }
    
    //what is the ball currently doing
    private Animation animation;
    
    //what is the width limit for expansion
    private final int EXPAND_LIMIT = 60;
    
    //the beginning ball width
    protected static final int BALL_WIDTH_START = 20;
    
    //timer object that will keep track of all the different times
    private TimerCollection timers;
    
    //is the ball dead and should be removed from the collection
    private boolean dead = false;
    
    public Ball(final boolean fill)
    {
        //we will only draw the outline
        this.fill = fill;
        
        //default the animation to none
        this.animation = Animation.None;
        
        //set the width and height dimensions
        setDimensions(BALL_WIDTH_START, BALL_WIDTH_START);
    }
    
    public Ball()
    {
        this(true);
    }
    
    public long getId()
    {
        return this.id;
    }
    
    public void generateColor(final Random random)
    {
        //create random color
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    
    public void setColor(final Color color)
    {
        this.color = color;
    }
    
    private Animation getAnimation()
    {
        return this.animation;
    }
    
    /**
     * Check if the specified animation is the current animation
     * @param animation
     * @return true if match, false otherwise
     */
    public boolean hasAnimation(final Animation animation)
    {
        return (this.animation == animation);
    }
    
    public void setAnimation(final Animation animation)
    {
        this.animation = animation;
    }
    
    public boolean isDead()
    {
        return this.dead;
    }
    
    private void setDead()
    {
        this.dead = true;
    }
    
    /**
     * Here we will check if the current ball intersects the ball parameter
     * @param ball The ball we want to check for collision
     * @return true if collision, false otherwise
     */
    public boolean intersects(final Ball ball)
    {
        //get the distance
        final double distance = Math.sqrt(Math.pow(ball.getX() - getX(), 2) + Math.pow(ball.getY() - getY(), 2));
        
        //if the distance is less than half the width(s) they intersect
        return (distance <= (getWidth() / 2) + (ball.getWidth() / 2));
    }
    
    /**
     * Can this ball cause a chain reaction
     * @return True if it is expanding or paused, false otherwise
     */
    public boolean canChain()
    {
        return (this.animation == Animation.Expand || this.animation == Animation.Pause);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        super.update();
        
        //if the timers have not been setup yet
        if (this.timers == null)
        {
            setupTimers(engine.getMain().getTime());
        }
        
        //update the appropriate timer
        timers.update(getAnimation());
                
        //we may be possibly updating the width, so use this variable
        final int width;
        
        switch(getAnimation())
        {
            //no animation, so nothing will be done
            case None:
                break;
                
            //the ball is expanding
            case Expand:
                
                //set the width depending on the progress of the timer and the pixel limit
                width = (int)(timers.getProgress(animation) * (EXPAND_LIMIT - BALL_WIDTH_START)) + BALL_WIDTH_START;
                
                //set the new dimensions as well
                super.setDimensions(width, width);
                
                //if the time has passed
                if (timers.hasTimePassed(getAnimation()))
                {
                    //reset the pause timer
                    timers.reset(Animation.Pause);
                    
                    //set the pause animation as the current
                    setAnimation(Animation.Pause);
                }
                
                break;
               
            //the ball is shrinking
            case Shrink:
                
                //set the width depending on the progress of the timer and the pixel limit
                width = (int)((1.0 - timers.getProgress(animation)) * EXPAND_LIMIT);
                
                //if time has passed
                if (timers.hasTimePassed(getAnimation()))
                {
                    //mark the ball as dead since we are done shrinking
                    setDead();
                    
                    //also we give no width or height so the ball won't be visible
                    super.setDimensions(0, 0);
                }
                else
                {
                    //set the new dimensions as well
                    super.setDimensions(width, width);
                }
                
                break;
            
            //the ball is paused
            case Pause:
                
                //if time has passed
                if (timers.hasTimePassed(getAnimation()))
                {
                    //reset the shrink timer
                    timers.reset(Animation.Shrink);
                    
                    //set the appropriate animation
                    setAnimation(Animation.Shrink);
                }
                
                break;
        }
    }
    
    /**
     * Setup all of the timers for the ball
     * @param time 
     */
    private void setupTimers(final long time)
    {
        this.timers = new TimerCollection(time);
        
        for (Animation tmp : Animation.values())
        {
            this.timers.add(tmp, tmp.getDuration());
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