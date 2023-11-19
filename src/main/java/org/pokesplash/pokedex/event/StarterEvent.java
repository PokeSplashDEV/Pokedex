package org.pokesplash.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.pokesplash.pokedex.util.Dexutils;

public class StarterEvent {
	public void registerEvent() {
		CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, e -> {

			Dexutils.checkDex(e.getPokemon(), e.getPlayer());

			return Unit.INSTANCE;
		});
	}
}
