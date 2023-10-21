package org.pokesplash.pokedex.config;

import java.util.ArrayList;

public class Reward {
	private double progress;
	private ArrayList<String> commands;

	public Reward(double progress) {
		this.progress = progress;
		commands = new ArrayList<>();
	}

	public double getProgress() {
		return progress;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}
}
