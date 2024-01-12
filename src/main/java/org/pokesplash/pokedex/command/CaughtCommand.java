package org.pokesplash.pokedex.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.dex.DexEntry;
import org.pokesplash.pokedex.ui.DexMenu;
import org.pokesplash.pokedex.util.LuckPermsUtils;
import org.pokesplash.pokedex.util.Utils;

import java.util.ArrayList;

public class CaughtCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("caught")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						try {
							return LuckPermsUtils.hasPermission(ctx.getPlayerOrThrow(),
									CommandHandler.basePermission + ".base");
						} catch (CommandSyntaxException e) {
							PokeDex.LOGGER.error(e);
							return false;
						}
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {
		try {
			if (!context.getSource().isExecutedByPlayer()) {
				context.getSource().sendMessage(Text.literal("This command must be ran by a player."));
			}

			ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

			ArrayList<DexEntry> caught = AccountProvider.getAccount(player.getUuid()).getCaught();

			ArrayList<Species> species = new ArrayList<>();
			for (DexEntry entry : caught) {
				species.add(PokemonSpecies.INSTANCE.getByPokedexNumber(entry.getDexNumber(), Cobblemon.MODID));
			}

			UIManager.openUIForcefully(player, new DexMenu().getPage(context, player.getUuid(), species));

			return 1;
		} catch (Exception ex) {
			Utils.printCommandException(ex, context);
			return 0;
		}
	}
}
