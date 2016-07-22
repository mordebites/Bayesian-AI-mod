package mc.mod.prove.match;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MatchHandler {
	private int maxRound;
	private int currentRound = 1;

	private static final int MAX_START_TIME = 3;
	private int startTime = MAX_START_TIME;

	private static final int MAX_ROUND_TIME = 5;
	private int minutesTime = MAX_ROUND_TIME;
	private int secsTime = 0;

	private boolean matchStarted = false;
	private boolean roundStarted = false;

	private static final int MAX_SIGHT_VALUE = 10;
	private int sightValue = 0;
	private boolean opponentHit = false;
	
	private boolean gamePaused = false;

	public boolean isGamePaused() {
		return gamePaused;
	}

	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}

	public int getMaxRound() {
		return maxRound;
	}

	public void setMaxRound(int maxRound) {
		this.maxRound = maxRound;
	}

	public void startMatch() {
		if (maxRound == 0) {
			throw new RuntimeException("Manca numero di round!");
		}
		matchStarted = true;
		startRound();
	}

	public void stopMatch() {
		matchStarted = false;
		roundStarted = false;
	}

	public boolean isMatchStarted() {
		return matchStarted;
	}

	public boolean isRoundStarted() {
		return roundStarted;
	}

	public int getMinutesTime() {
		return minutesTime;
	}

	public void setMinutesTime(int minutesTime) {
		this.minutesTime = minutesTime;
	}

	public int getSecsTime() {
		return secsTime;
	}

	public void setSecsTime(int secsTime) {
		this.secsTime = secsTime;
	}

	private void startRound() {

		minutesTime = MAX_ROUND_TIME;
		secsTime = 0;

		// TODO CODICE PER CAMBIARE LA POSIZIONE CORRENTE DEL PLAYER E ALZARLO
		// sull'asse y
		EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;

		playerIn.setPositionAndUpdate(playerIn.posX, playerIn.posY + 40,
				playerIn.posZ);

		roundStarted = true;
	}

	public int getMaxSightValue() {
		return MAX_SIGHT_VALUE;
	}
	
	public int getSightValue() {
		return sightValue;
	}

	public void setSightValue(int sightValue) {
		this.sightValue = (sightValue > MAX_SIGHT_VALUE) ? MAX_SIGHT_VALUE : sightValue;
	}
}
