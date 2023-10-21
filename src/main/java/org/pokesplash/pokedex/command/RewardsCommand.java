package org.pokesplash.pokedex.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.ui.RewardsMenu;
import org.pokesplash.pokedex.util.LuckPermsUtils;

public class RewardsCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("rewards")
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

		UIManager.openUIForcefully(player, new RewardsMenu().getPage(player.getUuid()));

		return 1;
	}
}
