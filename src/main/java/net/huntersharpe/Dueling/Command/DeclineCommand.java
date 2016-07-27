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
package net.huntersharpe.Dueling.Command;

import net.huntersharpe.Dueling.Util.DuelManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.UUID;

public class DeclineCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Text.of(TextColors.RED, "Only players can decline requests!"));
            return CommandResult.empty();
        }
        String sender = args.<String>getOne("player").get();
        Player player = (Player)src;
        if(!Sponge.getServer().getPlayer(sender).isPresent()){
            player.sendMessage(Text.of(TextColors.RED, "Player is no longer online!"));
            return CommandResult.empty();
        }
        Map<UUID, UUID> requests = DuelManager.getDuelManager().getRequestsMap();
        if(!requests.keySet().contains(Sponge.getServer().getPlayer(sender).get().getUniqueId())){
            player.sendMessage(Text.of(TextColors.RED, "You have no request from that player!"));
            return CommandResult.empty();
        }
        DuelManager.getDuelManager().acceptRequest(Sponge.getServer().getPlayer(sender).get());
        player.sendMessage(Text.of(TextColors.GREEN, "Declined Request"));
        return CommandResult.success();
    }
}
