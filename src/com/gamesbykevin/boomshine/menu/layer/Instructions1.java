package com.gamesbykevin.boomshine.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.*;
import com.gamesbykevin.boomshine.menu.CustomMenu;

public class Instructions1 extends Layer implements LayerRules
{
    public Instructions1(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(MenuImage.Keys.Instructions1));
        setNextLayer(CustomMenu.LayerKey.Instructions2);
        setForce(false);
        setPause(true);
        setTimer(null);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}