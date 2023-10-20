package org.pokesplash.pokedex.account;

import com.google.gson.Gson;
import org.pokesplash.pokedex.PokeDex;
import org.pokesplash.pokedex.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public abstract class AccountProvider {
	private static HashMap<UUID, Account> accounts;

	public static Account getAccount(UUID uuid) {
		if (!accounts.containsKey(uuid)) {
			accounts.put(uuid, new Account(uuid));
		}
		return accounts.get(uuid);
	}

	public static void updateAccount(Account account) {
		accounts.put(account.getUuid(), account);
	}

	public static void init() {
		accounts = new HashMap<>();

		File dir = Utils.checkForDirectory(PokeDex.BASE_PATH + "accounts/");

		String[] files = dir.list();

		if (files.length == 0) {
			return;
		}

		for (String file : files) {
			Utils.readFileAsync(PokeDex.BASE_PATH + "accounts/", file, el -> {
				Gson gson = Utils.newGson();
				Account account = gson.fromJson(el, Account.class);
				accounts.put(account.getUuid(), account);
			});
		}
	}
}
