package org.pokesplash.pokedex.config;

import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.Utils;

import java.util.concurrent.CompletableFuture;

// TODO this
public class Config {
	private boolean isExample;

	public Config() {
		isExample = true;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeDex.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					isExample = cfg.isExample();
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

	public boolean isExample() {
		return isExample;
	}
}
