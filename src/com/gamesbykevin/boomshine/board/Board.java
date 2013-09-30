package com.gamesbykevin.boomshine.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The board contains the balls
 * @author GOD
 */
public class Board extends Sprite implements Disposable, IElement
{
    //the list of balls on the screen
    private List<Ball> balls;
    
    //seed used to generate random numbers
    private final long seed = System.nanoTime();
    
    //random number generator object
    private Random random;
    
    //the number of balls that indicates when level is complete
    private int goal;
    
    //the beginning ball width
    private static final int BALL_WIDTH_START = 20;
    
    public Board()
    {
        balls = new ArrayList<>();
        
        random = new Random(seed);
        
        //display seed for now
        System.out.println("Seed = " + seed);
    }
    
    //create a new board
    public void resetBoard(final int count, final int goal)
    {
        //clear list before we start
        balls.clear();
        
        while (balls.size() < count)
        {
            Ball ball = new Ball(random);
            
            //set random location
            ball.setLocation(getX() + random.nextInt((int)getWidth()), getY() + random.nextInt((int)getHeight()));
            
            //set the width/height of the ball
            ball.setDimensions(BALL_WIDTH_START, BALL_WIDTH_START);
            
            final int maxSpeed = 2;
            
            while(ball.getVelocityX() == 0)
            {
                //set random velocity
                ball.setVelocityX(random.nextInt(maxSpeed * 2) - maxSpeed);
            }
            
            while (ball.getVelocityY() == 0)
            {
                //set random velocity
                ball.setVelocityY(random.nextInt(maxSpeed * 2) - maxSpeed);
            }
            
            //add ball to our list
            balls.add(ball);
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (Ball ball : balls)
        {
            //update the ball
            ball.update(engine);
            
            //if x is out of bounds
            if (ball.getX() < getX() || ball.getX() > getX() + getWidth())
                ball.setVelocityX(-ball.getVelocityX());
            
            //if y is out of bounds
            if (ball.getY() < getY() || ball.getY() > getY() + getHeight())
                ball.setVelocityY(-ball.getVelocityY());
            
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (Ball ball : balls)
        {
            ball.render(graphics);
        }
    }
}