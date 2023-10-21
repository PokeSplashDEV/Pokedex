package org.pokesplash.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.account.Account;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.util.Utils;

public class PokemonCaughtEvent {
	public void registerEvent() {
		CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {
			int dexNumber = e.getPokemon().getSpecies().getNationalPokedexNumber();
			Account account = AccountProvider.getAccount(e.getPlayer().getUuid());

			if (!account.getPokemon(dexNumber).isCaught()) {
				account.setCaught(dexNumber, true);

				double progress = Utils.getDexProgress(account);

				// Checks if any rewards have been met and completes them.
				for (Reward reward : PokeDex.config.getRewards()) {
					if (progress >= reward.getProgress() && !account.getReward(progress).isComplete()) {
						account.completeReward(progress);
						e.getPlayer().sendMessage(Text.literal(
								"You have unlocked a Pokedex reward for " + reward.getProgress() +
										"% progress"));
					}
				}
			}

			return Unit.INSTANCE;
		});
	}
}
