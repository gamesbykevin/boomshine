package com.gamesbykevin.boomshine.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.*;
import com.gamesbykevin.boomshine.menu.CustomMenu;

public class Title extends Layer implements LayerRules
{
    public Title(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        super.setImage(engine.getResources().getMenuImage(MenuImage.Keys.TitleScreen));

        //we will not force this layer to show
        setForce(true);
        
        //we do not want to pause this layer once it completes
        setPause(false);
        
        //the next layer to go to
        setNextLayer(CustomMenu.LayerKey.Credits);
        
        //this layer will be active for 2 seconds
        setTimer(new Timer(TimerCollection.toNanoSeconds(2000L)));
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}
