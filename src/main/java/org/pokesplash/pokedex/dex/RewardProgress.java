package org.pokesplash.pokedex.dex;

public class RewardProgress {
	private final double progress;
	private final boolean isComplete;
	private final boolean isRedeemed;

	public RewardProgress(double progress, boolean isComplete, boolean isRedeemed) {
		this.progress = progress;
		this.isComplete = isComplete;
		this.isRedeemed = isRedeemed;
	}

	public double getProgress() {
		return progress;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public boolean isRedeemed() {
		return isRedeemed;
	}
}
