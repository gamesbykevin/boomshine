package com.gamesbykevin.boomshine.resource;

import static com.gamesbykevin.boomshine.resource.Resources.RESOURCE_DIR;
import com.gamesbykevin.framework.resources.*;

/**
 * All audio for game
 * @author GOD
 */
public class GameAudio extends AudioManager
{
    //location of resources
    private static final String DIRECTORY = "audio/game/sound/{0}.wav";
    
    //description for progress bar
    private static final String DESCRIPTION = "Loading Game Audio Resources";
    
    public enum Keys
    {
        Sound1, Sound2, Sound3, Sound4, Sound5, Sound6, Sound7
    }
    
    public GameAudio() throws Exception
    {
        super(RESOURCE_DIR + DIRECTORY, Keys.values());
        
        //the description that will be displayed for the progress bar
        super.setDescription(DESCRIPTION);
    }
}
