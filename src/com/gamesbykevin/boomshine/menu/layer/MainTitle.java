package com.gamesbykevin.boomshine.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.boomshine.engine.Engine;
import com.gamesbykevin.boomshine.resource.*;
import com.gamesbykevin.boomshine.menu.CustomMenu;
import com.gamesbykevin.boomshine.shared.Shared;

public class MainTitle extends Layer implements LayerRules
{
    public MainTitle(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setTitle(Shared.GAME_NAME);
        setImage(engine.getResources().getMenuImage(MenuImage.Keys.TitleBackground));
        setForce(false);
        setPause(true);
        setTimer(new Timer(TimerCollection.toNanoSeconds(5000L)));
        setOptionContainerRatio(RATIO);
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //options for this layer setup here
        Option tmp;
        
        //create start game option with next layer specified
        tmp = new Option(CustomMenu.LayerKey.StartGame);
        
        //add option selection to option
        tmp.add("Start Game", null);
        
        //add option with specified unique key to this Layer
        super.add(CustomMenu.OptionKey.StartGame, tmp);
        
        
        tmp = new Option(CustomMenu.LayerKey.Options);
        tmp.add("Options", null);
        super.add(CustomMenu.OptionKey.Options, tmp);
        
        
        tmp = new Option(CustomMenu.LayerKey.Controls1);
        tmp.add("Controls", null);
        super.add(CustomMenu.OptionKey.Controls, tmp);
        
        
        tmp = new Option(CustomMenu.LayerKey.Instructions1);
        tmp.add("Instructions", null);
        super.add(CustomMenu.OptionKey.Instructions, tmp);
        
        
        tmp = new Option(CustomMenu.LayerKey.Credits);
        tmp.add("Credits", null);
        super.add(CustomMenu.OptionKey.Credits, tmp);
    }    
}