package org.pokesplash.pokedex.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.account.Account;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.util.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RewardsMenu {
	public Page getPage(UUID player) {
		Account account = AccountProvider.getAccount(player);
		double progress = Utils.getDexProgress(account);

		int highestSlot = -1;
		HashMap<Integer, Button> rewards = new HashMap<>();
		for (Reward reward : PokeDex.config.getRewards()) {
			// Finds the highest slot for the row amount.
			if (reward.getSlotNumber() > highestSlot) {
				highestSlot = reward.getSlotNumber();
			}

			GooeyButton.Builder button = GooeyButton.builder()
					.display(Utils.parseItemId(reward.getItemMaterial()))
					.title("§3" + (int) reward.getProgress() + "%");

			ArrayList<String> lore = new ArrayList<>();

			if (progress >= reward.getProgress()) {
				if (account.getReward(reward.getProgress()).isRedeemed()) {
					lore.add("§bYou have already claimed this reward");
				} else {
					lore.add("§aYou can claim this reward!");
					button.onClick(e -> {
						// Run commands
						CommandDispatcher<ServerCommandSource> dispatcher =
								e.getPlayer().getServer().getCommandManager().getDispatcher();
						for (String command : reward.getCommands()) {
							try {
								dispatcher.execute(
										Utils.formatPlaceholders(command, e.getPlayer().getName().getString()),
										e.getPlayer().getServer().getCommandSource());
							} catch (CommandSyntaxException ex) {
								throw new RuntimeException(ex);
							}
						}
						account.redeemReward(reward.getProgress());
						UIManager.closeUI(e.getPlayer());
						e.getPlayer().sendMessage(
								Text.literal("§c[Pokedex] §2You successfully redeemed the " + (int) reward.getProgress() +
										"% dex rewards."));
					});
				}
			} else {
				lore.add("§cYou need " + reward.getProgress() + " to claim this reward");
				lore.add("§6Current Progress: " +
						new BigDecimal(progress).setScale(2, RoundingMode.HALF_EVEN).floatValue()
				+ "%");
			}
			button.lore(lore);
			rewards.put(reward.getSlotNumber(), button.build());
		}

		int rows = (int) Math.ceil((double) highestSlot / 9);

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(PokeDex.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(Math.min(rows + 1, 6))
				.fill(filler);

		for (int slotNumber : rewards.keySet()) {
			template.set(slotNumber, rewards.get(slotNumber));
		}

		return GooeyPage.builder()
				.title(PokeDex.lang.getTitle())
				.template(template.build())
				.build();
	}


}
