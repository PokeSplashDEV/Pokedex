package org.pokesplash.pokedex.config;

import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Config {

	private ArrayList<Reward> rewards;

	public Config() {
		rewards = new ArrayList<>();
		rewards.add(new Reward(10));
		rewards.add(new Reward(20));
		rewards.add(new Reward(30));
		rewards.add(new Reward(40));
		rewards.add(new Reward(50));
		rewards.add(new Reward(60));
		rewards.add(new Reward(70));
		rewards.add(new Reward(80));
		rewards.add(new Reward(90));
		rewards.add(new Reward(100));
	}

	public ArrayList<Reward> getRewards() {
		return rewards;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeDex.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					rewards = cfg.getRewards();
				});

		if (!futureRead.join()) {
			PokeDex.LOGGER.info("No config.json file found for " + PokeDex.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeDex.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				PokeDex.LOGGER.fatal("Could not write config for " + PokeDex.MOD_ID + ".");
			}
			return;
		}
		PokeDex.LOGGER.info(PokeDex.MOD_ID + " config file read successfully");
	}
}
