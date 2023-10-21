package org.pokesplash.pokedex.account;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class AccountProvider {
	private static HashMap<UUID, Account> accounts;

	public static Account getAccount(UUID uuid) {
		if (!accounts.containsKey(uuid)) {
			accounts.put(uuid, new Account(uuid));
		}
		return accounts.get(uuid);
	}

	public static void updateAccount(Account account) {
		accounts.put(account.getUuid(), account);
	}

	public static void init() {
		accounts = new HashMap<>();

		File dir = Utils.checkForDirectory(PokeDex.BASE_PATH + "accounts/");

		String[] files = dir.list();

		if (files.length == 0) {
			return;
		}

		for (String file : files) {
			Utils.readFileAsync(PokeDex.BASE_PATH + "accounts/", file, el -> {
				Gson gson = Utils.newGson();
				Account account = gson.fromJson(el, Account.class);
				accounts.put(account.getUuid(), account);
			});
		}

		int totalPokemon = PokemonSpecies.INSTANCE.getSpecies().size();

		// If there are more Pokemon than in the account, add the new ones.
		for (UUID account : accounts.keySet()) {
			Account acc = accounts.get(account);
			if (acc.getPokemonCount() < totalPokemon) {
				ArrayList<Integer> newPokemon = new ArrayList<>();
				for (int x=acc.getPokemonCount() + 1; x <= totalPokemon; x++) {
					newPokemon.add(x);
				}
				acc.addAllPokemon(newPokemon);
			}

			ArrayList<Double> rewardProgresses = new ArrayList<>();
			for (Reward reward : PokeDex.config.getRewards()) {
				if (acc.getReward(reward.getProgress()) == null) {
					rewardProgresses.add(reward.getProgress());
				}
			}
			acc.addAllRewards(rewardProgresses);
		}
	}
}
