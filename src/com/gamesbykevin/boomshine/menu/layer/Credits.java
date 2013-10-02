package com.gamesbykevin.boomshine.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.Resources;
import com.gamesbykevin.boomshine.menu.CustomMenu;

public class Credits extends Layer implements LayerRules
{
    public Credits(final Engine engine)
    {
        super(Layer.Type.SCROLL_VERTICAL_NORTH, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Credits));
        setForce(true);
        setPause(false);
        setNextLayer(CustomMenu.LayerKey.MainTitle);
        setTimer(new Timer(TimerCollection.toNanoSeconds(7000L)));
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}