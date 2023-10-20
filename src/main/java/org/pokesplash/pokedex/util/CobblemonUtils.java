package org.pokesplash.pokedex.util;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.pokemon.Pokemon;

public abstract class CobblemonUtils {
	public static boolean isHA(Pokemon pokemon) {
		if (pokemon.getForm().getAbilities().getMapping().get(Priority.LOW) == null ||
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).size() != 1) {
			return false;
		}
		String ability =
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).get(0).getTemplate().getName();

		return pokemon.getAbility().getName().equalsIgnoreCase(ability);
	}
}
