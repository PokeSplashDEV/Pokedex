package org.pokesplash.pokedex.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.Dexutils;

public class TradeEvent {
	public void RegisterEvent() {
		CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, e -> {

			ServerPlayerEntity player1 =
					PokeDex.server.getPlayerManager().getPlayer(e.getTradeParticipant1().getUuid());

			ServerPlayerEntity player2 =
					PokeDex.server.getPlayerManager().getPlayer(e.getTradeParticipant2().getUuid());

			if (player1 != null) {
				Dexutils.checkDex(e.getTradeParticipant1Pokemon(), player1);
			}

			if (player2 != null) {
				Dexutils.checkDex(e.getTradeParticipant2Pokemon(), player2);
			}

			return Unit.INSTANCE;
		});
	}
}
