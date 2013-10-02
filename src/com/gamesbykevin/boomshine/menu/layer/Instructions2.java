package com.gamesbykevin.boomshine.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.Resources;
import com.gamesbykevin.boomshine.menu.CustomMenu;

public class Instructions2 extends Layer implements LayerRules
{
    public Instructions2(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Instructions2));
        setNextLayer(CustomMenu.LayerKey.MainTitle);
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