package org.pokesplash.pokedex.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.LuckPermsUtils;
import org.pokesplash.pokedex.util.Utils;

public class ReloadCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("reload")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
                        try {
                            return LuckPermsUtils.hasPermission(ctx.getPlayerOrThrow(), CommandHandler.basePermission + ".reload");
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
			PokeDex.load();

			context.getSource().sendMessage(Text.literal("Config reloaded."));

			return 1;
		} catch (Exception ex) {
			Utils.printCommandException(ex, context);
			return 0;
		}
	}
}
