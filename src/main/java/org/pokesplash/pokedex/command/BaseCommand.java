package org.pokesplash.pokedex.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.ui.DexMenu;
import org.pokesplash.pokedex.util.LuckPermsUtils;
import org.pokesplash.pokedex.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class BaseCommand {
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
                .literal("dex")
                .requires(ctx -> {
                    if (ctx.isExecutedByPlayer()) {
                        try {
                            return LuckPermsUtils.hasPermission(ctx.getPlayerOrThrow(), CommandHandler.basePermission + ".base");
                        } catch (CommandSyntaxException e) {
                            PokeDex.LOGGER.error(e);
                            return false;
                        }
                    } else {
                        return true;
                    }
                })
                .executes(this::run);

        LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

        dispatcher.register(CommandManager.literal("pokedex").requires(
                        ctx -> {
                            if (ctx.isExecutedByPlayer()) {
                                try {
                                    return LuckPermsUtils.hasPermission(ctx.getPlayerOrThrow(), CommandHandler.basePermission + ".base");
                                } catch (CommandSyntaxException e) {
                                    PokeDex.LOGGER.error(e);
                                    return false;
                                }
                            } else {
                                return true;
                            }
                        })
                .redirect(registeredCommand).executes(this::run));

        registeredCommand.addChild(new ReloadCommand().build());
        registeredCommand.addChild(new RewardsCommand().build());
        registeredCommand.addChild(new CaughtCommand().build());
        registeredCommand.addChild(new NeededCommand().build());

    }

    public int run(CommandContext<ServerCommandSource> context) {
        try {
            if (!context.getSource().isExecutedByPlayer()) {
                context.getSource().sendMessage(Text.literal("This command must be ran by a player."));
            }

            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            Collection<Species> list = new ArrayList<>();
            if (PokeDex.config.isImplementedOnly()) {
                list.addAll(PokemonSpecies.INSTANCE.getImplemented());
            } else {
                list.addAll(PokemonSpecies.INSTANCE.getSpecies());
            }

            UIManager.openUIForcefully(player, new DexMenu().getPage(context, player.getUuid(),
                    list));
            return 1;
        } catch (Exception ex) {
            Utils.printCommandException(ex, context);
            return 0;
        }
    }
}
