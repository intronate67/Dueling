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
package net.huntersharpe.Dueling;

import com.google.inject.Inject;
import net.huntersharpe.Dueling.Command.AcceptCommand;
import net.huntersharpe.Dueling.Command.Admin.CreateCommand;
import net.huntersharpe.Dueling.Command.Admin.DefaultsCommand;
import net.huntersharpe.Dueling.Command.Admin.DeleteCommand;
import net.huntersharpe.Dueling.Command.DeclineCommand;
import net.huntersharpe.Dueling.Command.DuelCommand;
import net.huntersharpe.Dueling.Command.StartCommand;
import net.huntersharpe.Dueling.Util.ArenaManager;
import net.huntersharpe.Dueling.Util.DuelManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id="dueling", name="Dueling", version="1.0")
public class Dueling {

    private static Dueling instance;

    public static Dueling getInstance(){
        return instance;
    }
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader = null;

    private CommentedConfigurationNode configNode = null;

    @Inject
    private Game game;

    @Listener
    public void onPreInit(GamePreInitializationEvent e){
        instance = this;
        new DuelManager();
        new ArenaManager();
        game.getCommandManager().register(this, duelSpec, "duel");
    }

    @Listener
    public void onLogout(ClientConnectionEvent.Disconnect e){
        //Remove from maps
    }

    //TODO: Permissions
    //Might switch over to children inside of map.
    private CommandSpec createSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("name")), GenericArguments.integer(Text.of("time")))
            .executor(new CreateCommand())
            .build();
    private CommandSpec delSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("name")))
            .executor(new DeleteCommand())
            .build();
    private CommandSpec defaults = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("name")),
                    GenericArguments.string(Text.of("operation")),
                    GenericArguments.optional(
                            GenericArguments.integer(Text.of("value"))
                    )
            )
            .executor(new DefaultsCommand())
            .build();
    private CommandSpec acceptSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("player")))
            .executor(new AcceptCommand())
            .build();
    private CommandSpec declineSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("player")))
            .executor(new DeclineCommand())
            .build();
    private CommandSpec startSpec = CommandSpec.builder()
            .arguments(GenericArguments.string(Text.of("player")))
            .executor(new StartCommand())
            .build();
    private CommandSpec duelSpec = CommandSpec.builder()
            .child(createSpec, "create")
            .child(delSpec, "del", "delete")
            .child(defaults, "defaults")
            .child(acceptSpec, "accept")
            .child(declineSpec, "delcine")
            .child(startSpec, "start")
            .executor(new DuelCommand())
            .build();

    public void setupConfig(){
        if(!Files.exists(configDir)) {
            if (Files.exists(configDir.resolveSibling("underground"))) {
                try {
                    Files.move(configDir.resolveSibling("underground"), configDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Files.createDirectories(configDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Files.exists(Paths.get(configDir + "/config.conf"))){
            try{
                Files.createFile(Paths.get(configDir + "/config.conf"));
                saveConfig();
                loadConfig();

            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            loadConfig();
        }
    }

    public void loadConfig(){
        try {
            configNode = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            loader.save(configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentedConfigurationNode getConfig(){
        return configNode;
    }

    //Load arenas into lists/maps
    private void serializeConfig(){

    }

}
