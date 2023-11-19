package org.pokesplash.pokedex.event;

import org.pokesplash.daycare.event.DayCareEvents;
import org.pokesplash.pokedex.util.Dexutils;

public class HatchEvent {
	public void registerEvent() {
		DayCareEvents.RETRIEVE_EGG.subscribe(e -> {

			Dexutils.checkDex(e.getBaby(), e.getPlayer());
		});
	}
}
