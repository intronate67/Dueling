/*This file is part of Dueling, licensed under the MIT License (MIT).
*
* Copyright (c) 2016 Hunter Sharpe
* Copyright (c) contributors

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package net.huntersharpe.Dueling.Util;

import net.huntersharpe.Dueling.Dueling;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class ArenaManager {

    private static ArenaManager arenaManager;

    public static ArenaManager getArenaManager(){
        return arenaManager;
    }

    public ArenaManager(){
        arenaManager = this;
    }

    private List<String> arenas = new ArrayList<>();

    public List<String> getArenasList(){
        return arenas;
    }

    private Map<String, Map<UUID, UUID>> inUseArenas = new HashMap<>();

    public Map<String, Map<UUID, UUID>> getInUseArenaMap(){
        return inUseArenas;
    }

    private CommentedConfigurationNode arenaNode = Dueling.getInstance().getConfig().getNode("arenas");

    public void createArena(String name, Player player){
        arenaNode.getNode("name", "world").setValue(player.getWorld().getName());
        arenaNode.getNode("name", "x").setValue(player.getLocation().getX());
        arenaNode.getNode("name", "y").setValue(player.getLocation().getY());
        arenaNode.getNode("name", "z").setValue(player.getLocation().getZ());
        arenaNode.getNode("name", "battleTime").setValue(300);
    }

    public void deleteArena(String name){
        arenaNode.removeChild("name");
    }

    public void editArena(String name, String operation, Object value){
        //Change spawn
        if(operation.equalsIgnoreCase("spawn")){
            Location<World> loc = (Location<World>)value;
            arenaNode.getNode(name, "world").setValue(loc.getExtent().getName());
            arenaNode.getNode(name, "x").setValue(loc.getX());
            arenaNode.getNode(name, "y").setValue(loc.getY());
            arenaNode.getNode(name, "z").setValue(loc.getZ());
        }else{
            //Change battle time
            int time = (Integer)value;
            arenaNode.getNode(name, "battleTime").setValue(time);
        }
    }

}
