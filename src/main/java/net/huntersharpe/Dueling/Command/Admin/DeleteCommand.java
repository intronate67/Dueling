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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeleteCommand implements CommandExecutor {
    //Confirmation message?
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("name").get();
        if(!Dueling.getInstance().getConfig().getNode("arenas").getChildrenList().contains(name)){
            src.sendMessage(Text.of(TextColors.RED, "No arena by that name exists!"));
            return CommandResult.empty();
        }
        ArenaManager.getArenaManager().deleteArena(name);
        src.sendMessage(Text.of(TextColors.GREEN, "Arena: ", name, " deleted!"));
        return CommandResult.success();
    }
}