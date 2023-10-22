package org.pokesplash.pokedex.account;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.config.Reward;
import org.pokesplash.pokedex.dex.DexEntry;
import org.pokesplash.pokedex.dex.RewardProgress;
import org.pokesplash.pokedex.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Account {
	private final UUID uuid;
	private HashMap<Double, RewardProgress> rewards;
	private HashMap<Integer, DexEntry> pokemon;
	private boolean isComplete;

	public Account(UUID uuid) {
		this.uuid = uuid;
		isComplete = false;

		pokemon = new HashMap<>();
		for (Species species : PokemonSpecies.INSTANCE.getSpecies()) {
			pokemon.put(species.getNationalPokedexNumber(),
					new DexEntry(species.getNationalPokedexNumber(), false));
		}

		rewards = new HashMap<>();
		for (Reward reward : PokeDex.config.getRewards()) {
			rewards.put(reward.getProgress(), new RewardProgress(reward.getProgress(), false, false));
		}
		writeToFile();
	}

	public RewardProgress getReward(double progress) {
		return rewards.get(progress);
	}

	public ArrayList<RewardProgress> getAllRewards() {
		return new ArrayList<>(rewards.values());
	}

	public void completeReward(double progress) {
		rewards.put(progress, new RewardProgress(progress, true, false));
		writeToFile();
	}

	public void redeemReward(double progress) {
		rewards.put(progress, new RewardProgress(progress, true, true));
		writeToFile();
	}

	public void addAllRewards(List<Double> rewardList) {
		double progress = Utils.getDexProgress(this);
		for (double number : rewardList) {
			rewards.put(number, new RewardProgress(number, progress >= number, false));
		}
		writeToFile();
	}

	public DexEntry getPokemon(int dexNumber) {
		return pokemon.get(dexNumber);
	}

	public void setCaught(int dexNumber, boolean isCaught) {
		pokemon.put(dexNumber, new DexEntry(dexNumber, isCaught));
		writeToFile();
	}

	public void addPokemon(int dexNumber) {
		pokemon.put(dexNumber, new DexEntry(dexNumber, false));
		writeToFile();
	}

	public void addAllPokemon(List<Integer> dexNumbers) {
		for (int number : dexNumbers) {
			pokemon.put(number, new DexEntry(number, false));
		}
		writeToFile();
	}

	public ArrayList<DexEntry> getCaught() {
		ArrayList<DexEntry> caught = new ArrayList<>();
		for (DexEntry entry : pokemon.values()) {
			if (entry.isCaught()) {
				caught.add(entry);
			}
		}
		return caught;
	}

	public ArrayList<DexEntry> getNeeded() {
		ArrayList<DexEntry> needed = new ArrayList<>();
		for (DexEntry entry : pokemon.values()) {
			if (!entry.isCaught()) {
				needed.add(entry);
			}
		}
		return needed;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
		writeToFile();
	}

	public int getPokemonCount() {
		return pokemon.size();
	}

	private void writeToFile() {
		AccountProvider.updateAccount(this);
		Gson gson = Utils.newGson();

		CompletableFuture<Boolean> future = Utils.writeFileAsync(PokeDex.BASE_PATH + "accounts/",
				uuid + ".json", gson.toJson(this));

		if (!future.join()) {
			PokeDex.LOGGER.error("Unable to write account for " + uuid);
		}
	}
}
