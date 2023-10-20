package org.pokesplash.pokedex;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.command.CommandHandler;
import org.pokesplash.pokedex.config.Config;
import org.pokesplash.pokedex.config.Lang;
import org.pokesplash.pokedex.event.JoinEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class PokeDex implements ModInitializer {
	public static final String MOD_ID = "PokeDex";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Lang lang = new Lang();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerPlayConnectionEvents.JOIN.register(new JoinEvent());
		load();
	}

	public static void load() {
		config.init();
		lang.init();
		AccountProvider.init();
	}
}
