package org.pokesplash.pokedex.config;

import java.util.ArrayList;

public class Reward {
	private double progress;
	private int slotNumber;
	private String itemMaterial;
	private ArrayList<String> commands;

	public Reward(double progress, int slotNumber, String itemMaterial) {
		this.progress = progress;
		this.slotNumber = slotNumber;
		this.itemMaterial = itemMaterial;
		commands = new ArrayList<>();
	}

	public double getProgress() {
		return progress;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public String getItemMaterial() {
		return itemMaterial;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}
}
