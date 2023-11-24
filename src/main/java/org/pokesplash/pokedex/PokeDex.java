package org.pokesplash.pokedex;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.pokedex.account.AccountProvider;
import org.pokesplash.pokedex.command.CommandHandler;
import org.pokesplash.pokedex.config.Config;
import org.pokesplash.pokedex.config.Lang;
import org.pokesplash.pokedex.event.*;

public class PokeDex implements ModInitializer {
	public static final String MOD_ID = "PokeDex";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Lang lang = new Lang();
	public static MinecraftServer server;

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerPlayConnectionEvents.JOIN.register(new JoinEvent());
		ServerWorldEvents.LOAD.register((a, b) -> server = a);
		new PokemonCaughtEvent().registerEvent();
		new HatchEvent().registerEvent();
		new StarterEvent().registerEvent();
		new EvolutionEvent().RegisterEvent();
		new TradeEvent().RegisterEvent();
		load();
	}

	public static void load() {
		config.init();
		lang.init();
		AccountProvider.init();
	}
}
