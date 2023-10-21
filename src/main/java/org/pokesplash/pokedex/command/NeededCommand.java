package org.pokesplash.pokedex.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.dex.DexEntry;
import org.pokesplash.pokedex.ui.DexMenu;
import org.pokesplash.pokedex.util.LuckPermsUtils;

import java.util.ArrayList;

public class NeededCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("needed")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".reload");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		ServerPlayerEntity player = context.getSource().getPlayer();

		ArrayList<DexEntry> caught = AccountProvider.getAccount(player.getUuid()).getNeeded();

		ArrayList<Species> species = new ArrayList<>();
		for (DexEntry entry : caught) {
			species.add(PokemonSpecies.INSTANCE.getByPokedexNumber(entry.getDexNumber(), Cobblemon.MODID));
		}

		UIManager.openUIForcefully(player, new DexMenu().getPage(player.getUuid(), species));

		return 1;
	}
}
