package org.pokesplash.pokedex.dex;

public class RewardProgress {
	private final double progress;
	private final boolean isComplete;

	public RewardProgress(double progress, boolean isComplete) {
		this.progress = progress;
		this.isComplete = isComplete;
	}

	public double getProgress() {
		return progress;
	}

	public boolean isComplete() {
		return isComplete;
	}
}
