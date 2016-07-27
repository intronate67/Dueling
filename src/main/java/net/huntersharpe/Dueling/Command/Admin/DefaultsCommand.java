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
package net.huntersharpe.Dueling.Command.Admin;

import net.huntersharpe.Dueling.Dueling;
import net.huntersharpe.Dueling.Util.ArenaManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DefaultsCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Text.of(TextColors.RED, "Only players can use /duel defaults"));
            return CommandResult.empty();
        }
        String name = args.<String>getOne("name").get();
        Player player = (Player)src;
        if(!Dueling.getInstance().getConfig().getNode("arenas").getChildrenList().contains(name)){
            player.sendMessage(Text.of(TextColors.RED, "No arena by that name exists in the config"));
            return CommandResult.empty();
        }
        String operation = args.<String>getOne("operation").get();
        switch(operation){
            case "spawn":
                if(args.<Integer>getOne("value").isPresent()){
                    player.sendMessage(Text.of(TextColors.RED, "Usage: /duel defaults <spawn|time> [value]\n Only time can indlue a value."));
                    break;
                }
                ArenaManager.getArenaManager().editArena(name, operation, player.getLocation());
                player.sendMessage(Text.of(TextColors.GREEN, "Spawn in arena: ", name, " was updated to your location!"));
                break;
            case "time":
            case "battletime":
                int time = args.<Integer>getOne("value").get();
                ArenaManager.getArenaManager().editArena(name, operation, time);
                player.sendMessage(Text.of(TextColors.GREEN, "Battle time in arena: ", name, " was updated to ", time));
                break;
            default:
                break;
        }
        return CommandResult.success();
    }
}