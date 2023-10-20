package org.pokesplash.pokedex.ui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.pokesplash.pokedex.account.AccountProvider;

import java.util.*;

public class DexMenu {

	public Page getPage(UUID player) {
		// Creates array if implemented Pokemon.
		ArrayList<Species> implemented = new ArrayList<>(PokemonSpecies.INSTANCE.getImplemented());
		HashMap<Integer, GooeyButton> pokemon = new HashMap<>();

		// For each species, make a button.
		for (Species dexSpecies : PokemonSpecies.INSTANCE.getSpecies()) {

			// If the species isn't implemented, show unimplemented button.
			if (!implemented.contains(dexSpecies)) {
				Collection<String> unimplementedLore = new ArrayList<>();
				unimplementedLore.add("Pokemon is not currently implemented into Cobblemon.");
				pokemon.put(dexSpecies.getNationalPokedexNumber(),
						GooeyButton.builder()
								.display(new ItemStack(Items.BARRIER))
								.title(dexSpecies.getName())
								.lore(unimplementedLore)
								.build());
				continue;
			}

			// Creates the button
			Pokemon mon = new Pokemon();
			mon.setSpecies(dexSpecies);
			Collection<String> lore = new ArrayList<>();
			boolean isCaught =
					AccountProvider.getAccount(player).getPokemon(dexSpecies.getNationalPokedexNumber()).isCaught();
			lore.add(isCaught ? "Caught" : "Not Caught");

			// Adds button to hashmap.
			pokemon.put(dexSpecies.getNationalPokedexNumber(),
					GooeyButton.builder()
							.display(PokemonItem.from(mon))
							.title(dexSpecies.getName() + " - " + dexSpecies.getNationalPokedexNumber())
							.lore(lore)
							.build()
			);
		}

		// Sorts the Pokemon by dex
		ArrayList<Integer> keys = new ArrayList<>(pokemon.keySet());
		Collections.sort(keys);
		ArrayList<Button> sortedButtons = new ArrayList<>();
		for (int number : keys) {
			sortedButtons.add(pokemon.get(number));
		}

		PlaceholderButton placeholderButton = new PlaceholderButton();

		LinkedPageButton nextPage = LinkedPageButton.builder()
				.display(new ItemStack(Items.ARROW))
				.title("§7Next Page")
				.linkType(LinkType.Next)
				.build();

		LinkedPageButton previousPage = LinkedPageButton.builder()
				.display(new ItemStack(CobblemonItems.POISON_BARB))
				.title("§7Previous Page")
				.linkType(LinkType.Previous)
				.build();

		Button filler = GooeyButton.builder()
				.display(new ItemStack(Items.WHITE_STAINED_GLASS_PANE))
				.hideFlags(FlagType.All)
				.lore(new ArrayList<>())
				.title("")
				.build();

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholderButton)
				.fill(filler)
				.set(45, previousPage)
				.set(53, nextPage)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, sortedButtons, null);
		page.setTitle("Pokedex");

		setPageTitle(page);
		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("Pokedex");
			setPageTitle(next);
		}
	}

}
