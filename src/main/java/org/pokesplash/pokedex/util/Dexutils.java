package org.pokesplash.pokedex.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.account.Account;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.dex.RewardProgress;

public abstract class Dexutils {
	public static void checkDex(Pokemon pokemon, ServerPlayerEntity player) {
		int dexNumber = pokemon.getSpecies().getNationalPokedexNumber();
		Account account = AccountProvider.getAccount(player.getUuid());

		if (!account.getPokemon(dexNumber).isCaught()) {
			account.setCaught(dexNumber, true);

			double progress = Utils.getDexProgress(account);

			// Checks if any rewards have been met and completes them.
			for (Reward reward : PokeDex.config.getRewards()) {
				if (progress >= reward.getProgress() &&
						!account.getReward(reward.getProgress()).isComplete()) {
					account.completeReward(reward.getProgress());
					player.sendMessage(Text.literal(
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
	}
}
