package org.pokesplash.pokedex.config;

import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.Utils;

import java.util.concurrent.CompletableFuture;

// TODO this
public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = PokeDex.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(PokeDex.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			PokeDex.LOGGER.info("No lang.json file found for " + PokeDex.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(PokeDex.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				PokeDex.LOGGER.fatal("Could not write lang.json for " + PokeDex.MOD_ID + ".");
			}
			return;
		}
		PokeDex.LOGGER.info(PokeDex.MOD_ID + " lang file read successfully.");
	}
}
