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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DuelManager {

    private static DuelManager duelManager;

    public static DuelManager getDuelManager(){
        return duelManager;
    }

    public DuelManager(){
        duelManager = this;
    }

    private Text prefix = Text.of(
            TextColors.GRAY,
            "[",
            TextColors.BLUE,
            "Duel",
            TextColors.GRAY,
            "] "
    );

    //Sender:Key Recipient:Value
    private Map<UUID, UUID> requests = new HashMap<>();

    private Map<UUID, Location> returnLoc = new HashMap<>();

    private CommentedConfigurationNode config = Dueling.getInstance().getConfig();

    public void sendRequest(Player sender, Player recipient){
        requests.put(sender.getUniqueId(), recipient.getUniqueId());
        sender.sendMessage(Text.of(prefix, "You have send a request to battle to ", recipient.getName()));
        recipient.sendMessage(Text.of(prefix, "You have a new duel request from: ", sender.getName()));
        recipient.sendMessage(Text.of(prefix, "Do /duel view to view your current requests!"));
    }

    public void acceptRequest(Player player){
        Player acceptant = Sponge.getServer().getPlayer(requests.get(player.getUniqueId())).get();
        String arena = findArena();
        if(arena.equals(null)){
            acceptant.sendMessage(Text.of(prefix, "All current arenas are full! please try again later."));
            return;
        }else{
            returnLoc.put(player.getUniqueId(), player.getLocation());
            returnLoc.put(acceptant.getUniqueId(), acceptant.getLocation());
            World world = Sponge.getServer().getWorld(config.getNode(arena, "world").getString()).get();
            double x = config.getNode(arena, "x").getDouble();
            double y = config.getNode(arena, "y").getDouble();
            double z = config.getNode(arena, "z").getDouble();
            Transform<World> location = new Transform<>(new Location<>(world, x,y,z));
            player.transferToWorld(location.getExtent().getUniqueId(), location.getPosition());
            player.setTransform(location);
            player.sendMessage(Text.of(prefix, "Starting duel!"));
            acceptant.sendMessage(Text.of(prefix, "Starting duel!"));
        }
    }

    public void declineRequest(Player player){

    }

    public void viewRequests(Player player){

    }

    public void quitArena(Player player){

    }

    public Map<UUID, UUID> getRequestsMap(){
        return requests;
    }

    public Map<UUID, Location> getReturnLocationMap(){
        return returnLoc;
    }

    public String findArena(){
        String arena = null;
        Map<String, Map<UUID, UUID>> inUse = ArenaManager.getArenaManager().getInUseArenaMap();
        List<String> arenas = ArenaManager.getArenaManager().getArenasList();
        for(int i=0; i < arenas.size(); i++){
            if(inUse.containsKey(arenas.get(i))){
                break;
            }else{
                arena = arenas.get(i);
            }
        }
        return arena;
    }
}
