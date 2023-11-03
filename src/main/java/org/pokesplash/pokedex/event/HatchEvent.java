package org.pokesplash.pokedex.event;

import kotlin.Unit;
import net.minecraft.text.Text;
import org.pokesplash.daycare.event.DayCareEvents;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.account.Account;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.dex.RewardProgress;
import org.pokesplash.pokedex.util.Utils;

public class HatchEvent {
	public void registerEvent() {
		DayCareEvents.RETRIEVE_EGG.subscribe(e -> {
			int dexNumber = e.getBaby().getSpecies().getNationalPokedexNumber();
			Account account = AccountProvider.getAccount(e.getPlayer().getUuid());

			if (!account.getPokemon(dexNumber).isCaught()) {
				account.setCaught(dexNumber, true);

				double progress = Utils.getDexProgress(account);

				// Checks if any rewards have been met and completes them.
				for (Reward reward : PokeDex.config.getRewards()) {
					if (progress >= reward.getProgress() &&
							!account.getReward(progress).isComplete()) {
						account.completeReward(progress);
						e.getPlayer().sendMessage(Text.literal(
								"§2You have unlocked a Pokedex reward!"));
					}
				}

				boolean isComplete = true;
				for (RewardProgress rwd : account.getAllRewards()) {
					if (!rwd.isComplete()) {
						isComplete = false;
					}
				}
				if (isComplete) {
					account.setComplete(true);
				}
			}
		});
	}
}
