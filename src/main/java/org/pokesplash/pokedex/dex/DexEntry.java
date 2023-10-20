package org.pokesplash.pokedex.dex;

public class DexEntry {
	private int dexNumber;
	private boolean isCaught;

	public DexEntry(int number, boolean caught) {
		dexNumber = number;
		isCaught = caught;
	}

	public int getDexNumber() {
		return dexNumber;
	}

	public boolean isCaught() {
		return isCaught;
	}
}
