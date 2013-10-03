package com.gamesbykevin.boomshine.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.boomshine.board.Ball.Animation;
import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.*;
import com.gamesbykevin.boomshine.shared.IElement;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
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
    
    //the object representing the mouse
    private Ball player;
    
    //the number of balls that indicates when level is complete
    private int goal;
    
    //the number of balls to start off with when the board is reset
    private int count;
    
    //where are we currently at towards our goal
    private int progress;
    
    //the max speed the ball can travel
    private static final int BALL_MAX_SPEED = 1;
    
    //random list of sound effects
    private List<GameAudio.Keys> keys;
    
    //has the level completed
    private boolean gameover = false;
    
    //did we win
    private boolean succeed = false;
    
    //the area where we draw the player information
    private Point locationInfo;
    
    //the area where we draw the player score
    private Point locationScore;
    
    //the score
    private int score = 0;
    
    public Board()
    {
        super.dispose();
        
        //create an empty list for our balls
        balls = new ArrayList<>();
        
        //our random number generator
        random = new Random(seed);
        
        //create empty list of possible sound effects to play
        keys = new ArrayList<>();
        
        //fill empty list
        resetAudioSelections();
    }
    
    @Override
    public void dispose()
    {
        for (Ball ball : balls)
        {
            ball.dispose();
        }
        
        balls.clear();
        balls = null;
        
        random = null;
        
        player.dispose();
        player = null;
        
        keys.clear();
        keys = null;
    }
    
    private void resetAudioSelections()
    {
        keys.add(GameAudio.Keys.Sound1);
        keys.add(GameAudio.Keys.Sound2);
        keys.add(GameAudio.Keys.Sound3);
        keys.add(GameAudio.Keys.Sound4);
        keys.add(GameAudio.Keys.Sound5);
    }
    
    /**
     * Add the parameter score to our total score
     * @param score 
     */
    public void addScore(final int score)
    {
        this.score += score;
    }
    
    /**
     * Get the score
     * @return 
     */
    public int getScore()
    {
        return this.score;
    }
    
    /**
     * Get the progress for the board
     * @return int The number of chain reactions
     */
    public int getProgress()
    {
        return this.progress;
    }
    
    //create a new board
    public void resetBoard(final int count, final int goal)
    {
        //clear list before we start
        balls.clear();
        
        //set the goal
        this.goal = goal;
        
        //set the count
        this.count = count;
        
        //start the progress back at 0
        this.progress = 0;
        
        //set flag to false so the game won't be over
        this.gameover = false;
        
        //the  possible different speeds of the balls
        List<Float> speeds = new ArrayList<>();
        
        for (float current = -BALL_MAX_SPEED; current <= BALL_MAX_SPEED; current+=.5)
        {
            if (current == 0)
                continue;
            
            speeds.add(current);
        }
        
        //continue to loop until we have reached our count
        while (balls.size() < count)
        {
            //create a new ball
            Ball ball = new Ball();
            
            final int x = (int)(getX() + random.nextInt((int)(getWidth()  - Ball.BALL_WIDTH_START)) + Ball.BALL_WIDTH_START);
            final int y = (int)(getY() + random.nextInt((int)(getHeight() - Ball.BALL_WIDTH_START)) + Ball.BALL_WIDTH_START);
            
            //set random location
            ball.setLocation(x, y);
            
            //create a random color for the ball
            ball.generateColor(random);
            
            //set the ball moving in a random direction
            ball.setVelocityX(speeds.get(random.nextInt(speeds.size())));
            ball.setVelocityY(speeds.get(random.nextInt(speeds.size())));
            
            //add ball to our list
            balls.add(ball);
        }
        
        //create a ball that will represent the player's mouse
        player = new Ball(false);
        player.setColor(Color.WHITE);
        player.setLocation(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
        player.setDimensions(Ball.BALL_WIDTH_START, Ball.BALL_WIDTH_START);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if the level is over
        if (gameover)
            return;
        
        boolean play = false;
        
        if (player != null)
        {
            //make sure the player animation doesn't exist
            if (player.hasAnimation(Animation.None))
            {
                //set the appropriate location for the player mouse
                player.setLocation(engine.getMouse().getLocation());

                //if the mouse was pressed check for collision
                if (engine.getMouse().isMousePressed())
                {
                    //mark the player ball as hit
                    mark(player);

                    //reset the mouse events
                    engine.getMouse().reset();
                }
            }
            else
            {
                //update the player animation
                player.update(engine);

                //if the player is not still check if other balls hit it
                if (player.canChain())
                {
                    //check if balls collide
                    for (Ball ball : balls)
                    {
                        if (player.intersects(ball) && ball.hasAnimation(Animation.None))
                        {
                            //mark the ball as hit
                            mark(ball);
                            
                            //play random hit sound effect
                            play = true;
                            
                            //increase the progress
                            this.progress++;
                        }
                    }
                }
            }
            
            if (player.isDead())
                player = null;
        }
        
        for (Ball ball : balls)
        {
            if (ball == null)
                continue;
            
            //update the ball
            ball.update(engine);
            
            //if the ball is moving west and out of bounds on the west side
            if (ball.getX() - (ball.getWidth() / 2) <= getX() && ball.getVelocityX() < 0)
                ball.setVelocityX(-ball.getVelocityX());
            
            //if the ball is moving east and out of bounds on the east side
            if (ball.getX() + (ball.getWidth() / 2) >= getX() + getWidth() && ball.getVelocityX() > 0)
                ball.setVelocityX(-ball.getVelocityX());
            
            //if the ball is moving north and out of bounds on the north side
            if (ball.getY() - (ball.getHeight() / 2) <= getY() && ball.getVelocityY() < 0)
                ball.setVelocityY(-ball.getVelocityY());
            
            //if the ball is moving south and out of bounds on the south side
            if (ball.getY() + (ball.getHeight() / 2) >= getY() + getHeight() && ball.getVelocityY() > 0)
                ball.setVelocityY(-ball.getVelocityY());
            
            //if the ball is not still check for collision
            if (ball.canChain())
            {
                //check if balls collide
                for (Ball tmp : balls)
                {
                    //if the balls are the same don't continue
                    if (ball.getId() == tmp.getId())
                        continue;
                    
                    if (ball.intersects(tmp) && tmp.hasAnimation(Animation.None))
                    {
                        //mark the ball as hit
                        mark(tmp);
                        
                        //play random hit sound effect
                        play = true;
                        
                        //increase the progress
                        this.progress++;
                    }
                }
            }
        }
        
        //remove dead balls
        for (int i=0; i < balls.size(); i++)
        {
            if (balls.get(i).isDead())
            {
                balls.remove(i);
                i--;
            }
        }
        
        //if any collision flag will be true to play a random sound effect
        if (play)
        {
            final int index = random.nextInt(keys.size());
            
            //play random hit sound effect
            engine.getResources().playGameAudio(keys.get(index));
            
            //remove constant from list
            keys.remove(index);
            
            //if list is empty reset
            if (keys.isEmpty())
                resetAudioSelections();
        }
        
        //if the game is not over lets check to see if it is
        if (!gameover)
        {
            //if the board is finished lets check success or fail
            if (hasFinished())
            {
                //we have succeeded if the number of balls chained at least matches our goal
                succeed = (count - balls.size() >= goal);
                
                if (succeed)
                {
                    //play succeed sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.Sound6);
                }
                else
                {
                    //play fail sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.Sound7);
                }
                
                //flag game over as true
                gameover = true;
                
                //reset mouse events
                engine.getMouse().reset();
            }
        }
    }
    
    public boolean hasSucceeded()
    {
        return this.succeed;
    }
    
    public boolean hasGameover()
    {
        return this.gameover;
    }
    
    /**
     * This method will check if the chain effect has started and finished
     * @return boolean
     */
    private boolean hasFinished()
    {
        //make sure player has already expanded and finished
        if (player == null)
        {
            //check each ball
            for (Ball ball : balls)
            {
                //if one of the balls is doing something other than nothing we are not finished
                if (!ball.hasAnimation(Animation.None))
                    return false;
            }
            
            //if none of the balls can cause a chain and the player is non-existent we are finished
            return true;
        }
        
        return false;
    }
    
    /**
     * Set the ball as hit
     * @param ball The ball we want to modiify
     */
    private void mark(final Ball ball)
    {
        //set the animation
        ball.setAnimation(Animation.Expand);

        //stop the velocity
        ball.resetVelocity();
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (Ball ball : balls)
        {
            if (ball == null)
                continue;
            
            ball.render(graphics);
        }
        
        if (player != null)
            player.render(graphics);
        
        //draw the extra information
        renderInformation(graphics);
    }
    
    private void renderInformation(final Graphics graphics)
    {
        if (locationInfo == null)
        {
            //create a new location
            locationInfo = new Point();
            locationInfo.x = (int)(getX() + 25);
            locationInfo.y = (int)(getY() + getHeight() - graphics.getFontMetrics().getHeight());
        }
        
        if (locationScore == null)
        {
            //create a new location
            locationScore = new Point();
            locationScore.x = (int)(getX() + getWidth() - 100);
            locationScore.y = (int)(getY() + getHeight() - graphics.getFontMetrics().getHeight());
        }
        
        if (progress >= goal)
        {
            graphics.setColor(Color.GREEN);
        }
        else
        {
            graphics.setColor(Color.WHITE);
        }
        
        //set the appropriate font
        graphics.setFont(graphics.getFont().deriveFont(24f));
        
        //draw progress and count
        graphics.drawString(progress + "/" + goal + "  " + count + " balls.", locationInfo.x, locationInfo.y);
        
        //draw score
        graphics.drawString("Score: " + score, locationScore.x, locationScore.y);
    }
}