package org.pokesplash.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.pokesplash.pokedex.util.Dexutils;

public class EvolutionEvent {
	public void RegisterEvent() {
		CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, e -> {

			Dexutils.checkDex(e.getPokemon(), e.getPokemon().getOwnerPlayer());

			return Unit.INSTANCE;
		});
	}
}
